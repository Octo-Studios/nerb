package it.hurts.shatterbyte.nerb.common.recipebook;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

public interface ServerRecipeBookAccess {
    void nerb$setServer(MinecraftServer server);

    Set<ResourceLocation> nerb$getRemovedRecipes();

    RecipeBookRules nerb$getSentRules();

    void nerb$removeStaleRecipes(ServerPlayer player);
}
