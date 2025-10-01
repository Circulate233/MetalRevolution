package com.circulation.metal_revolution.utils;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.item.Item;
import project.studio.manametalmod.ManaMetalMod;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft10;

public class MInitUtil {

    public static final Reference2IntMap<Item> maneItemFuelMap = new Reference2IntOpenHashMap<>();

    public static void init() {
        ManeFuelInit();
    }

    private static void ManeFuelInit() {
        addManeFuel(ManaMetalMod.dustMana, 50);
        addManeFuel(ManaMetalMod.ingotMana, 450);
        addManeFuel(ManaMetalMod.dustSmallMana, 5);
        addManeFuel(Item.getItemFromBlock(ManaMetalMod.blockMana), 4050);
        addManeFuel(ItemCraft10.BucketMana, 400);
        addManeFuel(ItemCraft10.ItemMana, 100);
        maneItemFuelMap.defaultReturnValue(0);
    }

    private static void addManeFuel(Item item, int time) {
        maneItemFuelMap.put(item, time);
    }

}
