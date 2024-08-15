package it.hurts.octostudios.nerb.common.config;

import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.octolib.modules.config.annotations.Prop;
import it.hurts.octostudios.octolib.modules.config.impl.OctoConfig;
import lombok.Data;

@Data
public class NERBConfig implements OctoConfig {
    @Prop(comment = """
            Recipe Book mode:
            DISABLED: Removes the recipe book button with its default functionality from your inventory.
            ENABLED: Retains the recipe book button in your inventory, but its default functionality remains disabled.
            DISCOVERED: Automatically unlocks all existing recipes in your recipe book. Does not apply any optimizations.
            TOGGLE: Keeps the recipe book button in your inventory, but instead of toggling the recipe book, it toggles the visibility of the JEI/REI/EMI UI. Its default functionality remains disabled.
            """)
    private ButtonMode buttonMode = ButtonMode.TOGGLE;
}