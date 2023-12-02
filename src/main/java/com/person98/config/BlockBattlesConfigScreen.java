package com.person98.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BlockBattlesConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.blockbattles.config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory generalCategory = builder.getOrCreateCategory(Text.translatable("category.blockbattles.general"));

        // General Settings
        generalCategory.addEntry(entryBuilder.startTextDescription(Text.translatable("header.blockbattles.general")).build());
        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.blockbattles.autogg"), BlockBattlesConfig.isAutoGGEnabled()).setDefaultValue(true).setSaveConsumer(BlockBattlesConfig::setAutoGG).build());
        generalCategory.addEntry(entryBuilder.startStrField(Text.translatable("option.blockbattles.autoggmessage"), BlockBattlesConfig.getAutoGGMessage()).setDefaultValue("GG").setSaveConsumer(BlockBattlesConfig::setAutoGGMessage).build());

        // Stats on Ready
        generalCategory.addEntry(entryBuilder.startTextDescription(Text.translatable("header.blockbattles.statsonready")).build());
        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.blockbattles.statsonready"), BlockBattlesConfig.isStatsOnReady()).setDefaultValue(true).setSaveConsumer(BlockBattlesConfig::setStatsOnReady).build());

        // Auto Queue Settings
        generalCategory.addEntry(entryBuilder.startTextDescription(Text.translatable("header.blockbattles.autoqueue")).build());
        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.blockbattles.autojoinqueue"), BlockBattlesConfig.isAutoJoinQueueEnabled()).setDefaultValue(false).setSaveConsumer(BlockBattlesConfig::setAutoJoinQueue).build());

        // Queue Type Selector
        String[] queueTypes = new String[]{"Casual", "Ranked", "Random"};
        int defaultIndex = getIndex(BlockBattlesConfig.getQueueType());
        generalCategory.addEntry(builder.entryBuilder()
                .startSelector(Text.translatable("option.blockbattles.queuetype"),
                        queueTypes,
                        defaultIndex)
                .setSaveConsumer(selectedString -> {
                    // Convert the selected string back to the corresponding index
                    for (int i = 0; i < queueTypes.length; i++) {
                        if (queueTypes[i].equals(selectedString)) {
                            BlockBattlesConfig.setQueueType(queueTypes[i]);
                            break;
                        }
                    }
                    BlockBattlesConfig.saveConfig();
                    BlockBattlesConfig.loadConfig();
                })
                .build());
        // API Key Settings
        generalCategory.addEntry(entryBuilder.startTextDescription(Text.translatable("header.blockbattles.apikey")).build());
        generalCategory.addEntry(entryBuilder.startStrField(Text.translatable("option.blockbattles.apikey"), BlockBattlesConfig.getApiKey()).setDefaultValue("OpenTicketTOGetAPiKey").setSaveConsumer(BlockBattlesConfig::setApiKey).build());

        return builder.build();
    }

    private static int getIndex(String queueType) {
        return switch (queueType) {
            case "Ranked" -> 1;
            case "Random" -> 2;
            default -> 0;
        };
    }

}
