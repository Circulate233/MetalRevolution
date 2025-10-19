package com.circulation.metal_revolution.interfaces;

public interface MRMetalEnergyMachinery {

    int m$getEnergy();

    int m$getMaxEnergy();

    void m$addEnergy(int enetgy);

    default void m$reduceEnergy(int energy) {
        m$addEnergy(-energy);
        if (m$getEnergy() < 0) {
            m$setEnergy(0);
        }
    }

    void m$setEnergy(int energy);

    default int m$getCanInputQuantity() {
        return m$getMaxEnergy() - m$getEnergy();
    }
}
