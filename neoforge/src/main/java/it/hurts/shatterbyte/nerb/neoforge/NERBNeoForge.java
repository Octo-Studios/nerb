package it.hurts.shatterbyte.nerb.neoforge;

import it.hurts.shatterbyte.nerb.common.NERB;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.shatterlib.platform.ShatterLibServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(NERB.MODID)
public final class NERBNeoForge {
    public NERBNeoForge(IEventBus modBus) {
        modBus.addListener(NERBNeoForge::onCommonSetup);

        CraftingManagerCompat.setupCommon(modId -> ModList.get().isLoaded(modId));

        NeoForge.EVENT_BUS.addListener(NERBNeoForge::onPlayerJoin);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NERB.init();

            var platform = ShatterLibServices.platform();
            var configDirectory = platform.getConfigDirectory();

            if (platform.isClientEnvironment())
                ConfigRegistry.CLIENT.load(configDirectory);

            ConfigRegistry.SERVER.load(configDirectory);
        });
    }

    private static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            NERB.onPlayerJoin(player);
    }
}
