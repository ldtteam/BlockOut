package com.minecolonies.blockout.gui;

import com.minecolonies.blockout.connector.core.IGuiKey;
import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public interface IBlockOutGui
{
    @NotNull
    IUIElementHost getRoot();

    void setRoot(@NotNull IUIElementHost root);

    @NotNull
    IGuiKey getKey();
}
