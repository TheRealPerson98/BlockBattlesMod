package com.person98.util;


import net.minecraft.client.MinecraftClient;

public class ChatHandler {

    public static void sendCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.networkHandler.sendChatCommand(command);
        }
    }

    public static void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.networkHandler.sendChatMessage(message);
        }
    }
}