package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class ImageOnlyTest implements IBlockOutGuiTest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.WHITE + "Image Only" + TextFormatting.RESET;
    }

    @Override
    public void onTestButtonClicked(
            final ServerPlayerEntity entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        ProxyHolder.getInstance().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .from(new ResourceLocation(
                                                                                                                        "blockout_test:gui/image_only_test.json"))
                                                                                                      .usingDefaultData()
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                                      .forEntity(entityPlayer));
    }
}
