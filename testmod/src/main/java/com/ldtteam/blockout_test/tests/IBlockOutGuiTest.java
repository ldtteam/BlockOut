package com.ldtteam.blockout_test.tests;

import com.ldtteam.blockout.element.simple.Button;
import net.minecraft.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutGuiTest
{
    @NotNull
    String getTestName();

    void onTestButtonClicked(EntityPlayerMP entityPlayer, Button button, Button.ButtonClickedEventArgs eventArgs);
}
