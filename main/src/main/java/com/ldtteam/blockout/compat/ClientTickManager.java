package com.ldtteam.blockout.compat;

/**
 * Manages the client tick systems of BlockOut.
 */
public class ClientTickManager
{
    private static ClientTickManager ourInstance = new ClientTickManager();
    private        long              tickCount   = 0;

    private ClientTickManager()
    {
    }

    /**
     * The instance of the ClientTickManager.
     *
     * @return The instance.
     */
    public static ClientTickManager getInstance()
    {
        return ourInstance;
    }

    /**
     * Get the current tick count.
     *
     * @return The tick count.
     */
    public long getTickCount()
    {
        return tickCount;
    }

    /**
     * Called by the {@link UpdateHandler} to increment the tick count on client tick.
     */
    public void onClientTick()
    {
        tickCount++;
    }
}
