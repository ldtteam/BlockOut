package com.ldtteam.blockout.tooltip;

public interface ITooltipDelayHandler {

    boolean shouldDisplay();

    void onRenderTick();

    void reset();

    int getCurrentDelay();
}
