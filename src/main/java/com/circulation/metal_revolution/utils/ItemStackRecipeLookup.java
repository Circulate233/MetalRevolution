package com.circulation.metal_revolution.utils;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public final class ItemStackRecipeLookup {

    public static final int MISSING_INT = Integer.MIN_VALUE;
    /**
     * Quantity and NBT do not participate in these recipes; wildcard matching belongs in the lookup, not equality.
     */
    public static final Hash.Strategy<ItemStack> STRATEGY = new Hash.Strategy<>() {

        @Override
        public int hashCode(ItemStack stack) {
            return stack == null ? 0 : 31 * System.identityHashCode(stack.getItem()) + stack.getItemDamage();
        }

        @Override
        public boolean equals(ItemStack first, ItemStack second) {
            return first == second || first != null && second != null
                && first.getItem() == second.getItem()
                && first.getItemDamage() == second.getItemDamage();
        }
    };
    /**
     * Fuel handlers may distinguish tags, but stack counts still do not participate in the cache key.
     */
    public static final Hash.Strategy<ItemStack> NBT_STRATEGY = new Hash.Strategy<>() {

        @Override
        public int hashCode(ItemStack stack) {
            return stack == null ? 0 : 31 * STRATEGY.hashCode(stack) + Objects.hashCode(stack.getTagCompound());
        }

        @Override
        public boolean equals(ItemStack first, ItemStack second) {
            return STRATEGY.equals(first, second)
                && (first == null || Objects.equals(first.getTagCompound(), second.getTagCompound()));
        }
    };
    private static final float MISSING_FLOAT = Float.NaN;

    private ItemStackRecipeLookup() {}

    /**
     * Exact metadata takes precedence. Recipe keys must keep their item and metadata stable while registered.
     */
    public static <V> V findObject(Object2ObjectMap<ItemStack, V> recipes, ItemStack query) {
        if (query == null || query.getItem() == null) return null;
        int metadata = query.getItemDamage();
        V result = recipes.get(query);
        if (result != null || recipes.containsKey(query) || metadata == OreDictionary.WILDCARD_VALUE) {
            return result;
        }
        try {
            query.setItemDamage(OreDictionary.WILDCARD_VALUE);
            return recipes.get(query);
        } finally {
            query.setItemDamage(metadata);
        }
    }

    public static float findFloat(Object2FloatMap<ItemStack> recipes, ItemStack query) {
        if (query == null || query.getItem() == null) return 0.0f;
        int metadata = query.getItemDamage();
        float result = recipes.getOrDefault(query, MISSING_FLOAT);
        if (!Float.isNaN(result) || recipes.containsKey(query)) return result;
        if (metadata == OreDictionary.WILDCARD_VALUE) return 0.0f;
        try {
            query.setItemDamage(OreDictionary.WILDCARD_VALUE);
            return recipes.getOrDefault(query, 0.0f);
        } finally {
            query.setItemDamage(metadata);
        }
    }

    public static int findInt(Object2IntMap<ItemStack> recipes, ItemStack query) {
        if (query == null || query.getItem() == null) return MISSING_INT;
        int metadata = query.getItemDamage();
        int result = recipes.getOrDefault(query, MISSING_INT);
        if (result != MISSING_INT || recipes.containsKey(query) || metadata == OreDictionary.WILDCARD_VALUE) {
            return result;
        }
        try {
            query.setItemDamage(OreDictionary.WILDCARD_VALUE);
            return recipes.getOrDefault(query, MISSING_INT);
        } finally {
            query.setItemDamage(metadata);
        }
    }

    /**
     * Distinguishes a stored sentinel-valued chance from a missing recipe, preserving its random draw.
     */
    public static boolean containsInt(Object2IntMap<ItemStack> recipes, ItemStack query) {
        if (query == null || query.getItem() == null) return false;
        if (recipes.containsKey(query)) return true;
        int metadata = query.getItemDamage();
        if (metadata == OreDictionary.WILDCARD_VALUE) return false;
        try {
            query.setItemDamage(OreDictionary.WILDCARD_VALUE);
            return recipes.containsKey(query);
        } finally {
            query.setItemDamage(metadata);
        }
    }
}
