package it.hurts.shatterbyte.nerb.common.init;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.octostudios.octolib.modules.config.ConfigManager;

public class ConfigRegistry {
    public static NERBClientConfig CLIENT = new NERBClientConfig();
    public static NERBServerConfig SERVER = new NERBServerConfig();

    public static void registerCommon() {
        if (Platform.getEnvironment() == Env.CLIENT)
            ConfigManager.registerConfig("nerb/client", CLIENT);

        ConfigManager.registerConfig("nerb/server", SERVER);
    }
}
