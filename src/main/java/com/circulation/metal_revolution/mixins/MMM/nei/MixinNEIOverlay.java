package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.OffsetPositioner;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.circulation.metal_revolution.MetalRevolution;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.nei.NEIManaCraftTable;
import project.studio.manametalmod.nei.NEIManaGravityWellHandler;
import project.studio.manametalmod.nei.NEIManaSewingMachine;
import project.studio.manametalmod.nei.NEIManaSpinningWheel;
import project.studio.manametalmod.nei.NEIMetalCraftTableHandler;

@Mixin(value = {
    NEIMetalCraftTableHandler.class,
    NEIManaCraftTable.class,
    NEIManaGravityWellHandler.class,
    NEIManaSewingMachine.class,
    NEIManaSpinningWheel.class
},remap = false)
public abstract class MixinNEIOverlay extends TemplateRecipeHandler {

    @Unique
    private boolean m$init = false;

    @Intrinsic
    public String getOverlayIdentifier() {
        if (!m$init){
            m$init = true;
            m$regNEIOverlay();
        }
        return getHandlerId();
    }

    @Unique
    private void m$regNEIOverlay() {
        final var aClass = getGuiClass();
        final var xy = MetalRevolution.proxy.getXY(this.getClass());
        RecipeInfo.registerOverlayHandler(aClass, new DefaultOverlayHandler(xy.x(), xy.y()), getOverlayIdentifier());
        RecipeInfo.registerGuiOverlay(aClass, getOverlayIdentifier(), new OffsetPositioner(xy.x(), xy.y()));
    }
}
