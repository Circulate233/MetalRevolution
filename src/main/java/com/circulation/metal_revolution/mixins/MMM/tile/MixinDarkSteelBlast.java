package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.dark_magic.DarkSteelRecipes;
import project.studio.manametalmod.dark_magic.TileEntityDarkSteelBlast;

@Mixin(TileEntityDarkSteelBlast.class)
public class MixinDarkSteelBlast {

    @Unique
    private static final int[] m$AllSlot = {0, 1};

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
        return slot == 0 && DarkSteelRecipes.smelting().getSmeltingResult(stack) != null;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 1;
    }
}
