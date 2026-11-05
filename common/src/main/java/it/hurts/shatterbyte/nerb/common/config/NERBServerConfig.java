package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import lombok.Getter;

@Getter
public class NERBServerConfig extends ShatterConfig {
    @Comment("""
            Recipe Book mode:
            DISABLED: Disables recipe book data preparation, recipe unlocking, and recipe synchronization. Skips saved recipe book loading when clearRecipeBookData is enabled. Skips advancement listeners used only to unlock recipes.
            DISCOVERED: Automatically unlocks all recipes for each player when they join and after data pack reloads. Otherwise retains the default server-side recipe book behavior without optimizations.
            """)
    private RecipeBookMode recipeBookMode = RecipeBookMode.DISABLED;

    @Comment("""
            Only applies when recipeBookMode is DISABLED.
            true: Skips loading saved recipe book data, replacing it with an empty book when the player is saved. Previously unlocked recipes and book settings are lost.
            false: Loads and preserves existing recipe book data without synchronizing it to the client.
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
