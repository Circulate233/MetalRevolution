package com.circulation.metal_revolution.mixins.MMM.tile;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.tileentity.TileEntityManaSF;

@Mixin(TileEntityManaSF.class)
public class MixinManaSF {
    @Unique
    private static final int[] m$AllSlot = {0, 1, 2};

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> FurnaceRecipes.smelting().getSmeltingResult((ItemStack) item) != null;
        m$valid[1] = item -> MMM.getManaItem((ItemStack) item);
        m$valid[2] = item -> false;
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

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 2 || stack.getItem() == Items.bucket;
    }
}
