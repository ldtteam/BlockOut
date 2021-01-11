package com.ldtteam.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.ldtteam.blockout.loader.object.ObjectUIElementBuilder;
import com.ldtteam.blockout.management.server.network.messages.OnElementUpdatedMessage;
import com.ldtteam.blockout.util.kryo.KryoUtil;
import net.minecraft.util.ResourceLocation;
import com.ldtteam.test.AbstractBlockOutTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertEquals;

public class SerializableObjectTest extends AbstractBlockOutTest
{

    @Test()
    public void attemptObjectUIElementDataSerializationTest() {
        final Kryo kryo = KryoUtil.createNewKryo();
        byte[] data;

        final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        final Output kryoOutput = new Output(arrayOutputStream);

        final OnElementUpdatedMessage message = new OnElementUpdatedMessage(new ObjectUIElementBuilder().withMetaData((builder) -> builder.withId("Test")).build());

        kryo.writeClassAndObject(kryoOutput, message);

        kryoOutput.close();
        data = arrayOutputStream.toByteArray();
        Assert.assertTrue("The given object could not be serialized. Data is empty.", data.length > 0);
    }

    @Test(expected = NotSerializableException.class)
    public void resourceLocation() throws Exception
    {
        final ResourceLocation location = new ResourceLocation("blockout", "test");

        attemptObjectTest(location);
    }

    private void attemptObjectTest(final Object o) throws Exception
    {
        byte[] data;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream))
            {
                objectOutputStream.writeObject(o);
                data = byteArrayOutputStream.toByteArray();
            }
        }

        Object result = null;

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data))
        {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream))
            {
                result = objectInputStream.readObject();
            }
        }

        assertEquals(o, result);
    }
}
