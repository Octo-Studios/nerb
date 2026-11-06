package it.hurts.shatterbyte.nerb.common.recipebook;

import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;

public interface RecipeManagerAccess {
    RecipeBookRules nerb$getRecipeBookRules();

    ClientboundRecipeBookAddPacket nerb$getDiscoveredRecipes();
}
