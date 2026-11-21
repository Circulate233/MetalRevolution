package com.circulation.metal_revolution.mixins.mmm.tile;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import project.studio.manametalmod.MMM;
import project.studio.manametalmod.ManaMetalMod;
import project.studio.manametalmod.tileentity.TileEntityManaSFurnace;

@Mixin(TileEntityManaSFurnace.class)
public class MixinManaSFurnace {

    @Unique
    private static final int[] m$AllSlot = { 0, 1, 2, 3, 4 };

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
            case 0 -> MMM.isStringFromArray(MMM.getItemOreDictionaryName(stack), "ingotIron");
            case 1 -> MMM.getManaItem(stack);
            case 4 -> stack.getItem() == ManaMetalMod.MetalEnergy02;
            default -> false;
        };
    }

}
