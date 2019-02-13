package com.ldtteam.blockout.element.input.client;

import com.ldtteam.blockout.element.IUIElement;

public interface IClientSideScrollAcceptingUIElement extends IUIElement
{
    boolean canAcceptMouseInputClient(final int localX, final int localY, final int deltaWheel);

    boolean onMouseScrollClient(final int localX, final int localY, final int deltaWheel);
}
