package com.circulation.metal_revolution.mixins.mmm.tile;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import project.studio.manametalmod.MMM;
import project.studio.manametalmod.api.IQualityPearl;
import project.studio.manametalmod.tileentity.TileEntityQualityTable;

@Mixin(TileEntityQualityTable.class)
public class MixinQualityTable {

    @Unique
    private static final int[] m$AllSlot = { 0, 1 };

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
            case 0 -> stack.getItem() instanceof IQualityPearl;
            case 1 -> MMM.getItemIsWeapon(stack) || stack.getItem() instanceof ItemArmor;
            default -> false;
        };
    }
}
