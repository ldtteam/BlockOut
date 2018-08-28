package com.minecolonies.blockout.core.management.render;

import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.render.core.IRenderingController;
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
}
