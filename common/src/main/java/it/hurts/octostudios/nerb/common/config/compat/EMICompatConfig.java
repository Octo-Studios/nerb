package it.hurts.octostudios.nerb.common.config.compat;

import it.hurts.octostudios.octolib.modules.config.annotations.Prop;
import it.hurts.octostudios.octolib.modules.config.impl.OctoConfig;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class EMICompatConfig implements OctoConfig {
    @Prop(comment = "Specifies the list of sidebars that should be disabled/enabled when the recipe book button is pressed. Only works when TOGGLE mode is enabled. Supported values: index, craftables, favorites, lookup-history, craft-history, chess")
    private List<String> toggleableSidebars = Arrays.asList("craftables");
}