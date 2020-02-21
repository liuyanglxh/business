package com.liuyang;

import com.aux.SubClass;
import org.junit.Test;

public class ClassLoadingTest {

    /**
     * 通过子类引用父类静态变量，不会导致子类加载
     */
    @Test
    public void test1(){
        System.out.println(SubClass.a);
    }

}
