package it.hurts.shatterbyte.nerb.common.config.compat;

import it.hurts.octostudios.octolib.module.config.annotation.Prop;
import it.hurts.octostudios.octolib.module.config.impl.ConfigSide;
import it.hurts.octostudios.octolib.module.config.impl.OctoConfig;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class EMICompatConfig implements OctoConfig {
    @Prop(comment = "Specifies which EMI sidebar page types to hide/show when the recipe book button is pressed. Only works when TOGGLE mode is enabled. A sidebar is hidden when its active page has a listed type; matching subpanels are hidden separately. Supported values: index, craftables, favorites, lookup-history, craft-history, chess. The chess type refers to EMI's chess board. Set toggleableSidebars: [] to toggle the entire EMI UI.")
    private List<String> toggleableSidebars = Arrays.asList("craftables");

    @Override
    public ConfigSide getSide() {
        return ConfigSide.CLIENT;
    }
}
