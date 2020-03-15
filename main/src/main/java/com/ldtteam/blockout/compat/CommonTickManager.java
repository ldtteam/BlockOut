package com.ldtteam.blockout.compat;

/**
 * Manages the client tick systems of BlockOut.
 */
public class CommonTickManager implements ITickManager {
    private        long              tickCount   = 0;

    /**
     * Get the current tick count.
     *
     * @return The tick count.
     */
    @Override
    public long getTickCount()
    {
        return tickCount;
    }

    /**
     * Called by the {@link UpdateHandler} to increment the tick count on client tick.
     */
    @Override
    public void onTick()
    {
        tickCount++;
    }
}
