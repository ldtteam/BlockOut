package com.minecolonies.blockout.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.minecolonies.blockout.network.message.core.IBlockOutNetworkMessage;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.ByteArrayOutputStream;

public class BlockOutNetworkMessageWrapper implements IMessage, IMessageHandler<BlockOutNetworkMessageWrapper, BlockOutNetworkMessageWrapper>
{

    private final        Kryo             KRYO             = new Kryo();
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

            final Input kryoInput = new Input(data);
            message = (IBlockOutNetworkMessage) KRYO.readClassAndObject(kryoInput);
            loaded = true;
        }
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        byte[] data;

        final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        final Output kryoOutput = new Output(arrayOutputStream);

        KRYO.writeClassAndObject(kryoOutput, message);

        kryoOutput.close();
        data = arrayOutputStream.toByteArray();

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
