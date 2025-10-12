package com.circulation.metal_revolution.mixins.MMM.block;

import com.circulation.metal_revolution.interfaces.MRNeighborsTile;
import com.circulation.metal_revolution.utils.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import project.studio.manametalmod.blocks.BlockTileEntityMetalCraftTable;
import project.studio.manametalmod.blocks.api.BlockContainer;

@Mixin(BlockTileEntityMetalCraftTable.class)
public abstract class MixinBlockMetalCraftTable extends BlockContainer {

    protected MixinBlockMetalCraftTable(Material Material) {
        super(Material);
    }

    @Intrinsic
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        TileEntity ti = world.getTileEntity(x,y,z);
        if (ti instanceof MRNeighborsTile m) {
            m.m$updateNeighbors(world, m.m$getPos(), new BlockPos(tileX,tileY,tileZ));
        }
    }
}
