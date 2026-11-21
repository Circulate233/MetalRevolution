package com.circulation.metal_revolution.mixins.mmm;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import com.circulation.metal_revolution.utils.ItemStackRecipeLookup;
import com.circulation.metal_revolution.utils.MInitUtil;
import com.circulation.metal_revolution.utils.MRUtil;

import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.registry.GameData;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import project.studio.manametalmod.MMM;

@Mixin(value = MMM.class, remap = false, priority = 999)
public class MixinMMM {

    @Unique
    private static final Reference2IntMap<Item> m$maneItemFuelMap = MInitUtil.maneItemFuelMap;
    @Unique
    private static final Object2IntMap<ItemStack> m$itemFuelMap = Object2IntMaps
        .synchronize(new Object2IntOpenCustomHashMap<>(ItemStackRecipeLookup.NBT_STRATEGY));
    @Unique
    private static final Object2ObjectMap<ItemStack, String[]> m$ODNameMap = Object2ObjectMaps
        .synchronize(new Object2ObjectOpenCustomHashMap<>(ItemStackRecipeLookup.STRATEGY));

    static {
        m$itemFuelMap.defaultReturnValue(-1);
    }

    /**
     * @author circulation
     * @reason 测试性的新燃料查询方式
     */
    @Overwrite
    public static int getManaItemFuelTime(ItemStack items) {
        if (items != null && items.stackSize != 0) {
            return m$maneItemFuelMap.getInt(items.getItem());
        } else {
            return 0;
        }
    }

    /**
     * @author circulation
     * @reason 测试性的新燃料查询方式
     */
    @Overwrite
    public static int getItemStackFuelValue(ItemStack items) {
        ItemStack key = items == null || items.getItem() == null ? null : items;
        int out = m$itemFuelMap.getInt(key);
        if (out < 0) {
            ItemStack snapshot = key == null ? null : key.copy();
            out = TileEntityFurnace.getItemBurnTime(items);
            m$itemFuelMap.put(snapshot, out);
        }
        return out;
    }

    /**
     * @author circulation
     * @reason 测试性的查询方式
     */
    @Overwrite
    public static String[] getItemOreDictionaryName(ItemStack item) {
        if (item == null) {
            return null;
        } else {
            ItemStack key = item.getItem() == null ? null : item;
            String[] names = m$ODNameMap.get(key);
            if (names == null) {
                ItemStack snapshot = key == null ? null : key.copy();
                int[] ids = OreDictionary.getOreIDs(item);
                names = new String[ids.length];
                if (ids.length != 0) {
                    for (int s = 0; s < names.length; ++s) {
                        names[s] = OreDictionary.getOreName(ids[s]);
                    }
                }
                m$ODNameMap.put(snapshot, names);
            }
            if (names.length == 0) {
                return null;
            }
            return names;
        }
    }

    /**
     * @author circulation
     * @reason 更效率的检测是否加载CoFHAPI|energy
     */
    @Overwrite
    public static boolean checkIfRfLoaded() {
        return ModAPIManager.INSTANCE.hasAPI("CoFHAPI|energy");
    }

    /**
     * @author circulation
     * @reason 一种可能更快的物品MODID获取？
     */
    @Overwrite
    public static String getItemStackModid(@Nonnull ItemStack stack) {
        return getItemModid(stack.getItem());
    }

    /**
     * @author circulation
     * @reason 一种可能更快的物品MODID获取？
     */
    @Overwrite
    public static String getItemModid(Item stack) {
        String id = GameData.getItemRegistry()
            .getNameForObject(stack);
        if (id == null) return "minecraft";
        int separator = id.indexOf(':');
        return separator < 0 ? id : id.substring(0, separator);
    }

    /**
     * @author circulation
     * @reason 测试性的查询方式
     */
    @Overwrite
    public static int getItemMetalEnergy(ItemStack items) {
        if (items != null && items.stackSize != 0) {
            return MRUtil.metalMap.getInt(items.getItem());
        } else {
            return 0;
        }
    }
}
