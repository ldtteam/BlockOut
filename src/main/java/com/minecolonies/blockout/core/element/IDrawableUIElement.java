package com.minecolonies.blockout.core.element;

import com.minecolonies.blockout.render.core.IRenderingController;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public interface IDrawableUIElement
{

    void drawBackground(@NotNull final IRenderingController controller);

    void drawForeground(@NotNull final IRenderingController controller);
}
