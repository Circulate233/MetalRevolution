package com.circulation.metal_revolution.common;

import net.minecraft.item.ItemStack;

import project.studio.manametalmod.Lapuda.TileEntityGilded;

public class MRTileEntityGilded extends TileEntityGilded {

    private static final int[] m$AllSlot = { 1, 2, 3, 4, 5 };

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return this.getItemGold(itemstack) > 0;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }
}
