package com.ldtteam.blockout.tooltip;

public interface ITooltipDelayHandler {

    boolean shouldDisplay();

    void reset();

    int getCurrentDelay();
}
