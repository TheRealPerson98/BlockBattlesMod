package com.person98.mixin;

import com.person98.config.BlockBattlesConfig;
import com.person98.util.ChatHandler;
import com.person98.util.DelayedTaskManager;
import com.person98.util.ServerChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinAutoGG {
    @Unique
    private static boolean messageSent = false;

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!BlockBattlesConfig.isAutoGGEnabled()) {
            return; // Auto GG is disabled
        }

        if (!ServerChecker.isOnTargetServer()) {
            return; // Not on the target server
        }

        Text message = packet.content();
        String chatMessage = message.getString();

        if (!messageSent && chatMessage.matches("You have (lost to|defeated) .+ in a casual battle!")) {
            ChatHandler.sendMessage(BlockBattlesConfig.getAutoGGMessage());
            messageSent = true;
        }
        DelayedTaskManager.scheduleDelayedTask(
                c -> messageSent = false,
                40
        );
    }
}
