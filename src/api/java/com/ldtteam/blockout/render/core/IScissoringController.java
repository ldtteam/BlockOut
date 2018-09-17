package com.ldtteam.blockout.render.core;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.math.BoundingBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public interface IScissoringController
{
    /**
     * Puts a new Bounding box on the scissoring stack and starts a session.
     *
     * @param box The new box.
     */
    void push(@NotNull final BoundingBox box);

    /**
     * Performs a push with the {@link IUIElement#getAbsoluteBoundingBox()} of the given {@link IUIElement}.
     *
     * @param element The element to focus.
     */
    default void focus(@NotNull final IUIElement element)
    {
        push(element.getAbsoluteBoundingBox());
    }

    /**
     * Ends the current scissoring session.
     */
    void pop();

    /**
     * Gives the possibility to push
     */
    void setDebugDrawingEnabled(boolean enabled);
}
