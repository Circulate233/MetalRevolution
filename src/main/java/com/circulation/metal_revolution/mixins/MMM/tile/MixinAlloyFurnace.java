package com.circulation.metal_revolution.mixins.MMM.tile;

import com.circulation.metal_revolution.utils.SimpleItem;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.items.crafting.AlloyFurnaceRecipes;
import project.studio.manametalmod.tileentity.TileEntityAlloyFurnace;

import java.util.Set;

@Mixin(TileEntityAlloyFurnace.class)
public abstract class MixinAlloyFurnace {

    @Shadow(remap = false)
    public static boolean isItemFuel(ItemStack item) {
        return false;
    }

    @Shadow(remap = false)
    private ItemStack[] inventory;

    @Unique
    private static final int[] m$AllSlot = {0, 1, 2, 3, 4};

    @Unique
    private Set<SimpleItem> m$imp;
    @Unique
    private Reference2ObjectMap<SimpleItem, Set<SimpleItem>> m$rimp1;
    @Unique
    private Reference2ObjectMap<SimpleItem, Set<SimpleItem>> m$rimp2;

    @Unique
    private void m$initRecipe() {
        Set<SimpleItem> imp = new ReferenceOpenHashSet<>();
        Reference2ObjectMap<SimpleItem, Set<SimpleItem>> rimp1 = new Reference2ObjectOpenHashMap<>();
        Reference2ObjectMap<SimpleItem, Set<SimpleItem>> rimp2 = new Reference2ObjectOpenHashMap<>();
        for (ItemStack[] recipe : AlloyFurnaceRecipes.smelting().list) {
            var list1 = recipe[0];
            var list2 = recipe[1];
            var s1 = SimpleItem.getNoNBTInstance(list1);
            var s2 = SimpleItem.getNoNBTInstance(list2);

            imp.add(s1);
            imp.add(s2);

            rimp1.computeIfAbsent(s1, ss -> new ReferenceOpenHashSet<>())
                .add(s2);
            rimp2.computeIfAbsent(s2, ss -> new ReferenceOpenHashSet<>())
                .add(s1);
        }
        m$imp = imp;
        m$rimp1 = rimp1;
        m$rimp2 = rimp2;
    }

    @Unique
    private static final Reference2BooleanFunction<ItemStack>[] m$valid = new Reference2BooleanFunction[m$AllSlot.length];

    @Inject(method = "<init>()V", at = @At("TAIL"), remap = false)
    public void onInit(CallbackInfo ci) {
        if (m$valid[0] != null) return;
        m$valid[0] = item -> m$isItemRecipe1((ItemStack) item);
        m$valid[1] = item -> isItemFuel((ItemStack) item);
        m$valid[2] = item -> false;
        m$valid[3] = item -> false;
        m$valid[4] = item -> m$isItemRecipe2((ItemStack) item);
    }

    @Inject(method = "<init>(I)V", at = @At("TAIL"), remap = false)
    public void onInitI(CallbackInfo ci) {
        if (m$valid[0] != null) return;
        m$valid[0] = item -> m$isItemRecipe1((ItemStack) item);
        m$valid[1] = item -> isItemFuel((ItemStack) item);
        m$valid[2] = item -> false;
        m$valid[3] = item -> false;
        m$valid[4] = item -> m$isItemRecipe2((ItemStack) item);
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return m$valid[slot].getBoolean(stack);
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 2 || slot == 3 || stack.getItem() == Items.bucket;
    }

    @Unique
    public boolean m$isItemRecipe1(@NotNull ItemStack item) {
        if (this.inventory == null) return false;
        if (this.inventory[4] != null) {
            if (m$rimp2 == null) {
                m$initRecipe();
            }
            return m$rimp2
                .getOrDefault(SimpleItem.getNoNBTInstance(this.inventory[4]), ReferenceSets.emptySet())
                .contains(SimpleItem.getNoNBTInstance(item));
        }
        if (m$imp == null) {
            m$initRecipe();
        }
        return m$imp.contains(SimpleItem.getNoNBTInstance(item));
    }

    @Unique
    public boolean m$isItemRecipe2(@NotNull ItemStack item) {
        if (this.inventory == null) return false;
        if (this.inventory[0] != null) {
            if (m$rimp1 == null) {
                m$initRecipe();
            }
            return m$rimp1
                .getOrDefault(SimpleItem.getNoNBTInstance(this.inventory[0]), ReferenceSets.emptySet())
                .contains(SimpleItem.getNoNBTInstance(item));
        }
        if (m$imp == null) {
            m$initRecipe();
        }
        return m$imp.contains(SimpleItem.getNoNBTInstance(item));
    }
}
