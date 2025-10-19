package com.circulation.metal_revolution.proxy;

import com.circulation.metal_revolution.common.MRTileEntityCheeseMaker;
import com.circulation.metal_revolution.common.MRTileEntityCrystalPillars;
import com.circulation.metal_revolution.common.MRTileEntityGilded;
import com.circulation.metal_revolution.common.MRTileEntityManaMetalInjection;
import com.circulation.metal_revolution.utils.MInitUtil;
import com.github.bsideup.jabel.Desugar;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;

public class CommonProxy {

    public static void registerTileEntity(Class<? extends TileEntity> aCalss) {
        GameRegistry.registerTileEntity(aCalss, aCalss.getSimpleName().toLowerCase());
    }

    public void preInit(FMLPreInitializationEvent event) {
        registerTileEntity(MRTileEntityManaMetalInjection.class);
        registerTileEntity(MRTileEntityCheeseMaker.class);
        registerTileEntity(MRTileEntityGilded.class);
        registerTileEntity(MRTileEntityCrystalPillars.class);
    }

    public void init(FMLInitializationEvent event) {
        MInitUtil.init();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public XYPair getXY(Class<?> aClass) {
        return null;
    }

    @Desugar
    public record XYPair(int x, int y) {

        public static XYPair of(int x, int y) {
            return new XYPair(x, y);
        }
    }
}
