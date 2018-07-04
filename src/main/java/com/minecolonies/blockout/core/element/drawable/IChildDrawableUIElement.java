package com.minecolonies.blockout.core.element.drawable;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import org.jetbrains.annotations.NotNull;

public interface IChildDrawableUIElement extends IUIElementHost
{
    /**
     * Called by the rendering manager before the drawing of the background of our children starts.
     *
     * @param manager The manager.
     */
    default void preBackgroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        //Noop
    }

    /**
     * Called by the rendering manager to draw all children.
     *
     * @param manager The manager.
     */
    default void backgroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        values().forEach(manager::drawBackground);
    }

    /**
     * Called by the rendering manager after the drawing of the background of our children ended.
     *
     * @param manager The manager.
     */
    default void postBackgroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        //Noop
    }

    /**
     * Called by the rendering manager before the drawing of the foreground of our children starts.
     *
     * @param manager The manager.
     */
    default void preForegroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        //Noop
    }

    /**
     * Called by the rendering manager to draw all children.
     *
     * @param manager The manager.
     */
    default void foregroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        values().forEach(manager::drawForeground);
    }

    /**
     * Called by the rendering manager after the drawing of the foreground of our children ended.
     *
     * @param manager The manager.
     */
    default void postForegroundDrawOfChildren(@NotNull final IRenderManager manager)
    {
        //Noop
    }
}
