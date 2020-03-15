package com.ldtteam.blockout.compat;

public interface ITickManager {
    /**
     * Get the current tick count.
     *
     * @return The tick count.
     */
    long getTickCount();

    void onTick();
}
