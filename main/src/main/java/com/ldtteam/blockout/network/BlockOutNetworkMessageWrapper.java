package com.ldtteam.blockout.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ldtteam.blockout.network.message.core.IBlockOutNetworkMessage;
import com.ldtteam.blockout.util.kryo.KryoUtil;
import com.ldtteam.jvoxelizer.networking.messaging.IMessage;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageHandler;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;

public class BlockOutNetworkMessageWrapper implements IMessage, IMessageHandler<BlockOutNetworkMessageWrapper, BlockOutNetworkMessageWrapper>
{

    private final Kryo                    KRYO   = KryoUtil.createNewKryo();
    private       boolean                 loaded = false;
    private       IBlockOutNetworkMessage message;

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
    public BlockOutNetworkMessageWrapper onMessage(final BlockOutNetworkMessageWrapper message, final IMessageContext ctx)
    {
        if (message.loaded)
        {
            message.message.onMessage(ctx);
        }

        return null;
    }
}
