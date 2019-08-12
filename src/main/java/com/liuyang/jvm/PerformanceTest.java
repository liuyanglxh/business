package com.liuyang.jvm;

import org.junit.Test;


public class PerformanceTest {

    @Test
    public void test1() {
        int a = 256;
        int b = 2312313;
        System.out.println(b % a);
        System.out.println(b & (a - 1));
    }

    @Test
    public void test() {
        int totalTime = 1_000_000;
        int a = 256;
        int b = 2312313;

        int c = 0;
        int b2 = b;
        for (int i = 0; i < totalTime; i++) {
            c = b2-- % a;
        }
        b2 = b;
        for (int i = 0; i < totalTime; i++) {
            c = b2-- & (a - 1);
        }

        long cost1 = 0;
        long cost2 = 0;

        long start = System.currentTimeMillis();
        b2 = b;
        for (int i = 0; i < totalTime; i++) {
            c = b2-- % a;
        }
        cost1 = System.currentTimeMillis() - start;


        start = System.currentTimeMillis();
        b2 = b;
        for (int i = 0; i < totalTime; i++) {
            c = b2-- & (a - 1);
        }
        cost2 = System.currentTimeMillis() - start;
        System.out.println(cost1);
        System.out.println(cost2);
    }


}
