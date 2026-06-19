package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.shatterbyte.shatterlib.module.config.ConfigSide;
import it.hurts.shatterbyte.shatterlib.module.config.ShatterConfig;
import it.hurts.shatterbyte.shatterlib.module.config.type.annotation.Comment;
import lombok.Getter;

@Getter
public class NERBConfig extends ShatterConfig {

    @Comment("""
            Recipe Book mode:
            DISABLED: Removes the recipe book button with its default functionality from your inventory.
            ENABLED: Retains the recipe book button in your inventory, but its default functionality remains disabled.
            DISCOVERED: Automatically unlocks all existing recipes in your recipe book. Does not apply any optimizations.
            TOGGLE: Keeps the recipe book button in your inventory, but instead of toggling the recipe book, it toggles the visibility of the JEI/REI/EMI UI. Its default functionality remains disabled. Sometimes can be additionally configured in separate configuration files for compats.
            """)
    private ButtonMode buttonMode = ButtonMode.TOGGLE;

    @Override
    public String getName() {
        return "nerb";
    }

    @Override
    public ConfigSide getSide() {
        return ConfigSide.COMMON;
    }

    @Override
    public int getSchemaVersion() {
        return 1;
    }

    public enum ButtonMode {
        DISCOVERED,
        ENABLED,
        DISABLED,
        TOGGLE
    }
}
