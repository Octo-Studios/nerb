package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import lombok.Getter;

@Getter
public class NERBClientConfig extends ShatterConfig {
    @Comment("""
            Controls the vanilla recipe book button on this client:
            HIDDEN: Hides the button completely.
            INACTIVE: Leaves the button visible, but clicking it does nothing.
            TOGGLE: Clicking the button shows or hides the installed JEI/REI interfaces. If neither mod is installed, the button is hidden.
            RECIPE_BOOK: Opens the normal vanilla recipe book. On servers running NERB, recipes must be enabled with DISCOVERED or recipeWhitelist.
            HIDDEN, INACTIVE and TOGGLE also turn off the vanilla book and skip its recipe updates to reduce client load.
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
