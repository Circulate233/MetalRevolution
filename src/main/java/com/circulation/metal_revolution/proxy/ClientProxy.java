package com.circulation.metal_revolution.proxy;

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
import project.studio.manametalmod.client.GuiCookingTable;
import project.studio.manametalmod.nei.NEICooktable;
import project.studio.manametalmod.nei.NEIManaGravityWellHandler;
import project.studio.manametalmod.nei.NEIManaSewingMachine;
import project.studio.manametalmod.nei.NEIManaSpinningWheel;

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
    }

    @Override
    public XYPair getXY(Class<?> aClass) {
        return map.get(aClass);
    }

}
