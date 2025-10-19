package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import project.studio.manametalmod.nei.NEICooktable;

@Mixin(value = NEICooktable.class, remap = false)
public abstract class MixinNEICooktable extends TemplateRecipeHandler {

    @Intrinsic
    public String getOverlayIdentifier() {
        return getHandlerId();
    }

}
