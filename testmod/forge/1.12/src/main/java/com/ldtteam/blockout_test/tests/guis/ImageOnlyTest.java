package com.ldtteam.blockout_test.tests.guis;

import com.ldtteam.blockout.BlockOutForge;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout_test.tests.IBlockOutGuiTest;
import com.ldtteam.jvoxelizer.launcher.forge_1_12.entity.living.player.PlayerEntity;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import net.minecraft.entity.player.EntityPlayerMP;
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
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        ProxyHolder.getInstance().getGuiController().openUI(PlayerEntity.fromForge(entityPlayer), iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                                                      .ofFile(IIdentifier.create(
                                                                                                                        "blockout_test:gui/image_only_test.json"))
                                                                                                      .usingDefaultData()
                                                                                                      .withDefaultItemHandlerManager()
                                                                                                                      .forEntity(PlayerEntity.fromForge(entityPlayer)));
    }
}
