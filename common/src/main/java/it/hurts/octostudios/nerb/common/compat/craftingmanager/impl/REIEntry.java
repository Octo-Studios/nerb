package it.hurts.octostudios.nerb.common.compat.craftingmanager.impl;

import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.base.ICMEntry;
import me.shedaniel.rei.api.client.REIRuntime;

public class REIEntry implements ICMEntry {
    @Override
    public void toggleVisibility() {
        REIRuntime.getInstance().toggleOverlayVisible();
    }
}