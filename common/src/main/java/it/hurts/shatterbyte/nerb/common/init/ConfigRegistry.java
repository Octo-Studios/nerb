package it.hurts.shatterbyte.nerb.common.init;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.config.compat.EMICompatConfig;
import it.hurts.octostudios.octolib.module.config.ConfigManager;

public class ConfigRegistry {
    public static NERBClientConfig CLIENT = new NERBClientConfig();
    public static NERBServerConfig SERVER = new NERBServerConfig();
    public static EMICompatConfig EMI_COMPAT = new EMICompatConfig();

    public static void registerCommon() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            ConfigManager.registerConfig("nerb/client", CLIENT);
            ConfigManager.registerConfig("nerb/compat/emi", EMI_COMPAT);
        }

        ConfigManager.registerConfig("nerb/server", SERVER);
    }
}
