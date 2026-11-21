package com.circulation.metal_revolution.common;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import project.studio.manametalmod.tileentity.TileEntityCheeseMaker;

public class MRTileEntityCheeseMaker extends TileEntityCheeseMaker {

    private static final int[] AllSlot = { 0, 1, 2 };

    @Override
    public int getSizeInventory() {
        return AllSlot.length;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return AllSlot;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return switch (slot) {
            case 0 -> item != null && item.getItem() == Items.sugar;
            case 1 -> isItemMilk(item);
            default -> false;
        };
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return switch (slot) {
            case 1 -> !isItemMilk(item);
            case 2 -> true;
            default -> false;
        };
    }
}
