package com.ldtteam.network;

import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.test.AbstractBlockOutTest;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertEquals;

public class SerializableObjectTest extends AbstractBlockOutTest
{

    @Test(expected = NotSerializableException.class)
    public void resourceLocation() throws Exception
    {
        final IIdentifier location = IIdentifier.create("blockout", "test");

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
