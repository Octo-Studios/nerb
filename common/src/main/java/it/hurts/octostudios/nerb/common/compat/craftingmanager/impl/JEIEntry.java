package it.hurts.octostudios.nerb.common.compat.craftingmanager.impl;

import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.base.ICMEntry;
import mezz.jei.common.Internal;

public class JEIEntry implements ICMEntry {
    @Override
    public void toggleVisibility() {
        Internal.getClientToggleState().toggleOverlayEnabled();
    }
}