package com.circulation.metal_revolution.mixins.MMM.tile;

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
    private String m$debugStack(ItemStack stack) {
        if (stack == null) return "null";
        return stack.getItem() + " meta="
            + stack.getItemDamage()
            + " size="
            + stack.stackSize
            + " nbt="
            + stack.getTagCompound();
    }

    @Unique
    public boolean m$isItemRecipe1Loose(@Nonnull ItemStack item) {
        if (item == null || this.items == null || this.recipe == null) {
            return false;
        }

        for (RecipeOre recipeOre : this.recipe) {
            List<ItemStack> list1 = recipeOre.getImp1()
                .getItem();
            List<ItemStack> list2 = recipeOre.getImp2()
                .getItem();

            boolean matchSelf = false;
            if (list1 != null) {
                for (ItemStack s1 : list1) {
                    if (m$stackMatchesNoNBT(s1, item)) {
                        matchSelf = true;
                        break;
                    }
                }
            }

            if (!matchSelf) continue;

            // 如果 3 槽已有物品，则要求当前物品能与其配对
            if (this.items[3] != null) {
                if (list2 != null) {
                    for (ItemStack s2 : list2) {
                        if (m$stackMatchesNoNBT(s2, this.items[3])) {
                            return true;
                        }
                    }
                }
            } else {
                return true;
            }
        }

        return false;
    }

    @Unique
    public boolean m$isItemRecipe2Loose(@Nonnull ItemStack item) {
        if (item == null || this.items == null || this.recipe == null) {
            return false;
        }

        for (RecipeOre recipeOre : this.recipe) {
            List<ItemStack> list1 = recipeOre.getImp1()
                .getItem();
            List<ItemStack> list2 = recipeOre.getImp2()
                .getItem();

            boolean matchSelf = false;
            if (list2 != null) {
                for (ItemStack s2 : list2) {
                    if (m$stackMatchesNoNBT(s2, item)) {
                        matchSelf = true;
                        break;
                    }
                }
            }

            if (!matchSelf) continue;
            if (this.items[0] != null) {
                if (list1 != null) {
                    for (ItemStack s1 : list1) {
                        if (m$stackMatchesNoNBT(s1, this.items[0])) {
                            return true;
                        }
                    }
                }
            } else {
                return true;
            }
        }

        return false;
    }
}
