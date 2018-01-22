package com.minecolonies.blockout.render.core;

import com.minecolonies.blockout.util.math.BoundingBox;
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
     * Ends the current scissoring session.
     */
    void pop();

    /**
     * Gives the possibility to push
     */
    void setDebugDrawingEnabled(boolean enabled);
}
