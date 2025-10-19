package com.circulation.metal_revolution;

import com.circulation.metal_revolution.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = MetalRevolution.MOD_ID, name = "Metal Revolution", version = MetalRevolution.VERSION, acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:manametalmod@[7.4.5,);" +
        "required-after:unimixins@[0.1.23,);" +
        "required-after:gtnhlib@[0.6.39,);"
)
public class MetalRevolution {

    public static final String MOD_ID = "metal_revolution";
    public static final String VERSION = BuildConfig.VERSION;
    public static final String CLIENT_PROXY = "com.circulation.metal_revolution.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.circulation.metal_revolution.proxy.CommonProxy";
    @Mod.Instance(MOD_ID)
    public static MetalRevolution instance = new MetalRevolution();
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static Logger logger = LogManager.getLogger(MOD_ID);

    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

    }

}
