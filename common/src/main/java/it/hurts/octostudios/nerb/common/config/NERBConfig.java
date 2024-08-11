package it.hurts.octostudios.nerb.common.config;

import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.octolib.modules.config.annotations.Prop;
import it.hurts.octostudios.octolib.modules.config.impl.OctoConfig;
import lombok.Data;

@Data
public class NERBConfig implements OctoConfig {
    @Prop(comment = """
            Mode of the recipe book button. Supported modes:
            DISABLED: Removes the recipe book button from your inventory
            ENABLED: Keeps the recipe book button in your inventory (its vanilla functionality will still be disabled)
            """)
    private ButtonMode buttonMode = ButtonMode.DISABLED;
}