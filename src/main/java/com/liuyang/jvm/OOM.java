package com.liuyang.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OOM {

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<Tree> lst1 = new ArrayList<>();
        List<Tree> lst2 = new ArrayList<>();
        List<Tree> lst3 = new ArrayList<>();
        while (true) {
            int i = random.nextInt(0, 10);
            lst1.add(buildTree(i));
            lst2.add(buildTree(i / 2));
            lst3.add(buildTree(i / 4));
        }
    }

    public static Tree buildTree(int height) {
        Tree tree = new Tree();
        Tree curr = tree;
        while (height-- > 0) {
            curr.tree = new Tree();
            curr = curr.tree;
        }
        return tree;
    }

    static class Tree {
        private Tree tree;
    }
}
