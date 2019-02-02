package com.ldtteam.blockout.management.input.client;

import com.ldtteam.blockout.management.input.IInputManager;

public interface IClientSideScrollManager
{
    boolean onMouseWheel(final int localX, final int localY, final int deltaWheel);
}
