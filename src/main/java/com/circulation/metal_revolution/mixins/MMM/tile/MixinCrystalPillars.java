package com.circulation.metal_revolution.mixins.MMM.tile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.tileentity.TileEntityCrystalPillars;

@Mixin(TileEntityCrystalPillars.class)
public class MixinCrystalPillars {

    @Unique
    private static final int[] m$AllSlot = {0};

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

}
