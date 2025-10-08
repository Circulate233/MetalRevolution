package com.circulation.metal_revolution.mixins.MMM.tile;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.api.IEquipmentStrengthenItem;
import project.studio.manametalmod.tileentity.TileEntityItemUseTable;

@Mixin(TileEntityItemUseTable.class)
public class MixinTileEntityItemUseTable {
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
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot == 0 ?
            item.getItem() instanceof IEquipmentStrengthenItem
            : (MMM.getItemIsWeapon(item) || item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemTool || item.getItem() instanceof ItemHoe);
    }
}
