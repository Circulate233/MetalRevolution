package com.circulation.metal_revolution.mixins.MMM.tile;

import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.api.IWeaponScroll;
import project.studio.manametalmod.rpg.TileEntityWeaponScroll;

import static project.studio.manametalmod.itemAndBlockCraft.ItemCraft10.StrengthenStone;

@Mixin(TileEntityWeaponScroll.class)
public class MixinTileEntityWeaponScroll {
    @Unique
    private static final int[] m$AllSlot = {0, 1, 2};

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> ((ItemStack) item).getItem() instanceof IWeaponScroll;
        m$valid[1] = obj -> obj instanceof ItemStack item && MMM.getItemIsWeapon(item) && item.hasTagCompound() && item.getTagCompound().hasKey("weapon_strengthen", 10);
        m$valid[2] = item -> ((ItemStack) item).getItem() == StrengthenStone;
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
