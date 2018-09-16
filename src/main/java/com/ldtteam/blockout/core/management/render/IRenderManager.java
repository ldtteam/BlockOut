package com.ldtteam.blockout.core.management.render;

import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.gui.IBlockOutGui;
import com.ldtteam.blockout.render.core.IRenderingController;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public interface IRenderManager
{

    @SideOnly(Side.CLIENT)
    void drawBackground(@NotNull final IUIElement host);

    @SideOnly(Side.CLIENT)
    void drawForeground(@NotNull final IUIElement host);

    @NotNull
    @SideOnly(Side.CLIENT)
    IRenderingController getRenderingController();

    @NotNull
    @SideOnly(Side.CLIENT)
    IBlockOutGui getGui();

    @SideOnly(Side.CLIENT)
    void setGui(@NotNull final IBlockOutGui gui);
}
