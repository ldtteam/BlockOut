package com.ldtteam.blockout.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ldtteam.blockout.network.message.core.IBlockOutNetworkMessage;
import com.ldtteam.blockout.util.kryo.KryoUtil;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.ByteArrayOutputStream;

public class BlockOutNetworkMessageWrapper
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

    public BlockOutNetworkMessageWrapper(final ByteBuf byteBuf)
    {
        this.fromBytes(byteBuf);
        byteBuf.release();
    }

    public void fromBytes(final ByteBuf buf)
    {
        final ByteBuf localBuffer = buf.retain();

        final int length = localBuffer.readInt();

        if (length > 0)
        {
            byte[] data = new byte[localBuffer.readableBytes()];
            buf.readBytes(data);

            final Input kryoInput = new Input(data);
            final Object networkObject = KRYO.readClassAndObject(kryoInput);
            message = (IBlockOutNetworkMessage) networkObject;
            loaded = true;
        }
    }

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

    public void onArrived(final NetworkEvent.Context iMessageContext)
    {
        if (loaded)
        {
            message.onMessage(iMessageContext);
        }
    }
}
