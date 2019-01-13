package com.ldtteam.blockout.core.element.input;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.util.mouse.MouseButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IClientSideClickAcceptingUIElement extends IUIElement
{

    @SideOnly(Side.CLIENT)
    boolean canAcceptMouseInputClient(final int localX, final int localY, final MouseButton button);

    @SideOnly(Side.CLIENT)
    default boolean onMouseClickBeginClient(final int localX, final int localY, final MouseButton button)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    default boolean onMouseClickEndClient(final int localX, final int localY, final MouseButton button)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    default boolean onMouseClickMoveClient(final int localX, final int localY, final MouseButton button, final float timeElapsed)
    {
        return false;
    }
}
