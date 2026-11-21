package com.circulation.metal_revolution.mixins.mmm.tile;

import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.circulation.metal_revolution.utils.ItemStackRecipeLookup;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import project.studio.manametalmod.items.crafting.AlloyFurnaceRecipes;
import project.studio.manametalmod.tileentity.TileEntityAlloyFurnace;

@Mixin(TileEntityAlloyFurnace.class)
public abstract class MixinAlloyFurnace {

    @Unique
    private static final int[] m$AllSlot = { 0, 1, 2, 3, 4 };
    @Shadow(remap = false)
    private ItemStack[] inventory;
    @Unique
    private Set<ItemStack> m$imp;
    @Unique
    private Object2ObjectMap<ItemStack, Set<ItemStack>> m$rimp1;
    @Unique
    private Object2ObjectMap<ItemStack, Set<ItemStack>> m$rimp2;

    @Shadow(remap = false)
    public static boolean isItemFuel(ItemStack item) {
        return false;
    }

    @Unique
    private void m$initRecipe() {
        Set<ItemStack> imp = new ObjectOpenCustomHashSet<>(ItemStackRecipeLookup.STRATEGY);
        Object2ObjectMap<ItemStack, Set<ItemStack>> rimp1 = new Object2ObjectOpenCustomHashMap<>(
            ItemStackRecipeLookup.STRATEGY);
        Object2ObjectMap<ItemStack, Set<ItemStack>> rimp2 = new Object2ObjectOpenCustomHashMap<>(
            ItemStackRecipeLookup.STRATEGY);
        for (ItemStack[] recipe : AlloyFurnaceRecipes.smelting().list) {
            ItemStack first = recipe[0] == null || recipe[0].getItem() == null ? null : recipe[0].copy();
            ItemStack second = recipe[1] == null || recipe[1].getItem() == null ? null : recipe[1].copy();

            imp.add(first);
            imp.add(second);

            rimp1.computeIfAbsent(first, stack -> new ObjectOpenCustomHashSet<>(ItemStackRecipeLookup.STRATEGY))
                .add(second);
            rimp2.computeIfAbsent(second, stack -> new ObjectOpenCustomHashSet<>(ItemStackRecipeLookup.STRATEGY))
                .add(first);
        }
        m$imp = imp;
        m$rimp1 = rimp1;
        m$rimp2 = rimp2;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (stack == null) return false;
        return switch (slot) {
            case 0 -> m$isItemRecipe1(stack);
            case 1 -> isItemFuel(stack);
            case 4 -> m$isItemRecipe2(stack);
            default -> false;
        };
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 2 || slot == 3 || stack.getItem() == Items.bucket;
    }

    @Unique
    public boolean m$isItemRecipe1(@Nonnull ItemStack item) {
        if (this.inventory == null) return false;
        if (item != null && item.getItem() == null) item = null;
        if (this.inventory[4] != null) {
            if (m$rimp2 == null) {
                m$initRecipe();
            }
            ItemStack other = this.inventory[4];
            Set<ItemStack> ingredients = m$rimp2.get(other.getItem() == null ? null : other);
            return ingredients != null && ingredients.contains(item);
        }
        if (m$imp == null) {
            m$initRecipe();
        }
        return m$imp.contains(item);
    }

    @Unique
    public boolean m$isItemRecipe2(@Nonnull ItemStack item) {
        if (this.inventory == null) return false;
        if (item != null && item.getItem() == null) item = null;
        if (this.inventory[0] != null) {
            if (m$rimp1 == null) {
                m$initRecipe();
            }
            ItemStack other = this.inventory[0];
            Set<ItemStack> ingredients = m$rimp1.get(other.getItem() == null ? null : other);
            return ingredients != null && ingredients.contains(item);
        }
        if (m$imp == null) {
            m$initRecipe();
        }
        return m$imp.contains(item);
    }
}
