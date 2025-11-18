package com.circulation.metal_revolution.mixins.MMM.container;

import com.circulation.metal_revolution.interfaces.MRMetalSeparator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.inventory.ContainerMetalSeparator;
import project.studio.manametalmod.tileentity.TileEntityMetalSeparator;

@Mixin(ContainerMetalSeparator.class)
public abstract class MixinContainerMetalSeparator extends Container {

    @Shadow(remap = false)
    private TileEntityMetalSeparator te;

    @Unique
    private int m$lastCacheEnergy;

    @Inject(method = "detectAndSendChanges", at = @At("TAIL"))
    public void detectAndSendChanges(CallbackInfo ci) {
        if (!(this.te instanceof MRMetalSeparator t)) return;
        for (Object crafter : this.crafters) {
            if (crafter instanceof ICrafting iCrafting) {
                if (this.m$lastCacheEnergy != t.m$getEnergy())
                    iCrafting.sendProgressBarUpdate(this, 3, t.m$getEnergy());
            }
        }
        this.m$lastCacheEnergy = t.m$getEnergy();
    }

    @Inject(method = "addCraftingToCrafters", at = @At("TAIL"))
    public void addCraftingToCrafters(ICrafting icrafting, CallbackInfo ci) {
        if (!(this.te instanceof MRMetalSeparator t)) return;
        icrafting.sendProgressBarUpdate(this, 3, t.m$getEnergy());
    }

    @SideOnly(Side.CLIENT)
    @Inject(method = "updateProgressBar", at = @At("TAIL"))
    public void updateProgressBar(int ord, int value, CallbackInfo ci) {
        if (!(this.te instanceof MRMetalSeparator t)) return;
        if (ord == 3) {
            t.m$setEnergy(value);
        }

    }

    @Intrinsic
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
        if (this.te instanceof MRMetalSeparator t) {
            if (slotId == 2) {
                var o = super.slotClick(slotId, clickedButton, mode, player);
                if (o == null) {
                    if (t.m$upOutSlot()) {
                        o = super.slotClick(slotId, clickedButton, mode, player);
                        t.m$upOutSlot();
                    }
                } else {
                    t.m$upOutSlot();
                }

                return o;
            }
            if (slotId == 3) {
                t.m$upSmallOutSlot();
            }
        }
        return super.slotClick(slotId, clickedButton, mode, player);
    }

}
