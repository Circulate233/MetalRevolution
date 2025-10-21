package com.circulation.metal_revolution.common;

import com.circulation.metal_revolution.interfaces.MRMetalSeparator;
import com.circulation.metal_revolution.utils.MRUtil;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.ManaMetalMod;
import project.studio.manametalmod.items.crafting.MetalSeparatorRecipes;
import project.studio.manametalmod.tileentity.TileEntityMetalSeparator;

public class MRTileEntityMetalSeparator extends TileEntityMetalSeparator implements MRMetalSeparator {

    private static final int m$maxCacheEnergy = 10000;
    private static final int[] m$AllSlot = {0, 1, 2, 3};
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> MetalSeparatorRecipes.smelting().getSmeltingResult((ItemStack) item) != null;
        m$valid[1] = item -> MMM.getManaItem((ItemStack) item);
        m$valid[2] = item -> false;
        m$valid[3] = item -> false;
    }

    private int m$cacheEnergy = 0;

    public MRTileEntityMetalSeparator() {
        super();
    }

    public MRTileEntityMetalSeparator(int speed) {
        super(speed);
    }

    @Override
    public ItemStack decrStackSize(int slot, int data) {
        if (slot == 2 || slot == 3) m$upOutSlot();
        return super.decrStackSize(slot, data);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 2 || slot == 3) m$upOutSlot();
        return super.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        super.setInventorySlotContents(slot, stack);
        if (slot == 2 || slot == 3) m$upOutSlot();
    }

    @Override
    public int m$getEnergy() {
        return m$cacheEnergy;
    }

    @Override
    public void m$addEnergy(int energy) {
        m$cacheEnergy += energy;
        if (m$cacheEnergy > m$maxCacheEnergy) {
            m$cacheEnergy = m$maxCacheEnergy;
        }
    }

    @Override
    public void m$setEnergy(int energy) {
        m$cacheEnergy = energy;
    }

    @Override
    public int m$getMaxEnergy() {
        return m$maxCacheEnergy;
    }

    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return m$valid[slot].getBoolean(item);
    }

    public boolean canExtractItem(int slot, ItemStack item, int side) {
        boolean i = slot == 2 || slot == 3;
        if (i && m$cacheEnergy >= 9) {
            m$upOutSlot();
        }
        return i;
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = MetalSeparatorRecipes.smelting().getSmeltingResult(this.inventory[0]);
            final int energy = MRUtil.getStackMetalEnergy(itemstack) + 1;

            m$addEnergy(energy);
            m$upOutSlot();

            --this.inventory[0].stackSize;
            if (this.inventory[0].stackSize <= 0) {
                this.inventory[0] = null;
            }
        }
    }

    public boolean canSmelt() {
        if (this.inventory[0] == null) {
            return false;
        } else {
            ItemStack itemstack = MetalSeparatorRecipes.smelting().getSmeltingResult(this.inventory[0]);
            if (itemstack == null) {
                return false;
            } else {
                final int energy = MRUtil.getStackMetalEnergy(itemstack) + 1;
                return m$maxCacheEnergy - m$cacheEnergy >= energy;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        m$cacheEnergy = nbt.getInteger("cacheEnergy");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("cacheEnergy", m$cacheEnergy);
    }

    @Override
    public boolean m$upOutSlot() {
        if (m$cacheEnergy < 9) return false;
        final int need;
        final int max = this.getInventoryStackLimit();
        if (this.inventory[2] == null) {
            need = Math.min(max, m$cacheEnergy / 9);
            this.inventory[2] = new ItemStack(ManaMetalMod.MetalEnergy02, need);
            m$cacheEnergy -= need * 9;
            return true;
        } else if (this.inventory[2].stackSize < max) {
            need = Math.min(max - this.inventory[2].stackSize, m$cacheEnergy / 9);
            this.inventory[2].stackSize += need;
            m$cacheEnergy -= need * 9;
            return true;
        }
        return false;
    }

    @Override
    public boolean m$upSmallOutSlot() {
        if (m$cacheEnergy < 1) return false;
        final int need;
        final int max = this.getInventoryStackLimit();
        if (this.inventory[3] == null) {
            need = Math.min(max, m$cacheEnergy);
            this.inventory[3] = new ItemStack(ManaMetalMod.MetalEnergy01, need);
            m$cacheEnergy -= need;
            return true;
        } else if (this.inventory[3].stackSize < max) {
            need = Math.min(max - this.inventory[3].stackSize, m$cacheEnergy);
            this.inventory[3].stackSize += need;
            m$cacheEnergy -= need;
            return true;
        }
        return false;
    }

    @Override
    public int m$getCache(int data) {
        return m$cacheEnergy * data / m$maxCacheEnergy;
    }
}
