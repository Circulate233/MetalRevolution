package com.circulation.metal_revolution.mixins.MMM.block;

import com.circulation.metal_revolution.common.MRTileEntityCrystalPillars;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import project.studio.manametalmod.blocks.BlockTileEntityCrystalPillars;

@Mixin(BlockTileEntityCrystalPillars.class)
public class MixinBlockCrystalPillars {

    @Intrinsic
    public TileEntity createTileEntity(World world, int metadata) {
        return new MRTileEntityCrystalPillars();
    }

    /**
     * @author circulation
     * @reason 替换实体避免旧实体的一些问题
     */
    @Overwrite
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new MRTileEntityCrystalPillars();
    }

}
