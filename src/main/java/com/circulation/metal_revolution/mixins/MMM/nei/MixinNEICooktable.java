package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import project.studio.manametalmod.nei.NEICooktable;

@Mixin(value = NEICooktable.class, remap = false)
public abstract class MixinNEICooktable extends TemplateRecipeHandler {

    @Intrinsic
    public String getOverlayIdentifier() {
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
        return super.getOverlayHandler(gui,recipe);
    }

}
