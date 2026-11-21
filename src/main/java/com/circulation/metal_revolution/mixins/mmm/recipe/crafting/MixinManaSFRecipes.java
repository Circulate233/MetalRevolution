package com.circulation.metal_revolution.mixins.mmm.recipe.crafting;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.circulation.metal_revolution.utils.ItemStackRecipeLookup;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import project.studio.manametalmod.items.crafting.ManaSFRecipes;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Mixin(value = ManaSFRecipes.class, remap = false)
public abstract class MixinManaSFRecipes {

    @Shadow
    private Map smeltingList;

    @Shadow
    private Map experienceList;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        m$getSmeltingList();
        m$getExperienceList();
    }

    @Unique
    private Object2ObjectOpenCustomHashMap<ItemStack, ItemStack> m$getSmeltingList() {
        if (!(smeltingList instanceof Object2ObjectOpenCustomHashMap)) {
            smeltingList = new Object2ObjectOpenCustomHashMap<ItemStack, ItemStack>(
                smeltingList,
                ItemStackRecipeLookup.STRATEGY);
        }
        return (Object2ObjectOpenCustomHashMap<ItemStack, ItemStack>) smeltingList;
    }

    @Unique
    public Object2FloatOpenCustomHashMap<ItemStack> m$getExperienceList() {
        if (!(experienceList instanceof Object2FloatOpenCustomHashMap)) {
            experienceList = new Object2FloatOpenCustomHashMap<ItemStack>(
                experienceList,
                ItemStackRecipeLookup.STRATEGY);
        }
        return (Object2FloatOpenCustomHashMap<ItemStack>) experienceList;
    }

    /**
     * @author circulation
     * @reason 通过自定义哈希表直接查找配方
     */
    @Overwrite
    public ItemStack getSmeltingResult(ItemStack stack) {
        return ItemStackRecipeLookup.findObject(m$getSmeltingList(), stack);
    }

    /**
     * @author circulation
     * @reason 保留物品经验覆盖并直接查询经验表
     */
    @Overwrite
    public float func_151398_b(ItemStack stack) {
        if (stack == null) return 0;
        Item item = stack.getItem();
        if (item == null) return 0;
        float ret = item.getSmeltingExperience(stack);
        if (ret != -1.0f) return ret;

        return ItemStackRecipeLookup.findFloat(m$getExperienceList(), stack);
    }

}
