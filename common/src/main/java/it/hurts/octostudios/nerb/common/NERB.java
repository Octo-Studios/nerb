package it.hurts.octostudios.nerb.common;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.octostudios.nerb.common.config.NERBConfig;
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
            if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
                return;

            MinecraftServer server = player.level().getServer();

            player.awardRecipes(server.getRecipeManager().getRecipes());
        });
    }
}