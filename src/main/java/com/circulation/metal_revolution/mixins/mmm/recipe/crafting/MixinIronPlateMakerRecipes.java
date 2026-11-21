package com.circulation.metal_revolution.mixins.mmm.recipe.crafting;

import java.util.Map;
import java.util.Random;

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
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import project.studio.manametalmod.items.crafting.IronPlateMakerRecipes;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Mixin(value = IronPlateMakerRecipes.class, remap = false)
public abstract class MixinIronPlateMakerRecipes {

    @Shadow
    private Map smeltingList;

    @Shadow
    private Map experienceList;

    @Shadow
    private Map smeltingextraitemList;

    @Shadow
    private Map smeltingextraitem_chanceList;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        m$getSmeltingList();
        m$getExperienceList();
        m$getSmeltingextraitemList();
        m$getSmeltingextraitem_chanceList();
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

    @Unique
    private Object2ObjectOpenCustomHashMap<ItemStack, ItemStack> m$getSmeltingextraitemList() {
        if (!(smeltingextraitemList instanceof Object2ObjectOpenCustomHashMap)) {
            smeltingextraitemList = new Object2ObjectOpenCustomHashMap<ItemStack, ItemStack>(
                smeltingextraitemList,
                ItemStackRecipeLookup.STRATEGY);
        }
        return (Object2ObjectOpenCustomHashMap<ItemStack, ItemStack>) smeltingextraitemList;
    }

    @Unique
    public Object2IntOpenCustomHashMap<ItemStack> m$getSmeltingextraitem_chanceList() {
        if (!(smeltingextraitem_chanceList instanceof Object2IntOpenCustomHashMap)) {
            smeltingextraitem_chanceList = new Object2IntOpenCustomHashMap<ItemStack>(
                smeltingextraitem_chanceList,
                ItemStackRecipeLookup.STRATEGY);
        }
        return (Object2IntOpenCustomHashMap<ItemStack>) smeltingextraitem_chanceList;
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
     * @reason 通过自定义哈希表直接查找副产物
     */
    @Overwrite
    public ItemStack getSmeltingExtraitem(ItemStack stack) {
        return ItemStackRecipeLookup.findObject(m$getSmeltingextraitemList(), stack);
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

    /**
     * @author circulation
     * @reason 直接查询概率并保留原随机判定
     */
    @Overwrite
    public boolean getSmeltingExtraitemChance(ItemStack stack) {
        var chances = m$getSmeltingextraitem_chanceList();
        int chance = ItemStackRecipeLookup.findInt(chances, stack);
        if (chance == ItemStackRecipeLookup.MISSING_INT && !ItemStackRecipeLookup.containsInt(chances, stack)) {
            return false;
        }

        Random ran = new Random();
        return chance > ran.nextInt(1000);
    }

}
