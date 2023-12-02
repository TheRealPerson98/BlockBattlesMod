package com.person98;

import com.person98.config.BlockBattlesConfig;
import com.person98.config.BlockBattlesConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockBattles implements ModInitializer, ModMenuApi {

    public static final Logger LOGGER = LoggerFactory.getLogger("blockbattles");

	@Override
	public void onInitialize() {
		LOGGER.info("Startig BlockBattles...");
		BlockBattlesConfig.loadConfig();

		LOGGER.info("BlockBattles started!");
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		System.out.println("Mod Menu is trying to access the config screen!");
		return BlockBattlesConfigScreen::create;
	}

}