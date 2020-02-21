package com.liuyang;

import com.liuyang.business.utils.CollectionUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

public class HashMapTest {

    @Test
    public void test2() {
        int size = 7;
        HashMap<Integer, Integer> map = new HashMap<>(CollectionUtil.hashMapInitCapacity(size));
        map.put(1, 1);
        this.showTableInfo(map);
        for (int i = 2; i <= size; i++) {
            map.put(i, i);

            this.showTableInfo(map);
        }
    }

    @Test
    public void test1() {
        int size = 7;
        HashMap<Integer, Integer> map = new HashMap<>(size);
        map.put(1, 1);
        this.showTableInfo(map);
        for (int i = 2; i <= size; i++) {
            map.put(i, i);
        }
        this.showTableInfo(map);
    }

    /**
     * 输出HashMap底层数组table的长度和地址
     */
    private void showTableInfo(HashMap<?, ?> hashMap) {
        try {
            Field table = hashMap.getClass().getDeclaredField("table");
            table.setAccessible(true);
            Object[] o = (Object[]) table.get(hashMap);
            System.out.println(String.format("length is %d  address is %s", o.length, o.toString()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
