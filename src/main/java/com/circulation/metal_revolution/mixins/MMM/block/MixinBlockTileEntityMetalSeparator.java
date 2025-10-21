package com.circulation.metal_revolution.mixins.MMM.block;

import com.circulation.metal_revolution.common.MRTileEntityMetalSeparator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import project.studio.manametalmod.blocks.BlockTileEntityMetalSeparator;
import project.studio.manametalmod.blocks.api.BlockContainer;

@Mixin(BlockTileEntityMetalSeparator.class)
public class MixinBlockTileEntityMetalSeparator extends BlockContainer {

    @Shadow(remap = false)
    int speed;

    protected MixinBlockTileEntityMetalSeparator(Material material) {
        super(material);
    }

    @Intrinsic
    public TileEntity createTileEntity(World world, int metadata) {
        return new MRTileEntityMetalSeparator(this.speed);
    }

    /**
     * @author circulation
     * @reason 替换实体避免旧实体的一些问题
     */
    @Overwrite
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new MRTileEntityMetalSeparator(this.speed);
    }
}
