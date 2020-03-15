package com.ldtteam.blockout.element.tooltip;

import com.ldtteam.blockout.proxy.IProxy;
import com.ldtteam.blockout.tooltip.ITooltipDelayHandler;

public class FixedTooltipDelayHandler implements ITooltipDelayHandler {

    private int currentDelay = 40;
    private long lastResetOn = -1;

    @Override
    public boolean shouldDisplay() {
        return IProxy.getInstance().getTickManager().getTickCount() - lastResetOn > currentDelay;
    }

    @Override
    public void reset() {
        this.lastResetOn = IProxy.getInstance().getTickManager().getTickCount();
    }

    @Override
    public int getCurrentDelay() {
        return currentDelay;
    }
}
