package it.hurts.octostudios.nerb.common;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class NERB {
    public static final String MODID = "nerb";

    public static void init() {
        LifecycleEvent.SETUP.register(() -> {
            ConfigRegistry.registerCommon();
            CraftingManagerCompat.setupCommon();
        });

        PlayerEvent.PLAYER_JOIN.register((ServerPlayer player) -> {
            if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
                return;

            MinecraftServer server = player.getServer();

            if (server == null)
                return;

            player.awardRecipes(server.getRecipeManager().getRecipes());
        });
    }
}