package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN(false).run("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(306l, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN(false).run("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(31629l, l);
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        long l = new DayN(true).run("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(291l, l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        long l = new DayN(true).run("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(-1l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}