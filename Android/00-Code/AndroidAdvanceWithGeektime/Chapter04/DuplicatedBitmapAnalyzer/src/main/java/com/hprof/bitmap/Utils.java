package com.hprof.bitmap;

import com.squareup.haha.perflib.ArrayInstance;
import com.squareup.haha.perflib.Instance;
import com.squareup.haha.perflib.Type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/6/24 15:27
 */
class Utils {

    static ArrayList<Instance> getTraceFromInstance(Instance instance) {
        ArrayList<Instance> arrayList = new ArrayList<>();
        Instance nextInstance;
        while ((nextInstance = instance.getNextInstanceToGcRoot()) != null) {
            arrayList.add(nextInstance);
            instance = nextInstance;
        }
        return arrayList;
    }

    private static boolean isByteArray(Object value) {
        return value instanceof ArrayInstance && ((ArrayInstance) value).getArrayType() == Type.BYTE;
    }

    static byte[] getByteArray(Object arrayInstance) {
        if (isByteArray(arrayInstance)) {
            try {
                Method asRawByteArray = ArrayInstance.class.getDeclaredMethod("asRawByteArray", int.class, int.class);
                asRawByteArray.setAccessible(true);
                Field length = ArrayInstance.class.getDeclaredField("mLength");
                length.setAccessible(true);
                int lengthValue = (int) length.get(arrayInstance);
                return (byte[]) asRawByteArray.invoke(arrayInstance, 0, lengthValue);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
