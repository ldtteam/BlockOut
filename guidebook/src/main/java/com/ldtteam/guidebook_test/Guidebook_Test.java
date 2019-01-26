package com.ldtteam.guidebook_test;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Guidebook_Test.MODID, name = Guidebook_Test.NAME, version = Guidebook_Test.VERSION)
public class Guidebook_Test
{
    public static final String MODID   = "guidebook_test";
    public static final String NAME    = "Guidebook Test";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }
}
