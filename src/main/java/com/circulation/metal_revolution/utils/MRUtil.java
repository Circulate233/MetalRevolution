package com.circulation.metal_revolution.utils;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MRUtil {

    public static final Reference2IntMap<Item> metalMap = new Reference2IntOpenHashMap<>();

    public static int getStackMetalEnergy(ItemStack stack) {
        if (stack == null) return 0;
        return metalMap.getInt(stack.getItem()) * stack.stackSize;
    }
}
