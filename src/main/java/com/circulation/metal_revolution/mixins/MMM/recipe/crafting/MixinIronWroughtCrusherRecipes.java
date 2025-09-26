package com.circulation.metal_revolution.mixins.MMM.recipe.crafting;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.items.crafting.IronWroughtCrusherRecipes;

import java.util.Map;
import java.util.Random;

@SuppressWarnings("rawtypes")
@Mixin(value = IronWroughtCrusherRecipes.class,remap = false)
public abstract class MixinIronWroughtCrusherRecipes {

    @Shadow
    private Map smeltingList;

    @Shadow
    private Map experienceList;

    @Shadow
    private Map smeltingextraitemList;

    @Shadow
    private Map smeltingextraitem_chanceList;

    @Shadow
    protected abstract boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.smeltingList = new Reference2ReferenceOpenHashMap<ItemStack, ItemStack>(smeltingList);
        this.experienceList = new Reference2FloatOpenHashMap<ItemStack>(experienceList);
        this.smeltingextraitemList = new Reference2ReferenceOpenHashMap<ItemStack, ItemStack>(smeltingextraitemList);
        this.smeltingextraitem_chanceList = new Reference2IntOpenHashMap<ItemStack>(smeltingextraitem_chanceList);
    }

    @Unique
    public Reference2FloatOpenHashMap<ItemStack> m$getExperienceList() {
        if (experienceList instanceof Reference2FloatOpenHashMap) {
            return (Reference2FloatOpenHashMap<ItemStack>) experienceList;
        } else {
            return (Reference2FloatOpenHashMap<ItemStack>) (experienceList = new Reference2FloatOpenHashMap<ItemStack>(experienceList));
        }
    }

    @Unique
    public Reference2IntOpenHashMap<ItemStack> m$getSmeltingextraitem_chanceList() {
        if (smeltingextraitem_chanceList instanceof Reference2IntOpenHashMap) {
            return (Reference2IntOpenHashMap<ItemStack>) smeltingextraitem_chanceList;
        } else {
            return (Reference2IntOpenHashMap<ItemStack>) (smeltingextraitem_chanceList = new Reference2IntOpenHashMap<ItemStack>(smeltingextraitem_chanceList));
        }
    }

    /**
     * @author circulationn
     * @reason 覆写方法适应Reference2FloatMap
     */
    @Overwrite
    public float func_151398_b(ItemStack stack) {
        if (stack == null) return 0;
        Item item = stack.getItem();
        if (item == null) return 0;
        float ret = item.getSmeltingExperience(stack);
        if (ret != -1.0f) {
            return ret;
        } else {
            for (Reference2FloatMap.Entry<ItemStack> entry : m$getExperienceList().reference2FloatEntrySet()) {
                if (this.func_151397_a(stack, entry.getKey())) {
                    return entry.getFloatValue();
                }
            }

            return 0;
        }
    }

    /**
     * @author circulationn
     * @reason 覆写方法适应Reference2IntMap
     */
    @Overwrite
    public boolean getSmeltingExtraitemChance(ItemStack stack) {
        for (Reference2IntMap.Entry<ItemStack> entry : m$getSmeltingextraitem_chanceList().reference2IntEntrySet()) {
            if (this.func_151397_a(stack, entry.getKey())) {
                final int chance = entry.getIntValue();
                Random ran = new Random();
                return chance > ran.nextInt(1000);
            }
        }

        return false;
    }
}
