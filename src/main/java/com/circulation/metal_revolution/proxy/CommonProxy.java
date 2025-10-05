package com.circulation.metal_revolution.proxy;

import com.circulation.metal_revolution.utils.MInitUtil;
import com.github.bsideup.jabel.Desugar;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        MInitUtil.init();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public XYPair getXY(Class<?> aClass){
        return null;
    }

    @Desugar
    public record XYPair(int x, int y){

        public static XYPair of(int x, int y){
            return new XYPair(x,y);
        }
    }
}
