package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.tileentity.TileEntityManaGravityWell;

import java.util.stream.IntStream;

@Mixin(TileEntityManaGravityWell.class)
public class MixinManaGravityWell {

    @Unique
    private static final int[] m$AllSlot = IntStream.range(0, 13).toArray();

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
        return slot != 12;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 12;
    }
}
