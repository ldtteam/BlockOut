package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
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
