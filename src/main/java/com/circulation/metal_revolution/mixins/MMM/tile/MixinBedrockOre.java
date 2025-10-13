package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.tileentity.TileEntityBedrockOre;

import java.util.stream.IntStream;

@Mixin(TileEntityBedrockOre.class)
public class MixinBedrockOre {

    @Unique
    private static final int[] m$AllSlot = IntStream.range(0, 40).toArray();

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
        return slot < 20 && FurnaceRecipes.smelting().getSmeltingResult(stack) != null;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot > 19;
    }
}
