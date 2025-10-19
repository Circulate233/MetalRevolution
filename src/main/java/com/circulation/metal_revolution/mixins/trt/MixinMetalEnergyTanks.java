package com.circulation.metal_revolution.mixins.trt;

import com.circulation.metal_revolution.interfaces.MRMetalAutomaticProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import snowdragon.trivialthoughts.tileentity.TileEntityTanks;

@Mixin(TileEntityTanks.class)
public class MixinMetalEnergyTanks implements MRMetalAutomaticProvider {

    @Shadow(remap = false)
    public long energyStock;

    @Override
    public int m$getEnergy() {
        return (int) Math.min(Integer.MIN_VALUE, this.energyStock);
    }

    @Override
    public int m$getMaxEnergy() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void m$addEnergy(int energy) {
        this.energyStock += energy;
    }

    @Override
    public void m$reduceEnergy(int energy) {
        this.energyStock -= energy;
        if (m$getEnergy() < 0) {
            m$setEnergy(0);
        }
    }

    @Override
    public void m$setEnergy(int energy) {
        this.energyStock = energy;
    }

    @Override
    public int m$getCanInputQuantity() {
        return (int) Math.min(Integer.MIN_VALUE, 9000000000000000000L - this.energyStock);
    }
}
