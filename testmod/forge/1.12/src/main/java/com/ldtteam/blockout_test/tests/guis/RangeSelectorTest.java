package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.BlockOutForge;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class RangeSelectorTest implements IBlockOutGuiTest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.DARK_AQUA + "Range Selector" + TextFormatting.RESET;
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        ProxyHolder.getInstance().getGuiController().openUI(PlayerEntity.fromForge(entityPlayer), iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .ofFile(new ResourceLocation(
                                                                                                                        "blockout_test:gui/range_selector_test.json"))
                                                                                                      .usingDefaultData()
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                                      .forEntity(PlayerEntity.fromForge(entityPlayer)));
    }
}
