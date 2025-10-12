package com.circulation.metal_revolution.mixins.MMM.tile;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.items.crafting.ManaFuelMakeRecipes;
import project.studio.manametalmod.tileentity.TileEntityManaFuelMake;

@Mixin(TileEntityManaFuelMake.class)
public class MixinManaFuelMake {
    @Unique
    private static final int[] m$AllSlot = {0, 1, 2, 3};

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> ManaFuelMakeRecipes.smelting().getSmeltingExtraitem((ItemStack) item) != null;
        m$valid[1] = item -> MMM.getManaItem((ItemStack) item);
        m$valid[2] = item -> false;
        m$valid[3] = item -> false;
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
