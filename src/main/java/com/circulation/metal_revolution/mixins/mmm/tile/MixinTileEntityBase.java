package com.circulation.metal_revolution.mixins.mmm.tile;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import project.studio.manametalmod.core.RecipeOre;
import project.studio.manametalmod.tileentity.TileEntityBase;

@Mixin(value = TileEntityBase.class, remap = false)
public abstract class MixinTileEntityBase extends TileEntity implements ISidedInventory {

    @Unique
    private static final int[] m$ALL_SLOTS = { 0, 1, 2, 3 };
    @Shadow
    public List<RecipeOre> recipe;
    @Shadow
    public ItemStack[] items;

    @Shadow
    public abstract boolean isOKFuel(ItemStack item);

    /**
     * @author circulation
     * @reason 开放全部槽位给自动化访问
     */
    @Overwrite(remap = false)
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$ALL_SLOTS;
    }

    /**
     * @author circulation
     * @reason 手动放入判定
     */
    @Overwrite(remap = false)
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null) return false;

        return switch (slot) {
            case 0 -> this.m$isItemRecipe1Loose(stack);
            case 2 ->
                // 手动时仍允许燃料进入 2 槽，避免“完全放不进去”
                this.isOKFuel(stack);
            case 3 -> this.m$isItemRecipe2Loose(stack);
            default -> false;
        };
    }

    /**
     * @author circulation
     * @reason 自动化插入判定：优先配方槽，只有纯燃料才进 2 槽
     */
    @Overwrite(remap = false)
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (stack == null) return false;

        return switch (slot) {
            case 0 -> this.m$isItemRecipe1Loose(stack);
            case 2 -> this.isOKFuel(stack) && !this.m$isItemRecipe1Loose(stack) && !this.m$isItemRecipe2Loose(stack);
            case 3 -> this.m$isItemRecipe2Loose(stack);
            default -> false;
        };
    }

    @Unique
    private boolean m$stackMatchesNoNBT(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a.getItem() != b.getItem()) return false;

        int da = a.getItemDamage();
        int db = b.getItemDamage();

        return da == db || da == 32767 || db == 32767;
    }

    @Unique
    private boolean m$containsMatchingStack(List<ItemStack> stacks, ItemStack item) {
        if (stacks == null) return false;
        for (ItemStack stack : stacks) {
            if (m$stackMatchesNoNBT(stack, item)) return true;
        }
        return false;
    }

    @Unique
    public boolean m$isItemRecipe1Loose(@Nonnull ItemStack item) {
        return m$isItemRecipeLoose(item, true);
    }

    @Unique
    public boolean m$isItemRecipe2Loose(@Nonnull ItemStack item) {
        return m$isItemRecipeLoose(item, false);
    }

    @Unique
    private boolean m$isItemRecipeLoose(ItemStack item, boolean firstInput) {
        if (item == null || this.items == null || this.recipe == null) {
            return false;
        }

        ItemStack other = this.items[firstInput ? 3 : 0];
        for (RecipeOre recipeOre : this.recipe) {
            List<ItemStack> candidates = (firstInput ? recipeOre.getImp1() : recipeOre.getImp2()).getItem();
            if (!m$containsMatchingStack(candidates, item)) continue;
            if (other == null) return true;

            List<ItemStack> partners = (firstInput ? recipeOre.getImp2() : recipeOre.getImp1()).getItem();
            if (m$containsMatchingStack(partners, other)) return true;
        }

        return false;
    }
}
