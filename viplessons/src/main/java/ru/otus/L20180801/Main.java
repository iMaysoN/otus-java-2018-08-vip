package ru.otus.L20180801;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;

public class Main {
    public static void main(String... args) {
        final ImmutableList<String> strings = ImmutableList.copyOf(Arrays.asList("One", "Two"));
        System.out.println(strings);

    }
}
