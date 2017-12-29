package com.rzm.commonlibrary.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Created by renzhenming on 2017/12/29.
 * java - 在数据库中，当对象有不同的serialVersionUID时，如何反序列化对象
 *
 * 在项目中创建下面的类。 Whereever创建对象的对象，
 * 使用 DecompressibleInputStream，而它将旧对象反序列化为 new 版本Id类。
 */

public class DecompressibleInputStream extends ObjectInputStream {
    public DecompressibleInputStream(InputStream in) throws IOException {
        super(in);
    }
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();//initially streams descriptor
        Class localClass = Class.forName(resultClassDescriptor.getName());//the class in the local JVM that this descriptor represents.
        if (localClass == null) {
            System.out.println("No local class for" + resultClassDescriptor.getName());
            return resultClassDescriptor;
        }
        ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
        if (localClassDescriptor!= null) {//only if class implements serializable
            final long localSUID = localClassDescriptor.getSerialVersionUID();
            final long streamSUID = resultClassDescriptor.getSerialVersionUID();
            if (streamSUID!= localSUID) {//check for serialVersionUID mismatch.
                final StringBuffer s = new StringBuffer("Overriding serialized class version mismatch:");
                s.append("local serialVersionUID =").append(localSUID);
                s.append(" stream serialVersionUID =").append(streamSUID);
                Exception e = new InvalidClassException(s.toString());
                System.out.println("Potentially Fatal Deserialization Operation." + e);
                resultClassDescriptor = localClassDescriptor;//Use local class descriptor for deserialization
            }
        }
        return resultClassDescriptor;
    }
}