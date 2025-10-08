package com.circulation.metal_revolution.proxy;

import codechicken.nei.api.API;
import codechicken.nei.recipe.RecipeInfo;
import com.circulation.metal_revolution.client.CookingTableOffsetPositioner;
import com.circulation.metal_revolution.client.CookingTableOverlayHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.client.GuiCookingTable;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft10;
import project.studio.manametalmod.nei.NEICooktable;
import project.studio.manametalmod.nei.NEIManaGravityWellHandler;
import project.studio.manametalmod.nei.NEIManaSewingMachine;
import project.studio.manametalmod.nei.NEIManaSpinningWheel;

import static project.studio.manametalmod.Lapuda.LapudaCore.LapudaFurnace;
import static project.studio.manametalmod.ManaMetalMod.BLOCKManaSF;
import static project.studio.manametalmod.ManaMetalMod.BLOCKTimeFurnace;
import static project.studio.manametalmod.dark_magic.DarkMagicCore.BlockTileEntityDarkSteelFurnaces;
import static project.studio.manametalmod.produce.mine.MineCore.MetalFurnace1Gold;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private final Reference2ReferenceMap<Class<?>, XYPair> map = new Reference2ReferenceOpenHashMap<>();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        map.defaultReturnValue(XYPair.of(5, 0));
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        map.put(NEIManaGravityWellHandler.class, XYPair.of(16, 0));
        map.put(NEIManaSewingMachine.class, XYPair.of(0, 0));
        map.put(NEIManaSpinningWheel.class, XYPair.of(0, 0));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        final var aClass = GuiCookingTable.class;
        final var xy = getXY(this.getClass());
        RecipeInfo.registerOverlayHandler(aClass, new CookingTableOverlayHandler(), NEICooktable.class.getName());
        RecipeInfo.registerGuiOverlay(aClass, NEICooktable.class.getName(), new CookingTableOffsetPositioner());

        addRecipeCatalyst("smelting", ItemCraft10.MetalFurnace1, ItemCraft10.MetalFurnace2, ItemCraft10.MetalFurnace3, ItemCraft10.MetalFurnace4, ItemCraft10.MetalFurnace5, ItemCraft10.MetalFurnace6, ItemCraft10.MetalFurnace7, MetalFurnace1Gold, BLOCKManaSF, BlockTileEntityDarkSteelFurnaces, LapudaFurnace, BLOCKTimeFurnace);
    }

    public void addRecipeCatalyst(String name, Object... objects) {
        for (var o : objects) {
            if (o instanceof Block block) {
                API.addRecipeCatalyst(new ItemStack(block), name);
            } else if (o instanceof Item item) {
                API.addRecipeCatalyst(new ItemStack(item), name);
            } else if (o instanceof ItemStack stack) {
                API.addRecipeCatalyst(stack, name);
            }
        }
    }

    @Override
    public XYPair getXY(Class<?> aClass) {
        return map.get(aClass);
    }

}
