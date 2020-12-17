package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partOne("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(5l, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partOne("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(1384l, l);
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partTwo("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(8l, l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partTwo("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(761l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}