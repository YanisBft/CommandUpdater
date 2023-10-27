package com.yanisbft.commandupdater;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandUpdater implements ClientModInitializer {
	public static final String MOD_ID = "commandupdater";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}