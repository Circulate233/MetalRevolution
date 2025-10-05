package com.circulation.metal_revolution.iinterface;

public interface MRMetalSeparator {

    int m$getEnergy();

    void m$setEnergy(int energy);

    int m$getMaxEnergy();

    boolean m$upOutSlot();

    boolean m$upSmallOutSlot();

    int m$getCache(int data);
}
