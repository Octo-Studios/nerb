package it.hurts.shatterbyte.nerb.api;

import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;

public final class NERBApi {
    private NERBApi() {

    }

    public static boolean isClientRecipeBookDisabled() {
        return ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK;
    }

    public static boolean isServerRecipeBookDisabled() {
        return ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISABLED;
    }
}
