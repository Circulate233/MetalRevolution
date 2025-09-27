package com.circulation.metal_revolution.mixins.MMM.tile;

import com.circulation.metal_revolution.utils.SimpleItem;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanFunction;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceReferenceMutablePair;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.core.RecipeOre;
import project.studio.manametalmod.tileentity.TileEntityBase;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(value = TileEntityBase.class,remap = false)
public abstract class MixinTileEntityBase extends TileEntity implements ISidedInventory {

    @Shadow
    public abstract boolean isOKFuel(ItemStack item);

    @Shadow
    public List<RecipeOre> recipe;

    @Shadow
    public ItemStack[] items;
    @Shadow
    public String TileName;
    @Unique
    private static final int[] m$AllSlot = {0, 1, 2, 3};

    @Unique
    private final static Map<String, Reference2BooleanFunction<ItemStack>[]> m$valids = new Object2ObjectOpenHashMap<>();

    @Unique
    private Pair<List<RecipeOre>, Set<SimpleItem>> m$imp1;
    @Unique
    private Pair<List<RecipeOre>, Set<SimpleItem>> m$imp2;

    @Unique
    private Reference2ObjectMap<SimpleItem, Set<SimpleItem>> m$rimp1;
    @Unique
    private Reference2ObjectMap<SimpleItem, Set<SimpleItem>> m$rimp2;

    @Inject(method = "<init>(Ljava/lang/String;Ljava/util/List;ILproject/studio/manametalmod/core/FuelType;)V", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        m$initValid();
    }

    @Inject(method = "<init>()V", at = @At("TAIL"))
    public void onEmptyInit(CallbackInfo ci) {
        m$initValid();
    }

    @Unique
    private void m$initValid() {
        if (m$valids.containsKey(this.TileName)) return;
        var valid = new Reference2BooleanFunction[m$AllSlot.length];
        valid[0] = item -> this.m$isItemRecipe1((ItemStack) item);
        valid[1] = item -> false;
        valid[2] = item -> this.isOKFuel((ItemStack) item);
        valid[3] = item -> this.m$isItemRecipe2((ItemStack) item);
        m$valids.put(this.TileName, valid);
    }

    @Unique
    private void m$initRecipe() {
        if (this.recipe != null) {
            Set<SimpleItem> imp1;
            Set<SimpleItem> imp2;
            if (m$imp1 == null || m$imp2 == null) {
                imp1 = new ReferenceOpenHashSet<>();
                imp2 = new ReferenceOpenHashSet<>();
                m$imp1 = ReferenceReferenceMutablePair.of(this.recipe, imp1);
                m$imp2 = ReferenceReferenceMutablePair.of(this.recipe, imp2);
            } else {
                (imp1 = m$imp1.right()).clear();
                (imp2 = m$imp2.right()).clear();
                m$imp1.left(this.recipe);
                m$imp2.left(this.recipe);
            }
            Reference2ObjectMap<SimpleItem, Set<SimpleItem>> rimp1 = new Reference2ObjectOpenHashMap<>();
            Reference2ObjectMap<SimpleItem, Set<SimpleItem>> rimp2 = new Reference2ObjectOpenHashMap<>();

            for (RecipeOre recipeOre : this.recipe) {
                var list1 = recipeOre.getImp1().getItem();
                var list2 = recipeOre.getImp2().getItem();
                for (ItemStack stack : list1) {
                    var s = SimpleItem.getNoNBTInstance(stack);
                    imp1.add(s);
                    var set = rimp1.computeIfAbsent(s, ss -> new ReferenceOpenHashSet<>());
                    list2.forEach(i -> set.add(SimpleItem.getNoNBTInstance(i)));
                }
                for (ItemStack stack : list2) {
                    var s = SimpleItem.getNoNBTInstance(stack);
                    imp2.add(s);
                    var set = rimp2.computeIfAbsent(s, ss -> new ReferenceOpenHashSet<>());
                    list1.forEach(i -> set.add(SimpleItem.getNoNBTInstance(i)));
                }
            }

            m$rimp1 = rimp1;
            m$rimp2 = rimp2;
        }
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite(remap = true)
    public int[] getAccessibleSlotsFromSide(int side) {
        return m$AllSlot;
    }

    /**
     * @author circulation
     * @reason 覆写
     */
    @Overwrite(remap = true)
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return m$valids.get(this.TileName)[slot].getBoolean(stack);
    }

    @Unique
    public boolean m$isItemRecipe1(@NotNull ItemStack item) {
        if (this.items == null) return false;
        if (this.items[3] != null) {
            if (m$rimp2 == null) {
                m$initRecipe();
            }
            return m$rimp2
                .getOrDefault(SimpleItem.getNoNBTInstance(this.items[3]), ReferenceSets.emptySet())
                .contains(SimpleItem.getNoNBTInstance(item));
        }
        if (this.recipe != null) {
            if (m$imp1 == null || m$imp1.left() != this.recipe) {
                m$initRecipe();
            }
            return m$imp1.right().contains(SimpleItem.getNoNBTInstance(item));
        }

        return false;
    }

    @Unique
    public boolean m$isItemRecipe2(@NotNull ItemStack item) {
        if (this.items == null) return false;
        if (this.items[0] != null) {
            if (m$rimp1 == null) {
                m$initRecipe();
            }
            return m$rimp1
                .getOrDefault(SimpleItem.getNoNBTInstance(this.items[0]), ReferenceSets.emptySet())
                .contains(SimpleItem.getNoNBTInstance(item));
        }
        if (this.recipe != null) {
            if (m$imp2 == null || m$imp2.left() != this.recipe) {
                m$initRecipe();
            }
            return m$imp2.right().contains(SimpleItem.getNoNBTInstance(item));
        }

        return false;
    }
}
