package com.rzm.commonlibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by zp on 2017/4/23.
 */

public class CacheProviders {

    /**
     * 序列化对象
     *
     * @param baseModel
     * @return
     * @throws IOException
     */
    public static String serialize(Serializable baseModel) throws IOException {
        if (baseModel == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(baseModel);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Serializable deSerialization(String str) throws IOException,
            ClassNotFoundException {
        if (str == null)
            return null;
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Serializable baseModel = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return baseModel;
    }
}
