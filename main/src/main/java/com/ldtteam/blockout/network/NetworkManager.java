package com.ldtteam.blockout.network;

import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class NetworkManager
{
    private static SimpleNetworkWrapper network;

    public static void init()
    {
        network = new SimpleNetworkWrapper(Constants.NETWORK_NAME);

        network.registerMessage(BlockOutNetworkMessageWrapper.class, BlockOutNetworkMessageWrapper.class, 0, Side.SERVER);
        network.registerMessage(BlockOutNetworkMessageWrapper.class, BlockOutNetworkMessageWrapper.class, 1, Side.CLIENT);
    }

    /**
     * Send this message to everyone.
     *
     * @param message The message to send
     */
    public static void sendToAll(IBlockOutServerToClientMessage message)
    {
        if (network == null)
        {
            return;
        }

        network.sendToAll(new BlockOutNetworkMessageWrapper(message));
    }

    /**
     * Send this message to the specified player.
     *
     * @param message The message to send
     * @param uuid    The player to send it to
     */
    public static void sendTo(IBlockOutServerToClientMessage message, UUID uuid)
    {
        sendTo(message, FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid));
    }

    /**
     * Send this message to the specified player.
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public static void sendTo(IBlockOutServerToClientMessage message, EntityPlayerMP player)
    {
        if (network == null)
        {
            return;
        }

        network.sendTo(new BlockOutNetworkMessageWrapper(message), player);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     *
     * @param message The message to send
     * @param point   The {
     *                if (network == null) return;@link NetworkRegistry.TargetPoint} around which to send
     */
    public static void sendToAllAround(IBlockOutServerToClientMessage message, NetworkRegistry.TargetPoint point)
    {
        if (network == null)
        {
            return;
        }

        network.sendToAllAround(new BlockOutNetworkMessageWrapper(message), point);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public static void sendToDimension(IBlockOutServerToClientMessage message, int dimensionId)
    {
        if (network == null)
        {
            return;
        }

        network.sendToDimension(new BlockOutNetworkMessageWrapper(message), dimensionId);
    }

    /**
     * Send this message to the server.
     *
     * @param message The message to send
     */
    public static void sendToServer(IBlockOutClientToServerMessage message)
    {
        if (network == null)
        {
            return;
        }

        network.sendToServer(new BlockOutNetworkMessageWrapper(message));
    }
}
