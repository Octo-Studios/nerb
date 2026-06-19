package it.hurts.shatterbyte.nerb.common.init;

import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;

public class ConfigRegistry {
    public static NERBConfig GENERAL = new NERBConfig();

//    public static EMICompatConfig EMI_COMPAT = new EMICompatConfig();

    public static void registerCommon() {
        ConfigManager.register("nerb", GENERAL);

//        ConfigManager.registerConfig("nerb/compat/emi", EMI_COMPAT);
    }
}
