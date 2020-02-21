package com.liuyang;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class Guava {

    private final Joiner joiner = Joiner.on(",").skipNulls();
    private final Splitter splitter = Splitter.on(",").omitEmptyStrings();

    @Test
    public void test1() {
        String str = joiner.join(Arrays.asList("a", null, "b"));
        System.out.println(str);
        List<String> list = splitter.splitToList(str);
        System.out.println(list);
    }
}
