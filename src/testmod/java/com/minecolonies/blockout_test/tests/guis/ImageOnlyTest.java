package com.minecolonies.blockout_test.tests.guis;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.element.simple.Button;
import com.minecolonies.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class ImageOnlyTest implements IBlockOutUITest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.WHITE + "Image Only" + TextFormatting.RESET;
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        BlockOut.getBlockOut().getProxy().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                  .ofFile(new ResourceLocation("blockout_test:gui/image_only_test.json"))
                                                                                                  .usingDefaultData()
                                                                                                  .withDefaultItemHandlerManager()
                                                                                                  .forEntity(entityPlayer));
    }
}
