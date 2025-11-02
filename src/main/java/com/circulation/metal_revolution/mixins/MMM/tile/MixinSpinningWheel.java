package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.produce.textile.TileEntitySpinningWheel;

import java.util.stream.IntStream;

@Mixin(TileEntitySpinningWheel.class)
public class MixinSpinningWheel {
    @Unique
    private static final int[] m$AllSlot = IntStream.range(0, 6).toArray();

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
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot != 5;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return slot == 5;
    }
}
