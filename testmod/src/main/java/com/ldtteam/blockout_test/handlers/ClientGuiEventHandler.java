package com.ldtteam.blockout_test.handlers;

import com.ldtteam.blockout.element.simple.Button.ButtonConstructionDataBuilder;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "blockout_test", value = Dist.CLIENT)
public class ClientGuiEventHandler
{

    @SubscribeEvent
    public static void onGuiScreenInitGui(final GuiScreenEvent.InitGuiEvent event)
    {
        if (event.getGui() instanceof CreateWorldScreen)
        {
            CreateWorldScreen gui = (CreateWorldScreen) event.getGui();
            onCreateWorldGui(gui);
        }
    }

    private static void onCreateWorldGui(@NotNull final CreateWorldScreen worldCreationGui)
    {
        // worldCreationGui.addButton(new Button(worldCreationGui.width / 2 - 200, 60, 90, 20, new StringTextComponent("Open BO Test"), button -> onBOTestButtonPressed()));
    }

    private static void onBOTestButtonPressed()
    {
        ProxyHolder.getInstance()
            .getClientSideOnlyGuiController()
            .openUI(Minecraft.getInstance().player,
                iGuiKeyBuilder -> iGuiKeyBuilder.from(new ResourceLocation("blockout_test:gui/button_click_test.json"))
                    .usingData(guiBuilder -> guiBuilder
                        .withControl("test_click", ButtonConstructionDataBuilder.class, buttonBuilder -> {
                            buttonBuilder.withClickedEventHandler((button, eventArgs) -> Log.getLogger()
                                .error("Button pressed! State: " + eventArgs.getButton() + " Initial Press: " + eventArgs.isStart()));
                        }))
                    .withDefaultItemHandlerManager()
                    .forClientSideOnly());
    }
}
