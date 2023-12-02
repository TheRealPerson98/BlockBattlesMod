package com.person98.mixin;

import com.person98.config.BlockBattlesConfig;
import com.person98.util.ChatHandler;
import com.person98.util.ServerChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinAutoQueue {
    @Shadow @Final private static Logger LOGGER;
    @Unique
    private int delayTicks = 0;

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text message = packet.content();
        String chatMessage = message.getString();

        // Check for a specific message to start delay
        if (chatMessage.equals("Sending you to alpha...") && BlockBattlesConfig.isAutoJoinQueueEnabled()) {
            LOGGER.info("Detected queue message: {}", chatMessage);
            LOGGER.info("Checking if on target server...");

            if (!ServerChecker.isOnTargetServer()) {
                LOGGER.info("Not on target server, skipping...");
                return; // Not on the target server
            }
            delayTicks = 100; // 5 seconds delay (20 ticks per second * 5)
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (delayTicks > 0) {
            delayTicks--;
            if (delayTicks == 0) {
                String queueCommand = getQueueCommand(BlockBattlesConfig.getQueueType());
                LOGGER.info("Sending queue command: {}", queueCommand);
                ChatHandler.sendCommand(queueCommand);
            }
        }
    }

    @Unique
    private String getQueueCommand(String queueType) {
        return switch (queueType) {
            case "Casual" -> "play casual";
            case "Ranked" -> "play ranked";
            case "Random" -> "play random";
            default -> "play casual"; // Default to casual if unknown queue type
        };
    }
}
