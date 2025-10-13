package com.circulation.metal_revolution.common;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.tileentity.TileEntityCheeseMaker;

public class MRTileEntityCheeseMaker extends TileEntityCheeseMaker {

    private static final int[] AllSlot = {0, 1, 2};

    private static final Reference2BooleanFunction<ItemStack>[] inputValid = new Reference2BooleanFunction[AllSlot.length];
    private static final Reference2BooleanFunction<ItemStack>[] outputValid = new Reference2BooleanFunction[AllSlot.length];

    static {
        inputValid[0] = item -> item instanceof ItemStack s && s.getItem() == Items.sugar;
        inputValid[1] = item -> isItemMilk((ItemStack) item);
        inputValid[2] = item -> false;

        outputValid[0] = item -> false;
        outputValid[1] = item -> !isItemMilk((ItemStack) item);
        outputValid[2] = item -> true;
    }

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
        return inputValid[slot].getBoolean(item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return outputValid[slot].getBoolean(item);
    }
}
