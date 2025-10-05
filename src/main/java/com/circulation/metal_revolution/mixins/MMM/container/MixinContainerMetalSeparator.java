package com.circulation.metal_revolution.mixins.MMM.container;

import com.circulation.metal_revolution.iinterface.MRMetalSeparator;
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
        for (Object crafter : this.crafters) {
            if (crafter instanceof ICrafting iCrafting) {
                if (this.m$lastCacheEnergy != ((MRMetalSeparator) this.te).m$getEnergy())
                    iCrafting.sendProgressBarUpdate(this, 3, ((MRMetalSeparator) this.te).m$getEnergy());
            }
        }
        this.m$lastCacheEnergy = ((MRMetalSeparator) this.te).m$getEnergy();
    }

    @Inject(method = "addCraftingToCrafters", at = @At("TAIL"))
    public void addCraftingToCrafters(ICrafting icrafting, CallbackInfo ci) {
        icrafting.sendProgressBarUpdate(this, 3, ((MRMetalSeparator) this.te).m$getEnergy());
    }

    @SideOnly(Side.CLIENT)
    @Inject(method = "updateProgressBar", at = @At("TAIL"))
    public void updateProgressBar(int ord, int value, CallbackInfo ci) {
        if (ord == 3) {
            ((MRMetalSeparator) this.te).m$setEnergy(value);
        }

    }

    @Intrinsic
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
        if (slotId == 2) {
            MRMetalSeparator te = (MRMetalSeparator) this.te;
            var o = super.slotClick(slotId, clickedButton, mode, player);
            if (o == null){
                if (te.m$upOutSlot()){
                    o = super.slotClick(slotId, clickedButton, mode, player);
                    te.m$upOutSlot();
                }
            } else {
                te.m$upOutSlot();
            }

            return o;
        }
        if (slotId == 3) {
            MRMetalSeparator te = (MRMetalSeparator) this.te;
            te.m$upSmallOutSlot();
        }
        return super.slotClick(slotId, clickedButton, mode, player);
    }

}
