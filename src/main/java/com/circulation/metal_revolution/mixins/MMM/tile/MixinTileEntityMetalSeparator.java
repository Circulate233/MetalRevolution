package com.circulation.metal_revolution.mixins.MMM.tile;

import com.circulation.metal_revolution.iinterface.MRMetalSeparator;
import com.circulation.metal_revolution.utils.MRUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.ManaMetalMod;
import project.studio.manametalmod.items.crafting.MetalSeparatorRecipes;
import project.studio.manametalmod.tileentity.TileEntityMetalSeparator;

@Mixin(TileEntityMetalSeparator.class)
public abstract class MixinTileEntityMetalSeparator extends TileEntity implements ISidedInventory,MRMetalSeparator {

    @Unique
    private int m$cacheEnergy;

    @Unique
    private final int m$maxCacheEnergy = 10000;

    @Unique
    @Override
    public int m$getEnergy() {
        return m$cacheEnergy;
    }

    @Unique
    @Override
    public void m$setEnergy(int energy) {
        m$cacheEnergy = energy;
    }

    @Unique
    @Override
    public int m$getMaxEnergy() {
        return m$maxCacheEnergy;
    }

    @Shadow(remap = false)
    public ItemStack[] inventory;

    @Unique
    private static final int[] m$AllSlot = {0, 1, 2, 3};

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    static {
        m$valid[0] = item -> MetalSeparatorRecipes.smelting().getSmeltingResult((ItemStack) item) != null;
        m$valid[1] = item -> MMM.getManaItem((ItemStack) item);
        m$valid[2] = item -> false;
        m$valid[3] = item -> false;
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
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        boolean i = slot == 2 || slot == 3;
        if (i && m$cacheEnergy >= 9) {
            m$upOutSlot();
        }
        return i;
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
    @Overwrite(remap = false)
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = MetalSeparatorRecipes.smelting().getSmeltingResult(this.inventory[0]);
            final int energy = MRUtil.getStackMetalEnergy(itemstack) + 1;

            m$cacheEnergy += energy;
            m$upOutSlot();

            --this.inventory[0].stackSize;
            if (this.inventory[0].stackSize <= 0) {
                this.inventory[0] = null;
            }
        }

    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite(remap = false)
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

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    public void readFromNBT(NBTTagCompound nbt, CallbackInfo ci) {
        m$cacheEnergy = nbt.getInteger("cacheEnergy");
    }

    @Inject(method = "writeToNBT", at = @At("TAIL"))
    public void writeToNBT(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setInteger("cacheEnergy", m$cacheEnergy);
    }

    @Unique
    @Override
    public boolean m$upOutSlot() {
        if (m$cacheEnergy < 9) return false;
        int need;
        int max = this.getInventoryStackLimit();
        if (this.inventory[2] == null) {
            need = Math.min(64, m$cacheEnergy / 9);
            this.inventory[2] = new ItemStack(ManaMetalMod.MetalEnergy02, need);
            m$cacheEnergy -= need * 9;
            return true;
        } else if (this.inventory[2].stackSize < 64) {
            need = Math.min(64 - this.inventory[2].stackSize, m$cacheEnergy / 9);
            this.inventory[2].stackSize += need;
            m$cacheEnergy -= need * 9;
            return true;
        }
        return false;
    }

    @Unique
    @Override
    public boolean m$upSmallOutSlot() {
        if (m$cacheEnergy < 1) return false;
        int need;
        int max = this.getInventoryStackLimit();
        if (this.inventory[3] == null) {
            need = Math.min(64, m$cacheEnergy);
            this.inventory[3] = new ItemStack(ManaMetalMod.MetalEnergy01, need);
            m$cacheEnergy -= need;
            return true;
        } else if (this.inventory[3].stackSize < 64) {
            need = Math.min(64 - this.inventory[3].stackSize, m$cacheEnergy);
            this.inventory[3].stackSize += need;
            m$cacheEnergy -= need;
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Unique
    @Override
    public int m$getCache(int data) {
        return m$cacheEnergy * data / m$maxCacheEnergy;
    }
}
