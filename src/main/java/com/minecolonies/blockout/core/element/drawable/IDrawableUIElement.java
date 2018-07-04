package com.minecolonies.blockout.core.element.drawable;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.render.core.IRenderingController;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public interface IDrawableUIElement extends IUIElement
{

    @SideOnly(Side.CLIENT)
    void drawBackground(@NotNull final IRenderingController controller);

    @SideOnly(Side.CLIENT)
    void drawForeground(@NotNull final IRenderingController controller);
}
