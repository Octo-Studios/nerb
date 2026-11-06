package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NERBServerConfig extends ShatterConfig {
    @Comment("""
            Recipe Book mode, unless overridden by the lists below:
            DISABLED: Disables recipe book data preparation, recipe unlocking, and recipe synchronization. Skips saved recipe book loading when clearRecipeBookData is enabled. Skips advancement listeners used only to unlock recipes.
            DISCOVERED: Keeps all recipes unlocked and synchronizes them on join and after data pack reloads. Reduces server load by skipping individual recipe unlock tracking while preserving book settings.
            """)
    private RecipeBookMode recipeBookMode = RecipeBookMode.DISABLED;

    @Comment("""
            Recipes that bypass NERB and follow normal vanilla unlocking rules, even in DISABLED. Takes priority over recipeBlacklist.
            Accepts recipe IDs (minecraft:stick) and regular expressions matching the full ID (regex:minecraft:.*_planks).
            """)
    private List<String> recipeWhitelist = new ArrayList<>();

    @Comment("""
            Recipes to disable in the recipe book, even in DISCOVERED. Whitelisted recipes take priority.
            Accepts recipe IDs (minecraft:stick) and regular expressions matching the full ID (regex:minecraft:.*_planks).
            """)
    private List<String> recipeBlacklist = new ArrayList<>();

    @Comment("""
            Only applies when recipeBookMode is DISABLED.
            true: Clears saved recipe progress except for whitelisted recipes. Book settings are also cleared when no whitelisted recipes are available.
            false: Preserves existing recipe book data. Only whitelisted recipes are synchronized to the client.
            """)
    private boolean clearRecipeBookData = true;

    @Override
    public String getName() {
        return "nerb";
    }

    @Override
    public ConfigSide getSide() {
        return ConfigSide.SERVER;
    }

    @Override
    public int getSchemaVersion() {
        return 1;
    }

    public enum RecipeBookMode {
        DISABLED,
        DISCOVERED
    }
}
