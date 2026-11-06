package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.annotation.Prop;
import it.hurts.shatterbyte.shatterlib.module.config.impl.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.impl.ShatterConfig;
import lombok.Data;

@Data
public class NERBServerConfig implements ShatterConfig {
    @Prop(comment = """
            Recipe Book mode:
            DISABLED: Disables recipe book data preparation, recipe unlocking, and recipe synchronization. Skips saved recipe book loading when clearRecipeBookData is enabled. Skips advancement listeners used only to unlock recipes.
            DISCOVERED: Keeps all recipes unlocked and synchronizes them on join and after data pack reloads. Reduces server load by skipping individual recipe unlock tracking while preserving book settings.
            """)
    private RecipeBookMode recipeBookMode = RecipeBookMode.DISABLED;

    @Prop(comment = """
            Only applies when recipeBookMode is DISABLED.
            true: Skips loading saved recipe book data, replacing it with an empty book when the player is saved. Previously unlocked recipes and book settings are lost.
            false: Loads and preserves existing recipe book data without synchronizing it to the client.
            """)
    private boolean clearRecipeBookData = true;

    @Override
    public ConfigSide getSide() {
        return ConfigSide.SERVER;
    }

    public enum RecipeBookMode {
        DISABLED,
        DISCOVERED
    }
}
