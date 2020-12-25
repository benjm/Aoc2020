package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().run("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(14897079l, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().run("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(9620012l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}