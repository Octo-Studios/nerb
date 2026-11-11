package it.hurts.shatterbyte.nerb.common.init;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.mixin.PlayerAdvancementsAccessor;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import net.minecraft.server.MinecraftServer;

public class ConfigRegistry {
    public static final String SERVER_PATH = "nerb/server";

    public static NERBClientConfig CLIENT = new NERBClientConfig();
    public static NERBServerConfig SERVER = new NERBServerConfig();

    public static void registerCommon() {
        if (Platform.getEnvironment() == Env.CLIENT)
            ConfigManager.registerConfig("nerb/client", CLIENT);

        ConfigManager.registerConfig(SERVER_PATH, SERVER);
    }

    public static void reloadRecipeBook(MinecraftServer server) {
        server.getRecipeManager().finalizeRecipeLoading(server.getWorldData().enabledFeatures());

        for (var player : server.getPlayerList().getPlayers()) {
            var advancements = player.getAdvancements();

            advancements.stopListening();
            ((PlayerAdvancementsAccessor) advancements).nerb$registerListeners(server.getAdvancements());
            player.getRecipeBook().sendInitialRecipeBook(player);
        }
    }
}
