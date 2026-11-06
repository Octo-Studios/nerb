package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.octostudios.octolib.module.config.annotation.Prop;
import it.hurts.octostudios.octolib.module.config.impl.ConfigSide;
import it.hurts.octostudios.octolib.module.config.impl.OctoConfig;
import lombok.Data;

@Data
public class NERBServerConfig implements OctoConfig {
    @Prop(comment = """
            Recipe Book mode:
            DISABLED: Disables recipe unlocking and recipe book synchronization. Skips saved recipe book loading when clearRecipeBookData is enabled. Skips advancement listeners used only to unlock recipes. Recipe data required for crafting and recipe viewers is still synchronized.
            DISCOVERED: Automatically unlocks all recipes for each player when they join and after data pack reloads. Otherwise retains the default server-side recipe book behavior without optimizations.
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
