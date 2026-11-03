package it.hurts.shatterbyte.nerb.fabric;

import it.hurts.shatterbyte.nerb.common.NERB;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public final class NERBFabric implements ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitializeClient() {
        initialize();
    }

    @Override
    public void onInitializeServer() {
        initialize();
    }

    private static void initialize() {
        NERB.init();

        var loader = FabricLoader.getInstance();
        var configDirectory = loader.getConfigDir();

        if (loader.getEnvironmentType() == EnvType.CLIENT)
            ConfigRegistry.CLIENT.load(configDirectory);

        ConfigRegistry.SERVER.load(configDirectory);

        CraftingManagerCompat.setupCommon(loader::isModLoaded);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                NERB.onPlayerJoin(handler.player));
    }
}
