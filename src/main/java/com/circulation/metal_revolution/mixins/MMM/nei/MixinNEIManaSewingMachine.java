package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.circulation.metal_revolution.client.GuiSpinningWheel2;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.nei.NEIManaSewingMachine;

import java.awt.Rectangle;
import java.util.List;

@Mixin(value = NEIManaSewingMachine.class, remap = false)
public abstract class MixinNEIManaSewingMachine extends TemplateRecipeHandler {

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(125, 92, 28, 13), "SewingMachine_Crafting"));
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiSpinningWheel2.class;
    }

    @Mixin(value = NEIManaSewingMachine.SmeltingPair2.class, remap = false)
    public static class MixinSmeltingPair2 {

        @Shadow
        PositionedStack imp1;
        @Shadow
        PositionedStack imp2;
        @Shadow
        PositionedStack imp3;
        @Shadow
        PositionedStack imp4;
        @Shadow
        PositionedStack imp5;

        @Unique
        List<PositionedStack> m$inputs;

        @Inject(method = "<init>", at = @At("TAIL"))
        public void onInit(NEIManaSewingMachine this$0, ItemStack imp1, ItemStack imp2, ItemStack imp3, ItemStack imp4, ItemStack imp5, ItemStack out, CallbackInfo ci) {
            m$inputs = ObjectArrayList.of(this.imp1, this.imp2, this.imp3, this.imp4, this.imp5);
        }

        @Redirect(method = "getIngredients", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;asList([Ljava/lang/Object;)Ljava/util/List;"))
        public List<PositionedStack> getIngredients(Object... a) {
            return m$inputs;
        }

        /**
         * @author circulation
         * @reason 这里不应该返回任何材料
         */
        @Overwrite
        public List<PositionedStack> getOtherStacks() {
            return ObjectLists.emptyList();
        }
    }
}
