package it.hurts.shatterbyte.nerb.common.recipebook;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import net.minecraft.resources.ResourceLocation;
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

    private final Set<ResourceLocation> whitelist = new HashSet<>();
    private final ImmutableSet<ResourceLocation> discoveredRecipes;

    public RecipeBookRules(Iterable<RecipeHolder<?>> recipes, NERBServerConfig config) {
        discovered = config.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED;

        var whitelistPatterns = compilePatterns(config.getRecipeWhitelist());
        var blacklistPatterns = compilePatterns(config.getRecipeBlacklist());

        var discoveredIds = ImmutableSet.<ResourceLocation>builder();
        if (discovered || !whitelistPatterns.isEmpty()) {
            for (var recipe : recipes) {
                var id = recipe.id();
                if (matches(whitelistPatterns, id))
                    whitelist.add(id);
                else if (discovered && !recipe.value().isSpecial() && !matches(blacklistPatterns, id))
                    discoveredIds.add(id);
            }
        }
        discoveredRecipes = discoveredIds.build();
    }

    public boolean isDiscoveredMode() {
        return discovered;
    }

    public boolean hasVanillaRecipes() {
        return !whitelist.isEmpty();
    }

    public boolean isVanilla(ResourceLocation recipe) {
        return whitelist.contains(recipe);
    }

    public boolean isDiscovered(ResourceLocation recipe) {
        return discoveredRecipes.contains(recipe);
    }

    public ImmutableList<ResourceLocation> discoveredRecipes() {
        return discoveredRecipes.asList();
    }

    private static boolean matches(List<Pattern> patterns, ResourceLocation recipe) {
        if (patterns.isEmpty())
            return false;

        var id = recipe.toString();
        return patterns.stream().anyMatch(pattern -> pattern.matcher(id).matches());
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
