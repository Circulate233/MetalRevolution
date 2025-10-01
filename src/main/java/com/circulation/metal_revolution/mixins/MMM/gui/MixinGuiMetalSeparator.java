package com.circulation.metal_revolution.mixins.MMM.gui;

import com.circulation.metal_revolution.iinterface.MRMetalSeparator;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.client.GuiMetalSeparator;
import project.studio.manametalmod.tileentity.TileEntityMetalSeparator;

@Mixin(GuiMetalSeparator.class)
public abstract class MixinGuiMetalSeparator extends GuiContainer {

    @Shadow(remap = false)
    TileEntityMetalSeparator te;

    public MixinGuiMetalSeparator(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    @Intrinsic
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX,mouseY,partialTicks);
        MRMetalSeparator te = (MRMetalSeparator) this.te;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if (mouseX > k + 130 && mouseX < k + 144 && mouseY > l + 37 && mouseY < l + 50) {
            this.drawCreativeTabHoveringText(I18n.format("text.mr.cache_energy", te.m$getEnergy(), te.m$getMaxEnergy()), mouseX, mouseY);
        }
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY, CallbackInfo ci, @Local(name = "k") int k, @Local(name = "l") int l) {
        MRMetalSeparator te = (MRMetalSeparator) this.te;
        int i1 = te.m$getCache(13);
        if (i1 > 13) {
            i1 = 13;
        }
        this.drawTexturedModalRect(k + 130, l + 37, 32, 37, 14, 13);
        if (te.m$getEnergy() > 0) {
            this.drawTexturedModalRect(k + 130, l + 49 - i1, 176, 12 - i1, 14, i1 + 1);
        }
    }
}
