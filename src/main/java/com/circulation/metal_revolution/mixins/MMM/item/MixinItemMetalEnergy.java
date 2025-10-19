package com.circulation.metal_revolution.mixins.MMM.item;

import com.circulation.metal_revolution.utils.MRUtil;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.items.ItemMetalEnergy;

@Mixin(ItemMetalEnergy.class)
public class MixinItemMetalEnergy extends Item {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(String name, int size, CallbackInfo ci) {
        MRUtil.metalMap.put(this, size);
    }
}
