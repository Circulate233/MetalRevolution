package com.circulation.metal_revolution.mixins.mmm.tile;

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

@Mixin(value = TileEntityManaFurnace.class, remap = false)
public abstract class MixinManaFurnace extends TileEntity implements ISidedInventory, IMagicEnergyUse {

    @Unique
    private static final int[] m$AllSlot = { 0, 1, 2 };

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
        if (stack == null) return false;
        return switch (slot) {
            case 0 -> ManaFurnaceRecipes.smelting()
                .getSmeltingResult(stack) != null;
            case 1 -> MMM.getManaItem(stack);
            default -> false;
        };
    }

}
