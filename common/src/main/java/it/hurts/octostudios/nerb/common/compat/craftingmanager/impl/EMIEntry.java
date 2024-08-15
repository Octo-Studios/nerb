package it.hurts.octostudios.nerb.common.compat.craftingmanager.impl;

import dev.emi.emi.screen.EmiScreenManager;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.base.ICMEntry;

public class EMIEntry implements ICMEntry {
    @Override
    public void toggleVisibility() {
        EmiScreenManager.toggleVisibility(false);
    }
}