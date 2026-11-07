package it.hurts.shatterbyte.nerb.common.init;

import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.mixin.PlayerAdvancementsAccessor;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import net.minecraft.server.MinecraftServer;

public class ConfigRegistry {
    public static NERBClientConfig CLIENT = new NERBClientConfig();
    public static NERBServerConfig SERVER = new NERBServerConfig();

    public static void registerCommon() {
        ConfigManager.register("nerb", CLIENT);
        ConfigManager.register("nerb", SERVER);
    }

    public static void reloadRecipeBook(MinecraftServer server) {
        server.getRecipeManager().finalizeRecipeLoading(server.getWorldData().enabledFeatures());

        for (var player : server.getPlayerList().getPlayers()) {
            var advancements = player.getAdvancements();

            advancements.clearTriggers();
            ((PlayerAdvancementsAccessor) advancements).nerb$registerListeners(server.getAdvancements());
            player.getRecipeBook().sendInitialRecipeBook(player);
        }
    }
}
