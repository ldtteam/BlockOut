package com.ldtteam.blockout.element.drawable;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.render.core.IRenderingController;
import org.jetbrains.annotations.NotNull;

public interface IDrawableUIElement extends IUIElement
{
    void drawBackground(@NotNull final IRenderingController controller);

    void drawForeground(@NotNull final IRenderingController controller);
}
