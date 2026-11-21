package com.circulation.metal_revolution.mixins.mmm.tile;

import static project.studio.manametalmod.itemAndBlockCraft.ItemCraft10.StrengthenStone;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import project.studio.manametalmod.MMM;
import project.studio.manametalmod.api.IWeaponScroll;
import project.studio.manametalmod.rpg.TileEntityWeaponScroll;

@Mixin(TileEntityWeaponScroll.class)
public class MixinWeaponScroll {

    @Unique
    private static final int[] m$AllSlot = { 0, 1, 2 };

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
            case 0 -> stack.getItem() instanceof IWeaponScroll;
            case 1 -> MMM.getItemIsWeapon(stack) && stack.hasTagCompound()
                && stack.getTagCompound()
                    .hasKey("weapon_strengthen", 10);
            case 2 -> stack.getItem() == StrengthenStone;
            default -> false;
        };
    }
}
