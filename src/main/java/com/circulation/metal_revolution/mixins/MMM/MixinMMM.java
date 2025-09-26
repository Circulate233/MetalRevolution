package com.circulation.metal_revolution.mixins.MMM;

import com.circulation.metal_revolution.utils.SimpleItem;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.registry.GameData;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.ManaMetalMod;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft10;

@Mixin(value = MMM.class,remap = false,priority = 999)
public class MixinMMM {

    @Unique
    private static final Reference2IntMap<Item> m$maneItemFuelMap = Reference2IntMaps.synchronize(new Reference2IntOpenHashMap<>());

    static {
        m$addManeFuel(ManaMetalMod.dustMana, 50);
        m$addManeFuel(ManaMetalMod.ingotMana, 450);
        m$addManeFuel(ManaMetalMod.dustSmallMana, 5);
        m$addManeFuel(Item.getItemFromBlock(ManaMetalMod.blockMana), 4050);
        m$addManeFuel(ItemCraft10.BucketMana, 400);
        m$addManeFuel(ItemCraft10.ItemMana, 100);
        m$maneItemFuelMap.defaultReturnValue(0);
    }

    @Unique
    private static void m$addManeFuel(Item item, int time) {
        m$maneItemFuelMap.put(item, time);
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

    @Unique
    private static final Reference2IntMap<SimpleItem> m$itemFuelMap = Reference2IntMaps.synchronize(new Reference2IntOpenHashMap<>());

    /**
     * @author circulation
     * @reason 测试性的新燃料查询方式
     */
    @Overwrite
    public static int getItemStackFuelValue(ItemStack items) {
        return m$itemFuelMap.computeIfAbsent(SimpleItem.getInstance(items), s -> TileEntityFurnace.getItemBurnTime(items));
    }

    @Unique
    private static final Reference2ReferenceMap<SimpleItem, String[]> m$ODNameMap = Reference2ReferenceMaps.synchronize(new Reference2ReferenceOpenHashMap<>());

    /**
     * @author circulation
     * @reason 测试性的查询方式
     */
    @Overwrite
    public static String[] getItemOreDictionaryName(ItemStack item) {
        if (item == null) {
            return null;
        } else {
            var si = SimpleItem.getNoNBTInstance(item);
            String[] names = m$ODNameMap.get(si);
            if (names == null) {
                int[] ids = OreDictionary.getOreIDs(item);
                names = new String[ids.length];
                if (ids.length != 0) {
                    for (int s = 0; s < names.length; ++s) {
                        names[s] = OreDictionary.getOreName(ids[s]);
                    }
                }
                m$ODNameMap.put(si, names);
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
    public static String getItemStackModid(@NotNull ItemStack stack) {
        return getItemModid(stack.getItem());
    }

    /**
     * @author circulation
     * @reason 一种可能更快的物品MODID获取？
     */
    @Overwrite
    public static String getItemModid(Item stack) {
        String id = GameData.getItemRegistry().getNameForObject(stack);
        return id == null ? "minecraft" : id.split(":",2)[0];
    }
}
