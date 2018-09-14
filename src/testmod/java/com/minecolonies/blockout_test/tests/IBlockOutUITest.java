package com.minecolonies.blockout_test.tests;

import com.minecolonies.blockout.element.simple.Button;
import net.minecraft.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutUITest
{

    @NotNull
    String getTestName();

    void onTestButtonClicked(EntityPlayerMP entityPlayer, Button button, Button.ButtonClickedEventArgs eventArgs);
}
