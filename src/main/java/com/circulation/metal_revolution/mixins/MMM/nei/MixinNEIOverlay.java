package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.OffsetPositioner;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.circulation.metal_revolution.MetalRevolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
}, remap = false)
public abstract class MixinNEIOverlay extends TemplateRecipeHandler {

    @Unique
    private DefaultOverlayHandler m$oh;
    @Unique
    private boolean m$init = false;

    @Inject(method = "<init>",at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        final var aClass = getGuiClass();
        final var xy = MetalRevolution.proxy.getXY(this.getClass());
         m$oh = new DefaultOverlayHandler(xy.x(), xy.y());
    }


    @Intrinsic
    public String getOverlayIdentifier() {
        if (!m$init) {
            m$init = true;
            m$regNEIOverlay();
        }
        return getHandlerId();
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return getOverlayHandler(gui, recipe) != null || getOverlayRenderer(gui, recipe) != null;
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        if (!getGuiClass().isInstance(gui)) return null;
        return super.getOverlayRenderer(gui,recipe);
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        if (!getGuiClass().isInstance(gui)) return RecipeInfo.getOverlayHandler(gui, null);
        return m$oh;
    }

    @Unique
    private void m$regNEIOverlay() {
        final var aClass = getGuiClass();
        final var xy = MetalRevolution.proxy.getXY(this.getClass());
        RecipeInfo.registerGuiOverlay(aClass, getOverlayIdentifier(), new OffsetPositioner(xy.x(), xy.y()));
    }
}
