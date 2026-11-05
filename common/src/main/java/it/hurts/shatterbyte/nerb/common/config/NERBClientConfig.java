package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import lombok.Getter;

@Getter
public class NERBClientConfig extends ShatterConfig {
    @Comment("""
            Recipe Book button mode:
            HIDDEN: Removes the recipe book button and disables client-side recipe book processing.
            INACTIVE: Retains the recipe book button, but disables its default functionality and client-side recipe book processing.
            TOGGLE: Makes the recipe book button toggle the visibility of the JEI/REI UI instead of opening the recipe book. Disables client-side recipe book processing. If neither JEI nor REI is loaded, the button is hidden.
            RECIPE_BOOK: Retains the recipe book button and its default functionality, including client-side recipe book processing. The recipe book must also be enabled on the server.
            """)
    private ButtonMode buttonMode = ButtonMode.HIDDEN;

    @Override
    public String getName() {
        return "nerb";
    }

    @Override
    public ConfigSide getSide() {
        return ConfigSide.CLIENT;
    }

    @Override
    public int getSchemaVersion() {
        return 1;
    }

    public enum ButtonMode {
        HIDDEN,
        INACTIVE,
        TOGGLE,
        RECIPE_BOOK
    }
}
