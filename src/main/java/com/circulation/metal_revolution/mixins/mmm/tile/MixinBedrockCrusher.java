package com.circulation.metal_revolution.mixins.mmm.tile;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import project.studio.manametalmod.MMM;
import project.studio.manametalmod.items.crafting.BedrockCrusherRecipes;
import project.studio.manametalmod.tileentity.TileEntityBedrockCrusher;

@Mixin(TileEntityBedrockCrusher.class)
public class MixinBedrockCrusher {

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
            case 0 -> BedrockCrusherRecipes.smelting()
                .getSmeltingResult(stack) != null;
            case 1 -> MMM.getItemStackFuelValue(stack) > 0;
            default -> false;
        };
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 2 || stack.getItem() == Items.bucket;
    }
}
