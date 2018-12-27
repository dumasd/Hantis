package com.thinkerwolf.hantis.cache.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.KryoObjectInput;
import com.esotericsoftware.kryo.io.KryoObjectOutput;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoUtils {

    static Kryo kryo = new Kryo();

    public static byte[] serialize(Object object) throws IOException {
       // kryo.register(object.getClass());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        KryoObjectOutput koo = new KryoObjectOutput(kryo, output);
        koo.writeObject(object);
        koo.flush();
        return baos.toByteArray();
    }

    public static Object deserialize(byte[] bs) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bs);
        Input input = new Input(bais);
        KryoObjectInput koi = new KryoObjectInput(kryo, input);
        return koi.readObject();
    }
}
