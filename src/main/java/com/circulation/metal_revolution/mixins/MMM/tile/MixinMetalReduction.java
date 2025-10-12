package com.circulation.metal_revolution.mixins.MMM.tile;

import com.circulation.metal_revolution.interfaces.MRMetalAutomaticProvider;
import com.circulation.metal_revolution.interfaces.MRMetalEnergyMachinery;
import com.circulation.metal_revolution.interfaces.MRNeighborsTile;
import com.circulation.metal_revolution.utils.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.api.IMetalEnergy;
import project.studio.manametalmod.tileentity.TileEntityMetalReduction;

import java.util.EnumMap;

@Mixin(TileEntityMetalReduction.class)
public class MixinMetalReduction extends TileEntity implements MRMetalEnergyMachinery, MRNeighborsTile {

    @Shadow(remap = false)
    public int metal;

    @Unique
    private boolean m$init;

    @Unique
    private BlockPos m$pos;

    @Unique
    private final EnumMap<EnumFacing, MRMetalAutomaticProvider> m$neighbors = new EnumMap<>(EnumFacing.class);

    @Override
    public void m$updateNeighbors(EnumFacing facing) {
        var pos = m$getPos().offset(facing);
        if (this.worldObj.getTileEntity(pos.x(), pos.y(), pos.z()) instanceof MRMetalAutomaticProvider m) {
            m$neighbors.put(facing, m);
        } else {
            m$neighbors.remove(facing);
        }
    }

    @Override
    public void m$updateNeighbors(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        EnumFacing updateFromFacing;
        if (pos.getX() != neighbor.getX()) {
            if (pos.getX() > neighbor.getX()) {
                updateFromFacing = EnumFacing.WEST;
            } else {
                updateFromFacing = EnumFacing.EAST;
            }
        } else if (pos.getY() != neighbor.getY()) {
            if (pos.getY() > neighbor.getY()) {
                updateFromFacing = EnumFacing.DOWN;
            } else {
                updateFromFacing = EnumFacing.UP;
            }
        } else {
            if (pos.getZ() == neighbor.getZ()) {
                return;
            }

            if (pos.getZ() > neighbor.getZ()) {
                updateFromFacing = EnumFacing.NORTH;
            } else {
                updateFromFacing = EnumFacing.SOUTH;
            }
        }

        if (this.worldObj.getTileEntity(neighbor.x(), neighbor.y(), neighbor.z()) instanceof MRMetalAutomaticProvider m) {
            m$neighbors.put(updateFromFacing, m);
        } else {
            m$neighbors.remove(updateFromFacing);
        }
    }

    @Inject(method = "updateEntity", at = @At("HEAD"))
    public void onUp(CallbackInfo ci) {
        if (worldObj.isRemote) return;
        if (!m$init) {
            m$updateNeighbors();
            m$init = true;
        }
        if (worldObj.getTotalWorldTime() % 20 != 0) return;
        var q = m$getCanInputQuantity();
        if (q > 0) {
            for (var tile : m$neighbors.values()) {
                final var energy = tile.m$getEnergy();
                if (energy > 0) {
                    if (energy < q) {
                        tile.m$setEnergy(0);
                        q -= energy;
                        this.m$addEnergy(energy);
                    } else {
                        tile.m$reduceEnergy(q);
                        this.m$setEnergy(this.m$getMaxEnergy());
                        break;
                    }
                }
            }
        }
    }

    public BlockPos m$getPos() {
        if (m$pos == null || !m$pos.equals(this.xCoord, this.yCoord, this.zCoord)) {
            m$pos = new BlockPos(this.xCoord, this.yCoord, this.zCoord);
        }
        return m$pos;
    }

    @Unique
    private static final int m$maxCacheEnergy = 1000;

    @Override
    public int m$getEnergy() {
        return this.metal;
    }

    @Override
    public int m$getMaxEnergy() {
        return m$maxCacheEnergy;
    }

    @Override
    public void m$addEnergy(int enetgy) {
        this.metal += enetgy;
        if (this.metal > m$maxCacheEnergy) {
            this.metal = m$maxCacheEnergy;
        }
    }

    @Override
    public void m$setEnergy(int energy) {
        this.metal = energy;
    }

    @Unique
    private static final int[] m$AllSlot = {9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 25, 26, 27};

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
        return slot < 18 && stack != null && stack.getItem() instanceof IMetalEnergy;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int par3) {
        return slot > 18;
    }
}
