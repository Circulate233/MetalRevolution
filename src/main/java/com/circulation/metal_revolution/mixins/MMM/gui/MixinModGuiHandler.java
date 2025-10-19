package com.circulation.metal_revolution.mixins.MMM.gui;

import com.circulation.metal_revolution.client.GuiSpinningWheel2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.client.GuiSpinningWheel;
import project.studio.manametalmod.network.ModGuiHandler;
import project.studio.manametalmod.produce.textile.TileEntitySpinningWheel;

@Mixin(value = ModGuiHandler.class, remap = false)
public class MixinModGuiHandler {

    @Inject(method = "getClientGuiElement", at = @At("HEAD"), cancellable = true)
    public void getClientGuiElementI(int ID, EntityPlayer player, World world, int x, int y, int z, CallbackInfoReturnable<Object> cir) {
        if (ID == 161) {
            var te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntitySpinningWheel sw) {
                Object gui;
                if (sw.blockID == 1) {
                    gui = new GuiSpinningWheel2(player.inventory, sw);
                } else {
                    gui = new GuiSpinningWheel(player.inventory, sw);
                }
                cir.setReturnValue(gui);
                return;
            }
            cir.setReturnValue(null);
        }
    }
}
