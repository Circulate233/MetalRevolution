package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.tileentity.TileEntityManaCraftTable;

@Mixin(TileEntityManaCraftTable.class)
public class MixinTileEntityManaCraftTable {

    @Unique
    private static final int[] m$AllSlot = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite()
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return side < 9;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }
}
