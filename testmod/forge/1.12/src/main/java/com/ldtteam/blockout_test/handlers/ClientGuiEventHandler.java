package com.ldtteam.blockout_test.handlers;

import com.ldtteam.blockout.BlockOutForge;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "blockout_test", value = Side.CLIENT)
public class ClientGuiEventHandler
{

    @SubscribeEvent
    public static void onGuiScreenInitGui(final GuiScreenEvent.InitGuiEvent event)
    {
        if (event.getGui() instanceof GuiCreateWorld)
        {
            GuiCreateWorld gui = (GuiCreateWorld) event.getGui();
            onCreateWorldGui(gui);
        }
    }

    private static void onCreateWorldGui(@NotNull final GuiCreateWorld worldCreationGui)
    {
        worldCreationGui.buttonList.add(new GuiButton(1000, worldCreationGui.width / 2 - 200, 60, 90, 20, "Open BO Test"));
    }

    @SubscribeEvent
    public static void onGuiScreenActionPerformed(final GuiScreenEvent.ActionPerformedEvent event)
    {
        if (event.getGui() instanceof GuiCreateWorld && event.getButton().id == 1000)
        {
            onBOTestButtonPressed();
        }
    }

    private static void onBOTestButtonPressed()
    {
        ProxyHolder.getInstance().getClientSideOnlyGuiController().openUI(
          PlayerEntity.fromForge(Minecraft.getMinecraft().player),
          iGuiKeyBuilder -> iGuiKeyBuilder.ofFile(IIdentifier.create("blockout_test:gui/button_click_test.json"))
                              .usingData(iBlockOutGuiConstructionDataBuilder -> iBlockOutGuiConstructionDataBuilder
                                                                                  .withControl(
                                                                                    "test_click",
                                                                                    Button.ButtonConstructionDataBuilder.class,
                                                                                    buttonConstructionDataBuilder -> {
                                                                                        buttonConstructionDataBuilder
                                                                                          .withClickedEventHandler(
                                                                                            (button, eventArgs) -> Log.getLogger()
                                                                                                                     .error("Button pressed! State: " + eventArgs.getButton()
                                                                                                                              + " Initial Press: " + eventArgs.isStart())
                                                                                          );
                                                                                    }
                                                                                  ))
                              .withDefaultItemHandlerManager()
                              .forClientSideOnly()
        );
    }
}
