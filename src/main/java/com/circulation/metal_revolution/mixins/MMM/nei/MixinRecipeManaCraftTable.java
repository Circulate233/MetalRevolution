package com.circulation.metal_revolution.mixins.MMM.nei;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import codechicken.nei.PositionedStack;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import project.studio.manametalmod.nei.NEIManaCraftTable;

@Mixin(value = NEIManaCraftTable.RecipeManaCraftTable.class, remap = false)
public class MixinRecipeManaCraftTable {

    /**
     * @author circulaiton
     * @reason 解决不太正确的方法实现
     */
    @Overwrite
    public List<PositionedStack> getOtherStacks() {
        return ObjectLists.emptyList();
    }

}
