package com.ldtteam.guidebook_test;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import com.ldtteam.guidebook_test.items.ItemGuidebook;

import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(Side.CLIENT)
public class EventHandler
{
    public static Item guidebook;

    @SubscribeEvent
    public static void registerItems(@NotNull final RegistryEvent.Register<Item> event)
    {
        guidebook = new ItemGuidebook();
        event.getRegistry().register(guidebook);
    }

    @SubscribeEvent
    public static void registerModels(@NotNull final ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(guidebook, 0, new ModelResourceLocation(guidebook.getRegistryName(), "inventory"));
    }
}