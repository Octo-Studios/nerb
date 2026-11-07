package it.hurts.shatterbyte.nerb.common.recipebook;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Set;

public interface ServerRecipeBookAccess {
    void nerb$setServer(MinecraftServer server);

    Set<ResourceKey<Recipe<?>>> nerb$getRemovedRecipes();
}
