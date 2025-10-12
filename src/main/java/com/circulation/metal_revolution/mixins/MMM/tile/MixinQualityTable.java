package com.circulation.metal_revolution.mixins.MMM.tile;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
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
    private static final int[] m$AllSlot = {0, 1};

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> ((ItemStack) item).getItem() instanceof IQualityPearl;
        m$valid[1] = item -> MMM.getItemIsWeapon((ItemStack) item) || ((ItemStack) item).getItem() instanceof ItemArmor;
    }

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
        return m$valid[slot].getBoolean(stack);
    }
}
