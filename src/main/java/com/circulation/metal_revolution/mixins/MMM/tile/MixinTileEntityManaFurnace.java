package com.circulation.metal_revolution.mixins.MMM.tile;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.items.crafting.ManaFurnaceRecipes;
import project.studio.manametalmod.magicenergy.IMagicEnergyUse;
import project.studio.manametalmod.tileentity.TileEntityManaFurnace;

@Mixin(value = TileEntityManaFurnace.class,remap = false)
public abstract class MixinTileEntityManaFurnace extends TileEntity implements ISidedInventory, IMagicEnergyUse {

    @Unique
    private static final int[] m$AllSlot = {0, 1, 2};

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> ManaFurnaceRecipes.smelting().getSmeltingResult((ItemStack) item) != null;
        m$valid[1] = item -> MMM.getManaItem((ItemStack) item);
        m$valid[2] = item -> false;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite(remap = true)
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite(remap = true)
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return m$valid[slot].getBoolean(stack);
    }

}
