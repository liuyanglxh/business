package com.liuyang;

public class VolatileVisibleTest {

    public static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {


        new Thread(() -> {
            System.out.println("start waiting");
            while (!flag) {

            }
            System.out.println("finish waiting");
        }).start();

        Thread.sleep(1000);

        new Thread(() -> {
            System.out.println("start changing");
            flag = true;
            System.out.println("finish changing");
        }).start();


    }


}
