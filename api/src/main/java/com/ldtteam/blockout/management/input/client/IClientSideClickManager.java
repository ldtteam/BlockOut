package com.ldtteam.blockout.management.input.client;

import com.ldtteam.blockout.management.input.IInputManager;
import com.ldtteam.blockout.util.mouse.MouseButton;

public interface IClientSideClickManager
{

    boolean onMouseClickBegin(final int localX, final int localY, MouseButton button);

    boolean onMouseClickEnd(final int localX, final int localY, MouseButton button);

    boolean onMouseClickMove(final int localX, final int localY, MouseButton button, final float timeElapsed);
}
