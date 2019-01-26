package com.ldtteam.blockout.management.input;

public interface IScrollManager extends IInputManager
{
    void onMouseWheel(final int localX, final int localY, final int deltaWheel);
}
