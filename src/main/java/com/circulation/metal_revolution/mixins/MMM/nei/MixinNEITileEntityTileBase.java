package com.circulation.metal_revolution.mixins.MMM.nei;

import codechicken.nei.api.API;
import codechicken.nei.recipe.GuiRecipeTab;
import codechicken.nei.recipe.HandlerInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.core.FuelType;
import project.studio.manametalmod.core.RecipeOre;
import project.studio.manametalmod.nei.NEITileEntityTileBase;

import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(value = NEITileEntityTileBase.class,remap = false)
public abstract class MixinNEITileEntityTileBase extends TemplateRecipeHandler {

    @Unique
    private static final Map<Item, String> m$itemStringMap = new Reference2ObjectOpenHashMap<>();

    @Unique
    private String m$handlerId = "";

    @Shadow
    public String TileName;

    @Shadow
    public abstract void addManaRecipeUse(ItemStack result);

    @Shadow
    public FuelType Fuel;

    @Shadow
    public List<RecipeOre> recipe;

    @Intrinsic
    public String getHandlerId() {
        return m$handlerId;
    }

    @Inject(method = "setData", at = @At("TAIL"))
    public void setData(String name, FuelType type, List<RecipeOre> recipe, CallbackInfo ci) {
        m$handlerId = getClass().getName() + this.TileName;
        var stack = MMM.findItemStackM3("BlockTileEntityBase_" + this.TileName, 1, 0);
        m$setinfo(stack);
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(73, 25, 25, 13), this.TileName + "_Crafting"));
        m$itemStringMap.put(stack.getItem(), this.TileName);
    }

    /**
     * @author circulation
     * @reason 重写方法处理一些错误
     */
    @Overwrite
    public TemplateRecipeHandler newInstance() {
        findFuelsOnce();
        var newInstance = new NEITileEntityTileBase();
        newInstance.findFuels();
        newInstance.setData(this.TileName, this.Fuel, this.recipe);
        return newInstance;
    }

    /**
     * @author circulation
     * @reason 疑似写错了的方法？
     */
    @Overwrite
    public void loadUsageRecipes(@NotNull String inputId, Object... ingredients) {
        super.loadUsageRecipes(inputId, ingredients);
        if (inputId.equals("item") && (ingredients.length > 0 && ingredients[0] instanceof ItemStack stack && Objects.equals(this.TileName, m$itemStringMap.get(stack.getItem())))) {
            this.addManaRecipeUse(null);
        }
    }

    @Unique
    private void m$setinfo(Object item) {
        String nameid = this.m$handlerId;
        ItemStack baseitem = MMM.item(item);
        GuiRecipeTab.handlerMap.put(nameid, (new HandlerInfo.Builder(nameid, "ManaMetalMod", MMM.getMODID())).setDisplayStack(baseitem).build());
        API.addRecipeCatalyst(baseitem, nameid);
    }

}
