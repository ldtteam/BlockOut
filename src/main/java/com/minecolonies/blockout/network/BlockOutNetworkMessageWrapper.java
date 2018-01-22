package com.minecolonies.blockout.network;

import com.minecolonies.blockout.network.message.core.IBlockOutNetworkMessage;
import com.minecolonies.blockout.util.Log;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.*;

class BlockOutNetworkMessageWrapper implements IMessage, IMessageHandler<BlockOutNetworkMessageWrapper, BlockOutNetworkMessageWrapper>
{
    private boolean loaded = false;
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

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data))
            {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream))
                {
                    message = (IBlockOutNetworkMessage) objectInputStream.readObject();
                    loaded = true;
                }
            }
            catch (ClassCastException e)
            {
                Log.getLogger().error("Received a message object that is not a IBlockOutNetworkMessage.", e);
                message = null;
                loaded = false;
            }
            catch (ClassNotFoundException e)
            {
                Log.getLogger().error("Received a message object that contained an unknown class.", e);
                message = null;
                loaded = false;
            }
            catch (IOException e)
            {
                Log.getLogger().error("Received a message object that that could not be read.", e);
                message = null;
                loaded = false;
            }
        }
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        byte[] data;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream))
            {
                objectOutputStream.writeObject(message);
                data = byteArrayOutputStream.toByteArray();
            }
        }
        catch (IOException e)
        {
            Log.getLogger().error("Failed to write: " + this + "to network. Message might not be send properly.", e);
            data = new byte[0];
        }

        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

    @Override
    public BlockOutNetworkMessageWrapper onMessage(final BlockOutNetworkMessageWrapper message, final MessageContext ctx)
    {
        if (!message.loaded)
        {
            return null;
        }

        final IBlockOutNetworkMessage result = message.message.onMessage(ctx);

        if (result != null)
        {
            return new BlockOutNetworkMessageWrapper(result);
        }

        return null;
    }
}
