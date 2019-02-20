package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class RangeSelectorTest implements IBlockOutUITest
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
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                      .ofFile(new ResourceLocation("blockout_test:guitemp/range_selector_test.json"))
                                                                                                      .usingDefaultData()
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                      .forEntity(entityPlayer));
    }
}
