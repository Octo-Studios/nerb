package it.hurts.octostudios.nerb.common.init;

import it.hurts.octostudios.nerb.common.config.NERBConfig;
import it.hurts.octostudios.octolib.modules.config.ConfigManager;

public class ConfigRegistry {
    public static NERBConfig CONFIG = new NERBConfig();

    public static void registerCommon() {
        ConfigManager.registerConfig("nerb", CONFIG);
    }
}