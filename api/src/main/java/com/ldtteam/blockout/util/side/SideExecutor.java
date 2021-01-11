package com.ldtteam.blockout.util.side;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class SideExecutor {

    private SideExecutor() {
        throw new IllegalStateException("Tried to initialize: SideExecutor but this is a Utility class.");
    }

    /**
     * Run the callable in the supplier only on the specified {@link net.minecraftforge.fml.LogicalSide}
     *
     * @param side The side to run on
     * @param toRun A supplier of the callable to run (Supplier wrapper to ensure classloading only on the appropriate side)
     * @param <T> The return type from the callable
     * @return The callable's result
     */
    public static <T> T callWhenOn(LogicalSide side, Supplier<Callable<T>> toRun) {
        if (side == EffectiveSide.get()) {
            try
            {
                return toRun.get().call();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static void runWhenOn(LogicalSide side, Supplier<Runnable> toRun) {
        if (side == EffectiveSide.get()) {
            toRun.get().run();
        }
    }
    public static <T> T runForSide(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        switch (EffectiveSide.get())
        {
            case CLIENT:
                return clientTarget.get().get();
            case SERVER:
                return serverTarget.get().get();
            default:
                throw new IllegalArgumentException("UNSIDED?");
        }
    }
}
