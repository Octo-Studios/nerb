package it.hurts.shatterbyte.nerb.fabric;

import it.hurts.shatterbyte.nerb.common.NERB;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public final class NERBFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NERB.init();

        CraftingManagerCompat.setupCommon(FabricLoader.getInstance()::isModLoaded);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                NERB.onPlayerJoin(handler.player));
    }
}
