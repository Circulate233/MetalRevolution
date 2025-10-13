package com.circulation.metal_revolution.mixins.MMM.block;

import com.circulation.metal_revolution.common.MRTileEntityGilded;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import project.studio.manametalmod.Lapuda.BlockGilded;
import project.studio.manametalmod.blocks.BlockBase;

@Mixin(BlockGilded.class)
public abstract class MixinBlockGilded extends BlockBase {

    public MixinBlockGilded(Material Materials, String Name) {
        super(Materials, Name);
    }

    @Intrinsic
    public TileEntity createTileEntity(World world, int metadata) {
        return new MRTileEntityGilded();
    }

    /**
     * @author circulation
     * @reason 替换实体避免旧实体的一些问题
     */
    @Overwrite
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new MRTileEntityGilded();
    }
}
