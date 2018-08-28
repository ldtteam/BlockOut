package com.minecolonies.blockout.network;

import com.minecolonies.blockout.network.message.core.IBlockOutNetworkMessage;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.nustaq.serialization.FSTConfiguration;

public class BlockOutNetworkMessageWrapper implements IMessage, IMessageHandler<BlockOutNetworkMessageWrapper, BlockOutNetworkMessageWrapper>
{
    private final static FSTConfiguration CONST_SERIALIZER = FSTConfiguration.createDefaultConfiguration();
    private              boolean          loaded           = false;
    private IBlockOutNetworkMessage message;

    public BlockOutNetworkMessageWrapper()
    {
    }

    public BlockOutNetworkMessageWrapper(final IBlockOutNetworkMessage message)
    {
        this.message = message;
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        final ByteBuf localBuffer = buf.retain();

        final int length = localBuffer.readInt();

        if (length > 0)
        {
            byte[] data = new byte[localBuffer.readableBytes()];
            buf.readBytes(data);

            message = (IBlockOutNetworkMessage) CONST_SERIALIZER.asObject(data);
            loaded = true;
        }
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        byte[] data;

        data = CONST_SERIALIZER.asByteArray(message);

        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

    @Override
    public BlockOutNetworkMessageWrapper onMessage(final BlockOutNetworkMessageWrapper message, final MessageContext ctx)
    {
        if (message.loaded)
        {
            message.message.onMessage(ctx);
        }

        return null;
    }
}
