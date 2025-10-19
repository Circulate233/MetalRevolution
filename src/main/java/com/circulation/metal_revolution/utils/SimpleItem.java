package com.circulation.metal_revolution.utils;

import com.github.bsideup.jabel.Desugar;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Desugar
public record SimpleItem(Item item, int meta, NBTTagCompound nbt) {

    public static final SimpleItem empty = new SimpleItem(null, 0, null);
    private static final NBTTagCompound NullNbt = new NBTTagCompound() {
        @Override
        public boolean equals(Object nbt) {
            return nbt == this;
        }

        @Override
        public int hashCode() {
            return Integer.MIN_VALUE;
        }
    };
    private static final Reference2ObjectMap<Item, Int2ObjectMap<Map<NBTTagCompound, SimpleItem>>> chane = Reference2ObjectMaps.synchronize(new Reference2ObjectOpenHashMap<>());
    private static final Reference2ObjectFunction<Item, Int2ObjectMap<Map<NBTTagCompound, SimpleItem>>> intMap = i -> Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());
    private static final Int2ObjectFunction<Map<NBTTagCompound, SimpleItem>> itemMap = i -> new ConcurrentHashMap<>();
    private SimpleItem(ItemStack stack) {
        this(stack.getItem(), stack.getItemDamage(), stack.getTagCompound());
    }

    public static SimpleItem getInstance(final ItemStack stack) {
        if (stack == null) return empty;
        if (stack.getItem() == null) return empty;
        var nbt = stack.getTagCompound();
        return chane.computeIfAbsent(stack.getItem(), intMap)
            .computeIfAbsent(stack.getItemDamage(), itemMap)
            .computeIfAbsent(nbt == null ? NullNbt : nbt, n -> new SimpleItem(stack));
    }

    public static SimpleItem getNoNBTInstance(final ItemStack stack) {
        if (stack == null) return empty;
        if (stack.getItem() == null) return empty;
        return chane.computeIfAbsent(stack.getItem(), intMap)
            .computeIfAbsent(stack.getItemDamage(), itemMap)
            .computeIfAbsent(NullNbt, n -> new SimpleItem(stack));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SimpleItem that = (SimpleItem) o;
        return meta == that.meta && Objects.equals(item, that.item) && Objects.equals(nbt, that.nbt);
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + meta;
        result = 31 * result + (nbt != null ? nbt.hashCode() : 0);
        return result;
    }

}
