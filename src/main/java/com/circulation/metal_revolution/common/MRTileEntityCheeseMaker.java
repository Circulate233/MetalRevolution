package com.circulation.metal_revolution.common;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.items.crafting.ManaFurnaceRecipes;
import project.studio.manametalmod.tileentity.TileEntityCheeseMaker;

public class MRTileEntityCheeseMaker extends TileEntityCheeseMaker {

    private static final int[] AllSlot = {0, 1, 2};

    public static boolean isItemMilk(ItemStack itemStack) {
        return true;
    }

    private static final Reference2BooleanFunction<ItemStack>[] inputValid = new Reference2BooleanFunction[AllSlot.length];
    private static final Reference2BooleanFunction<ItemStack>[] outputValid = new Reference2BooleanFunction[AllSlot.length];

    static {
        inputValid[0] = item -> ManaFurnaceRecipes.smelting().getSmeltingResult((ItemStack) item) != null;
        inputValid[1] = item -> isItemMilk((ItemStack) item);
        inputValid[2] = item -> false;

        outputValid[0] = item -> false;
        outputValid[1] = item -> !isItemMilk((ItemStack) item);
        outputValid[2] = item -> true;
    }

    public int getSizeInventory() {
        return AllSlot.length;
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        return AllSlot;
    }

    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return inputValid[slot].getBoolean(item);
    }

    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return outputValid[slot].getBoolean(item);
    }
}
