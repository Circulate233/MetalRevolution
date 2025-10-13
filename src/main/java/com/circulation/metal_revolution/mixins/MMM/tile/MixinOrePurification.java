package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.tileentity.TileEntityOrePurification;

@Mixin(TileEntityOrePurification.class)
public class MixinOrePurification {

    @Unique
    private static final int[] m$AllSlot = {0, 1};

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot == 0;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack item, int face) {
        return slot == 1;
    }

}
