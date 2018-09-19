package ru.otus.L20180801;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final int MEASURE_COUNT = 1;

    public static void main(String... args) {
        final List<Integer> collection = new ArrayList<>();
        final int min = 0;
        final int max = 1_999;
        for (int i = min; i < max + 1; i++) {
            collection.add(i);
        }

        final ImmutableList<Integer> integers = ImmutableList.copyOf(collection);
        System.out.println(integers.subList(0, 10));

        calcTime(() -> Collections.shuffle(collection));
    }

    private static void calcTime(Runnable runnable) {
        long startTime = System.nanoTime();
        for (int i = 0; i < MEASURE_COUNT; i++) {
            runnable.run();
        }
        long finishTime = System.nanoTime();
        long timeNs = (finishTime - startTime) / MEASURE_COUNT;
        System.out.println("Time spent: " + timeNs + "ns (" + timeNs / 1_000_000 + "ms)");
    }
}
