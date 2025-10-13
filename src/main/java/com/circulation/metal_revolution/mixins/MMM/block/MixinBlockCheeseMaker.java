package com.circulation.metal_revolution.mixins.MMM.block;


import com.circulation.metal_revolution.common.MRTileEntityCheeseMaker;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import project.studio.manametalmod.blocks.BlockTileEntityCheeseMaker;

@Mixin(BlockTileEntityCheeseMaker.class)
public class MixinBlockCheeseMaker extends BlockContainer {

    protected MixinBlockCheeseMaker(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Intrinsic
    public TileEntity createTileEntity(World world, int metadata) {
        return new MRTileEntityCheeseMaker();
    }

    /**
     * @author circulation
     * @reason 替换实体避免旧实体的一些问题
     */
    @Overwrite
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new MRTileEntityCheeseMaker();
    }
}
