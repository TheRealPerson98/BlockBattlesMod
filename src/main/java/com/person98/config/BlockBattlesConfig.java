package com.person98.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class BlockBattlesConfig {
    private static BlockBattlesConfigData configData = new BlockBattlesConfigData();

    private static class BlockBattlesConfigData {
        boolean autogg = true;
        String autoggMessage = "GG";
        String apiKey = "OpenTicketTOGetAPiKey"; // New field
        boolean statsOnReady = true; // New field

        boolean autoJoinQueue = false; // New field for auto-join
        String queueType = "Casual"; // New field for queue type, defaulting to Casual
    }

    private static final File CONFIG_FILE = new File("./config/blockbattles.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (Reader reader = new FileReader(CONFIG_FILE)) {
                configData = GSON.fromJson(reader, BlockBattlesConfigData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(configData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAutoGGEnabled() {
        return configData.autogg;
    }

    public static String getAutoGGMessage() {
        return configData.autoggMessage;
    }

    public static void setAutoGG(boolean autogg) {
        configData.autogg = autogg;
    }

    public static void setAutoGGMessage(String message) {
        configData.autoggMessage = message;
    }
    public static String getApiKey() {
        return configData.apiKey;
    }

    public static void setApiKey(String apiKey) {
        configData.apiKey = apiKey;
    }

    public static boolean isStatsOnReady() {
        return configData.statsOnReady;
    }

    public static void setStatsOnReady(boolean statsOnReady) {
        configData.statsOnReady = statsOnReady;
    }
    public static boolean isAutoJoinQueueEnabled() {
        return configData.autoJoinQueue;
    }

    public static void setAutoJoinQueue(boolean autoJoinQueue) {
        configData.autoJoinQueue = autoJoinQueue;
    }

    public static String getQueueType() {
        return configData.queueType;
    }

    public static void setQueueType(String queueType) {
        configData.queueType = queueType;
    }
}
