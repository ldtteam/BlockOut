package com.ldtteam.blockout.gui;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.IUIElementHost;
import org.jetbrains.annotations.NotNull;

public interface IBlockOutGui
{
    @NotNull
    IUIElementHost getRoot();

    void setRoot(@NotNull IUIElementHost root);

    @NotNull
    IGuiKey getKey();
}
