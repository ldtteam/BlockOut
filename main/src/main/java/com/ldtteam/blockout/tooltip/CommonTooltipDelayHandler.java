package com.ldtteam.blockout.tooltip;

import com.ldtteam.blockout.proxy.IProxy;

public class CommonTooltipDelayHandler implements ITooltipDelayHandler {

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
