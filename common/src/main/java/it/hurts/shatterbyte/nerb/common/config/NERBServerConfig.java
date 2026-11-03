package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import lombok.Getter;

@Getter
public class NERBServerConfig extends ShatterConfig {
    @Comment("""
            Recipe Book mode:
            DISABLED: Disables recipe book data preparation, recipe unlocking, saved recipe book loading, and recipe synchronization. Skips advancement listeners used only to unlock recipes.
            DISCOVERED: Automatically unlocks all recipes for each player when they join. Otherwise retains the default server-side recipe book behavior without optimizations.
            """)
    private RecipeBookMode recipeBookMode = RecipeBookMode.DISABLED;

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
