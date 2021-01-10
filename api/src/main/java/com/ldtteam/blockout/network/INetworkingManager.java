package com.ldtteam.blockout.network;

import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.network.message.core.IBlockOutServerToClientMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public interface INetworkingManager {
    /**
     * Send this message to everyone.
     *
     * @param message The message to send
     */
    void sendToAll(IBlockOutServerToClientMessage message);

    /**
     * Send this message to the specified player.
     *
     * @param message The message to send
     * @param uuid    The player to send it to
     */
    void sendTo(IBlockOutServerToClientMessage message, UUID uuid);

    /**
     * Send this message to the specified player.
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    void sendTo(IBlockOutServerToClientMessage message, ServerPlayerEntity player);

    /**
     * Send this message to everyone within a certain range of a point.
     *
     * @param message The message to send
     * @param point   The {
     *                if (network == null) return;@link NetworkRegistry.TargetPoint} around which to send
     */
    void sendToAllAround(IBlockOutServerToClientMessage message, PacketDistributor.TargetPoint point);

    /**
     * Send this message to everyone within the supplied dimension.
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    void sendToDimension(IBlockOutServerToClientMessage message, RegistryKey<World> dimensionId);

    /**
     * Send this message to the server.
     *
     * @param message The message to send
     */
    void sendToServer(IBlockOutClientToServerMessage message);
}
