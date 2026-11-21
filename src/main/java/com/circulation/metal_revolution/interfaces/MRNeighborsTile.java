package com.circulation.metal_revolution.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import com.circulation.metal_revolution.utils.BlockPos;

public interface MRNeighborsTile {

    static EnumFacing m$getNeighborFacing(BlockPos pos, BlockPos neighbor) {
        if (pos.x() != neighbor.x()) {
            return pos.x() > neighbor.x() ? EnumFacing.WEST : EnumFacing.EAST;
        }
        if (pos.y() != neighbor.y()) {
            return pos.y() > neighbor.y() ? EnumFacing.DOWN : EnumFacing.UP;
        }
        if (pos.z() != neighbor.z()) {
            return pos.z() > neighbor.z() ? EnumFacing.NORTH : EnumFacing.SOUTH;
        }
        return null;
    }

    default void m$updateNeighbors() {
        for (var facing : EnumFacing.values()) {
            m$updateNeighbors(facing);
        }
    }

    void m$updateNeighbors(EnumFacing facing);

    void m$updateNeighbors(IBlockAccess world, BlockPos pos, BlockPos neighbor);

    BlockPos m$getPos();
}
