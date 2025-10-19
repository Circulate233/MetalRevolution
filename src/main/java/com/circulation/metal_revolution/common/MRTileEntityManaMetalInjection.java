package com.circulation.metal_revolution.common;

import net.minecraft.item.ItemStack;
import project.studio.manametalmod.tileentity.TileEntityManaMetalInjection;

public class MRTileEntityManaMetalInjection extends TileEntityManaMetalInjection {

    private static final int[] m$AllSlot = {0};

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item) {
        super.setInventorySlotContents(slot, item);
        if (slot == 0 && item != null) {
            this.setStart();
        }
    }
}
