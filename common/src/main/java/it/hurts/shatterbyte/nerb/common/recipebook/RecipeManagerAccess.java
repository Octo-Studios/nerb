package it.hurts.shatterbyte.nerb.common.recipebook;

import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;

public interface RecipeManagerAccess {
    ClientboundRecipeBookAddPacket nerb$getDiscoveredRecipes();
}
