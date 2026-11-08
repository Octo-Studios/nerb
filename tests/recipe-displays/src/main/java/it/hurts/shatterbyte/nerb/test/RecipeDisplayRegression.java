package it.hurts.shatterbyte.nerb.test;

import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import java.util.ArrayList;
import java.util.List;

/** Run on a started server with NERB's default DISABLED recipe-book mode. */
public final class RecipeDisplayRegression {
    public static int verify(MinecraftServer server) {
        int checks = 0;
        for (String id : List.of("minecraft:cake", "minecraft:bread", "minecraft:pumpkin_pie")) {
            var recipe = server.getRecipeManager().getRecipes().stream()
                    .filter(it -> it.id().identifier().toString().equals(id)).findFirst().orElseThrow();
            var displays = new ArrayList<RecipeDisplayEntry>();
            server.getRecipeManager().listDisplaysForRecipe(recipe.id(), displays::add);
            if (displays.isEmpty()) throw new AssertionError("Missing shared recipe display: " + id);
            checks++;
            for (var display : displays) {
                var resolved = server.getRecipeManager().getRecipeFromDisplay(display.id());
                if (resolved == null || !resolved.parent().id().equals(recipe.id()))
                    throw new AssertionError("Recipe display does not resolve to " + id);
                checks++;
            }
            var book = new ServerRecipeBook(server.getRecipeManager()::listDisplaysForRecipe);
            // DISABLED must return before accessing the player or unlocking recipes.
            if (book.addRecipes(List.of(recipe), null) != 0)
                throw new AssertionError("Disabled recipe book unlocked " + id);
            checks++;
        }
        return checks;
    }
}
