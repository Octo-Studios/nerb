package it.hurts.shatterbyte.nerb.common.recipebook;

import com.mojang.logging.LogUtils;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RecipeBookRules {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final boolean discovered;

    private final Set<ResourceKey<Recipe<?>>> whitelist = new HashSet<>();
    private final Set<ResourceKey<Recipe<?>>> blacklist = new HashSet<>();

    public RecipeBookRules(Iterable<RecipeHolder<?>> recipes, NERBServerConfig config) {
        discovered = config.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED;

        var whitelistPatterns = compilePatterns(config.getRecipeWhitelist());
        var blacklistPatterns = compilePatterns(config.getRecipeBlacklist());

        if (whitelistPatterns.isEmpty() && blacklistPatterns.isEmpty())
            return;

        for (var recipe : recipes) {
            var id = recipe.id().location().toString();

            if (whitelistPatterns.stream().anyMatch(pattern -> pattern.matcher(id).matches()))
                whitelist.add(recipe.id());
            else if (blacklistPatterns.stream().anyMatch(pattern -> pattern.matcher(id).matches()))
                blacklist.add(recipe.id());
        }
    }

    public boolean isDiscoveredMode() {
        return discovered;
    }

    public boolean hasVanillaRecipes() {
        return !whitelist.isEmpty();
    }

    public boolean isVanilla(ResourceKey<Recipe<?>> recipe) {
        return whitelist.contains(recipe);
    }

    public boolean isDiscovered(ResourceKey<Recipe<?>> recipe) {
        return discovered && !whitelist.contains(recipe) && !blacklist.contains(recipe);
    }

    public boolean isEnabled(ResourceKey<Recipe<?>> recipe) {
        return isVanilla(recipe) || isDiscovered(recipe);
    }

    private static List<Pattern> compilePatterns(List<String> entries) {
        var patterns = new ArrayList<Pattern>();

        for (var entry : entries) {
            try {
                patterns.add(Pattern.compile(entry.startsWith("regex:") ? entry.substring(6) : Pattern.quote(entry)));
            } catch (PatternSyntaxException exception) {
                LOGGER.warn("Ignoring invalid NERB recipe pattern '{}': {}", entry, exception.getDescription());
            }
        }

        return patterns;
    }
}
