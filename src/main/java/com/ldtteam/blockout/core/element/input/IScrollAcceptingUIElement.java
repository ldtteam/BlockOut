package com.ldtteam.blockout.core.element.input;

import com.ldtteam.blockout.core.element.IUIElement;

public interface IScrollAcceptingUIElement extends IUIElement
{

    boolean canAcceptMouseInput(final int localX, final int localY, final int deltaWheel);

    void onMouseScroll(final int localX, final int localY, final int deltaWheel);
}
