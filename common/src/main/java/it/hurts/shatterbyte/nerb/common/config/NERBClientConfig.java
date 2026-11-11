package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.octostudios.octolib.module.config.annotation.Prop;
import it.hurts.octostudios.octolib.module.config.impl.ConfigSide;
import it.hurts.octostudios.octolib.module.config.impl.OctoConfig;
import lombok.Data;

@Data
public class NERBClientConfig implements OctoConfig {
    @Prop(comment = """
            Controls the vanilla recipe book button on this client:
            HIDDEN: Hides the button completely.
            INACTIVE: Leaves the button visible, but clicking it does nothing.
            TOGGLE: Clicking the button shows or hides the installed JEI/REI/EMI interfaces. If none is installed, the button is hidden. EMI sidebars are configured in nerb/compat/emi.
            RECIPE_BOOK: Opens the normal vanilla recipe book and takes precedence over EMI's recipe book button action. On servers running NERB, recipes must be enabled with DISCOVERED or recipeWhitelist.
            HIDDEN, INACTIVE and TOGGLE also turn off the vanilla book and skip its recipe updates to reduce client load.
            """)
    private ButtonMode buttonMode = ButtonMode.HIDDEN;

    @Override
    public ConfigSide getSide() {
        return ConfigSide.CLIENT;
    }

    public enum ButtonMode {
        HIDDEN,
        INACTIVE,
        TOGGLE,
        RECIPE_BOOK
    }
}
