package com.liuyang;

import com.google.common.collect.HashBasedTable;
import org.junit.Test;

import java.util.Map;

public class ExceptionTest {


    @Test
    public void test2() {
        HashBasedTable<Integer, Integer, String> table = HashBasedTable.create();

        table.put(1,1,"a");
        table.put(1,2,"b");
        table.put(2,2,"c");

        Map<Integer, String> row = table.row(1);
        System.out.println(row);
        Map<Integer, String> column = table.column(2);
        System.out.println(column);
    }

    @Test
    public void test1() {
        try {
            a();
        } catch (Exception e) {
            System.out.println("name:" + e.getClass().getName());
            Throwable cause = e.getCause();
            System.out.println("cause:" + cause);
            String localizedMessage = e.getLocalizedMessage();
            System.out.println("localizedMessage :" + localizedMessage);
            String message = e.getMessage();
            System.out.println("message:" + message);
            StackTraceElement[] stackTrace = e.getStackTrace();
            System.out.println("---------------------");
            for (StackTraceElement s : stackTrace) {
                System.out.println(s.getClassName());
                System.out.println(s.getMethodName());
                System.out.println("**********");
            }
        }
    }

    void a() {
        b();
    }

    void b() {
        c();
    }

    void c() {
        d();
    }

    void d() {
        e();
    }

    void e() {
        throw new NullPointerException();
    }

}
