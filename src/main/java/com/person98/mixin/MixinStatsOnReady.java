package com.person98.mixin;

import com.google.gson.Gson;
import com.person98.config.BlockBattlesConfig;
import com.person98.util.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinStatsOnReady {
    @Unique
    private static boolean messageSent = false;

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!BlockBattlesConfig.isStatsOnReady()) {
            return; // Stats on Ready is disabled
        }

        if (!ServerChecker.isOnTargetServer()) {
            return; // Not on the target server
        }

        Text message = packet.content();
        String chatMessage = message.getString();

        if (!messageSent && chatMessage.matches("Both players are ready!")) {
            messageSent = true; // Set the flag as soon as we start processing

            String apiKey = BlockBattlesConfig.getApiKey();
            OtherClientPlayerEntity closestPlayer = ClosestPlayerFinder.getClosestSurvivalPlayer(MinecraftClient.getInstance().player);
            if (closestPlayer != null) {
                CompletableFuture<String> statsFuture = ApiFetcher.fetchPlayerStats(apiKey, closestPlayer.getEntityName());
                statsFuture.thenAccept(statsJson -> {
                    PlayerStats stats = new Gson().fromJson(statsJson, PlayerStats.class); // Parsing JSON
                    String formattedMessage = formatStatsMessage(stats); // Formatting message
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.literal(formattedMessage), false);
                }).thenRun(() -> {
                    // Reset messageSent after the entire operation is completed
                    messageSent = false;
                });
            } else {
                // If no closest player is found, reset the flag
                messageSent = false;
            }
        }
    }
    // Helper method to format the stats message
    @Unique
    private String formatStatsMessage(PlayerStats stats) {
        // Calculate win/lose percentages
        double casualWinLosePercent = calculateWinLosePercentage(stats.casual_wins, stats.casual_losses);
        double rankedWinLosePercent = calculateWinLosePercentage(stats.ranked_wins, stats.ranked_losses);

        // Formatting with color and a more horizontal layout
        return "§bStats (§a" + stats.name + "§b)\n" +
                "§eCasual: §fWins: §a" + stats.casual_wins + " §fLosses: §c" + stats.casual_losses + " §fWin/Lose%: §6" + String.format("%.2f", casualWinLosePercent) + "%\n" +
                "§eRanked: §fWins: §a" + stats.ranked_wins + " §fLosses: §c" + stats.ranked_losses + " §fWin/Lose%: §6" + String.format("%.2f", rankedWinLosePercent) + "%\n" +
                "§fElo: §6" + stats.elo + " §fStreak: §6" + stats.streak;
    }

    @Unique
    private double calculateWinLosePercentage(int wins, int losses) {
        if (wins + losses == 0) return 0.0;
        return ((double) wins / (wins + losses)) * 100;
    }

}