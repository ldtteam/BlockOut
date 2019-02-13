package com.ldtteam.blockout.management.render;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.gui.IBlockOutGui;
import com.ldtteam.blockout.render.core.IRenderingController;
import com.ldtteam.blockout.util.math.Vector2d;
import org.jetbrains.annotations.NotNull;

public interface IRenderManager
{

    void drawBackground(@NotNull final IUIElement host);

    void drawForeground(@NotNull final IUIElement host);

    @NotNull
    IRenderingController getRenderingController();

    @NotNull
    IBlockOutGui getGui();

    void setGui(@NotNull final IBlockOutGui gui);

    Vector2d getRenderingScalingFactor();

    void setRenderingScalingFactor(@NotNull final Vector2d scalingFactor);
}
