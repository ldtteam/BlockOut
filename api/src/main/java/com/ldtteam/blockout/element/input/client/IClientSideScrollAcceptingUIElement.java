package com.ldtteam.blockout.element.input.client;

import com.ldtteam.blockout.element.IUIElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IClientSideScrollAcceptingUIElement extends IUIElement
{
    @SideOnly(Side.CLIENT)
    boolean canAcceptMouseInputClient(final int localX, final int localY, final int deltaWheel);

    @SideOnly(Side.CLIENT)
    boolean onMouseScrollClient(final int localX, final int localY, final int deltaWheel);
}
