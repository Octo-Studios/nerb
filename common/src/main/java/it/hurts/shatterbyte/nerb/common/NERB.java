package it.hurts.shatterbyte.nerb.common;

import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class NERB {
    public static final String MODID = "nerb";

    public static void init() {
        ConfigRegistry.registerCommon();
    }

    public static void onPlayerJoin(ServerPlayer player) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            return;

        player.awardRecipes(player.level().getServer().getRecipeManager().getRecipes());
    }
}
