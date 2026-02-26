package it.hurts.octostudios.nerb.common.init;

import it.hurts.octostudios.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;

public class ConfigRegistry {
    public static NERBConfig GENERAL = new NERBConfig();

//    public static EMICompatConfig EMI_COMPAT = new EMICompatConfig();

    public static void registerCommon() {
        ConfigManager.registerConfig("nerb/general", GENERAL);

//        ConfigManager.registerConfig("nerb/compat/emi", EMI_COMPAT);
    }
}