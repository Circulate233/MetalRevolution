package com.circulation.metal_revolution.mixins.MMM.recipe.crafting;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import project.studio.manametalmod.items.crafting.AlloyFurnaceRecipes;

@Mixin(value = AlloyFurnaceRecipes.class, remap = false)
public class MixinAlloyFurnaceRecipes {

    @Shadow
    public List<ItemStack[]> list;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.list = new ObjectArrayList<>(this.list);
    }
}
