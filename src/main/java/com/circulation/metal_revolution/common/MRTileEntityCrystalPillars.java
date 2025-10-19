package com.circulation.metal_revolution.common;

import net.minecraft.item.ItemStack;
import project.studio.manametalmod.tileentity.TileEntityCrystalPillars;

public class MRTileEntityCrystalPillars extends TileEntityCrystalPillars {

    private static final int[] allSlot = {0};

    public int[] getAccessibleSlotsFromSide(int side) {
        return allSlot;
    }

    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        var out = super.decrStackSize(p_70298_1_, p_70298_2_);
        if (out != null) this.closeInventory();
        return out;
    }

    public void setInventorySlotContents(int slot, ItemStack item) {
        super.setInventorySlotContents(slot, item);
        if (item != null) {
            this.openInventory();
        } else {
            this.closeInventory();
        }
    }

    public ItemStack getStackInSlotOnClosing(int slot) {
        var out = super.getStackInSlotOnClosing(slot);
        if (out != null) this.closeInventory();
        return out;
    }
}
