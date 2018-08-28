package com.minecolonies.blockout.core.management.input;

import com.minecolonies.blockout.util.mouse.MouseButton;

public interface IClickManager extends IInputManager
{

    void onMouseClickBegin(final int localX, final int localY, MouseButton button);

    void onMouseClickEnd(final int localX, final int localY, MouseButton button);

    void onMouseClickMove(final int localX, final int localY, MouseButton button, final float timeElapsed);
}
