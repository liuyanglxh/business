package com.liuyang.business.utils;

public class CollectionUtil {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public static int hashMapInitCapacity(int elementSize) {
        float ft = ((float) elementSize / 0.75F) + 1.0F;
        return ((ft < (float) MAXIMUM_CAPACITY) ? (int) ft : MAXIMUM_CAPACITY);
    }
}
