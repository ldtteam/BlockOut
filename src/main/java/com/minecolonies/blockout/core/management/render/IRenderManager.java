package com.minecolonies.blockout.core.management.render;

import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public interface IRenderManager
{

    @SideOnly(Side.CLIENT)
    void drawBackground(@NotNull final IDrawableUIElement element);

    @SideOnly(Side.CLIENT)
    void drawBackground(@NotNull final IUIElementHost host);

    @SideOnly(Side.CLIENT)
    void drawForeground(@NotNull final IDrawableUIElement element);

    @SideOnly(Side.CLIENT)
    void drawForeground(@NotNull final IUIElementHost host);
}
