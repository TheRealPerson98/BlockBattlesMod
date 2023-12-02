package com.person98.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

public class ServerChecker {

    private static final String TARGET_SERVER_ADDRESS = "Play.BlockBattles.Org";

    public static boolean isOnTargetServer() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null && client.getCurrentServerEntry() != null) {
            ServerInfo currentServer = client.getCurrentServerEntry();
            return currentServer.address.equalsIgnoreCase(TARGET_SERVER_ADDRESS);
        }

        return false;
    }
}