package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.PositionedStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.nei.NEIManaMetalInjectionHandler;

import java.util.List;

@Mixin(value = NEIManaMetalInjectionHandler.SmeltingPair2.class, remap = false)
public class MixinNEIManaMetalInjection {

    @Shadow
    PositionedStack ingred;
    @Shadow
    PositionedStack ingred2;
    @Shadow
    PositionedStack ingred3;
    @Shadow
    PositionedStack ingred4;
    @Shadow
    PositionedStack ingred5;
    @Shadow
    PositionedStack ingred6;
    @Shadow
    PositionedStack ingred7;
    @Shadow
    PositionedStack ingred8;
    @Shadow
    PositionedStack ingred9;

    @Unique
    List<PositionedStack> m$inputs;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(NEIManaMetalInjectionHandler this$0, ItemStack ingred1, ItemStack ingred2, ItemStack ingred3, ItemStack ingred4, ItemStack ingred5, ItemStack ingred6, ItemStack ingred7, ItemStack ingred8, ItemStack ingred9, ItemStack result, CallbackInfo ci) {
        m$inputs = ObjectArrayList.of(this.ingred, this.ingred2, this.ingred3, this.ingred4, this.ingred5, this.ingred6, this.ingred7, this.ingred8, this.ingred9);
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
