package com.person98.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;

public class ClosestPlayerFinder {

    public static OtherClientPlayerEntity getClosestSurvivalPlayer(ClientPlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return null;
        }

        OtherClientPlayerEntity closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        for (AbstractClientPlayerEntity otherPlayer : client.world.getPlayers()) {
            if (otherPlayer.equals(player) || otherPlayer.getAbilities().creativeMode || otherPlayer.isSpectator()) {
                continue; // Skip if it's the same player or if the other player is in creative/spectator mode
            }

            double distance = player.squaredDistanceTo(otherPlayer);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = (OtherClientPlayerEntity) otherPlayer;
            }
        }

        return closestPlayer; // Return the closest player or null if none found
    }
}