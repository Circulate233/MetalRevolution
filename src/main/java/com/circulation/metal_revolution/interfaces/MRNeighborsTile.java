package com.circulation.metal_revolution.interfaces;

import com.circulation.metal_revolution.utils.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public interface MRNeighborsTile {
    default void m$updateNeighbors(){
        for (var facing : EnumFacing.values()) {
            m$updateNeighbors(facing);
        }
    }

    void m$updateNeighbors(EnumFacing facing);

    void m$updateNeighbors(IBlockAccess world, BlockPos pos, BlockPos neighbor);

    BlockPos m$getPos();
}
