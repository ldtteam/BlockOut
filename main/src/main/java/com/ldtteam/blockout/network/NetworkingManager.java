package com.ldtteam.blockout.network;

import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class NetworkingManager implements INetworkingManager {
    private static final String LATEST_PROTO_VER = "1.0";
    private static final String ACCEPTED_PROTO_VERS = LATEST_PROTO_VER;

    private static final NetworkingManager INSTANCE = new NetworkingManager();

    public static NetworkingManager getInstance() {
        return INSTANCE;
    }

    private SimpleChannel network;

    private NetworkingManager() {
    }

    public void init()
    {
        network = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.NETWORK_NAME), () -> LATEST_PROTO_VER, ACCEPTED_PROTO_VERS::equals, ACCEPTED_PROTO_VERS::equals);

        network.registerMessage(0,
          BlockOutNetworkMessageWrapper.class,
          BlockOutNetworkMessageWrapper::toBytes,
          BlockOutNetworkMessageWrapper::new,
          (msg, context) -> {
            msg.onArrived(context.get());
            context.get().setPacketHandled(true);
          });
    }

    /**
     * Send this message to everyone.
     *
     * @param message The message to send
     */
    @Override
    public void sendToAll(IBlockOutServerToClientMessage message)
    {
        if (network == null)
        {
            return;
        }

        network.send(PacketDistributor.ALL.with(() -> null), new BlockOutNetworkMessageWrapper(message));
    }

    /**
     * Send this message to the specified player.
     *
     * @param message The message to send
     * @param uuid    The player to send it to
     */
    @Override
    public void sendTo(IBlockOutServerToClientMessage message, UUID uuid)
    {
        sendTo(message, ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid));
    }

    /**
     * Send this message to the specified player.
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    @Override
    public void sendTo(IBlockOutServerToClientMessage message, ServerPlayerEntity player)
    {
        if (network == null)
        {
            return;
        }

        network.send(PacketDistributor.PLAYER.with(() -> player), new BlockOutNetworkMessageWrapper(message));
    }

    /**
     * Send this message to everyone within a certain range of a point.
     *
     * @param message The message to send
     * @param point   The {
     *                if (network == null) return;@link NetworkRegistry.TargetPoint} around which to send
     */
    @Override
    public void sendToAllAround(IBlockOutServerToClientMessage message, PacketDistributor.TargetPoint point)
    {
        if (network == null)
        {
            return;
        }

        network.send(PacketDistributor.NEAR.with(() -> point), new BlockOutNetworkMessageWrapper(message));
    }

    /**
     * Send this message to everyone within the supplied dimension.
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    @Override
    public void sendToDimension(IBlockOutServerToClientMessage message, RegistryKey<World> dimensionId)
    {
        if (network == null)
        {
            return;
        }

        network.send(PacketDistributor.DIMENSION.with(() -> dimensionId), new BlockOutNetworkMessageWrapper(message));
    }

    /**
     * Send this message to the server.
     *
     * @param message The message to send
     */
    @Override
    public void sendToServer(IBlockOutClientToServerMessage message)
    {
        if (network == null)
        {
            return;
        }

        network.sendToServer(new BlockOutNetworkMessageWrapper(message));
    }
}
