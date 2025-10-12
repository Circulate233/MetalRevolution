package com.circulation.metal_revolution.utils;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.util.EnumFacing;

@Desugar
public record BlockPos(int x,int y,int z) {

    public BlockPos offset(EnumFacing facing) {
        return this.offset(facing, 1);
    }

    public BlockPos offset(EnumFacing facing, int n) {
        return n == 0 ? this : new BlockPos(this.x + facing.getFrontOffsetX() * n, this.y + facing.getFrontOffsetY() * n, this.z + facing.getFrontOffsetZ() * n);
    }

    public boolean equals(int x,int y,int z){
        return this.x == x && this.y == y && this.z == z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
