package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.octostudios.octolib.module.config.annotation.Prop;
import it.hurts.octostudios.octolib.module.config.impl.ConfigSide;
import it.hurts.octostudios.octolib.module.config.impl.OctoConfig;
import lombok.Data;

@Data
public class NERBClientConfig implements OctoConfig {
    @Prop(comment = """
            Recipe Book button mode:
            HIDDEN: Removes the recipe book button and disables client-side recipe book processing.
            INACTIVE: Retains the recipe book button, but disables its default functionality and client-side recipe book processing.
            TOGGLE: Makes the recipe book button toggle the visibility of the JEI/REI/EMI UI instead of opening the recipe book. Disables client-side recipe book processing. If none of these mods is loaded, the button is hidden. EMI sidebars are configured in nerb/compat/emi.
            RECIPE_BOOK: Retains the recipe book button and its default functionality, including client-side recipe book processing. Takes precedence over EMI's recipe book button action. The recipe book must also be enabled on the server.
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
