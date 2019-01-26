package com.ldtteam.blockout.util;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Helper class to handle sided ness of Blockout.
 */
public final class SideHelper
{

    private SideHelper()
    {
        throw new IllegalArgumentException("Utility class");
    }

    /**
     * Method used to specific code on the server and the client side.
     *
     * @param clientSide The code that needs to be run on the client side.
     * @param serverSide The code that needs to be run on the server side.
     */
    public static void on(@NotNull final Runnable clientSide, @NotNull final Runnable serverSide)
    {
        onClient(clientSide);
        onServer(serverSide);
    }

    /**
     * Method used to only run code on the client.
     *
     * @param runnable The code that needs to be run on the client side only.
     */
    public static void onClient(@NotNull final Runnable runnable)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            runnable.run();
        }
    }

    /**
     * Method used to only run code on the server.
     *
     * @param runnable The code that needs to be run on the server side only.
     */
    public static void onServer(@NotNull final Runnable runnable)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            runnable.run();
        }
    }

    /**
     * A {@link Supplier} version of {@link #on(Runnable, Runnable)}.
     *
     * @param clientSide The {@link Supplier} that executes on the client side.
     * @param serverSide The {@link Supplier} that executes on the server side.
     * @param <T>        The type that is produced.
     * @return The value from the client or the server side.
     */
    public static <T> T on(@NotNull final Supplier<T> clientSide, @NotNull final Supplier<T> serverSide)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            return clientSide.get();
        }

        return serverSide.get();
    }
}
