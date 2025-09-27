package com.circulation.metal_revolution.mixins.MMM.tile;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.items.crafting.ManaFurnaceRecipes;
import project.studio.manametalmod.tileentity.TileEntityCheeseMaker;

@Mixin(TileEntityCheeseMaker.class)
public abstract class MixinTileEntityCheeseMaker extends TileEntity implements ISidedInventory {

    @Unique
    private static final int[] m$AllSlot = {0, 1, 2};

    @Shadow(remap = false)
    public static boolean isItemMilk(ItemStack itemStack) {
        return true;
    }

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$inputValid = new Reference2BooleanFunction[m$AllSlot.length];
    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$outputValid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$inputValid[0] = item -> ManaFurnaceRecipes.smelting().getSmeltingResult((ItemStack) item) != null;
        m$inputValid[1] = item -> isItemMilk((ItemStack) item);
        m$inputValid[2] = item -> false;

        m$outputValid[0] = item -> false;
        m$outputValid[1] = item -> !isItemMilk((ItemStack) item);
        m$outputValid[2] = item -> true;
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
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return m$inputValid[slot].getBoolean(item);
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return m$outputValid[slot].getBoolean(item);
    }
}
