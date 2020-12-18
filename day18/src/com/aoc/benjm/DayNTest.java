package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    /*
    1 + 2 * 3 + 4 * 5 + 6 becomes 71.
    1 + (2 * 3) + (4 * (5 + 6)) becomes 51.
    2 * 3 + (4 * 5) becomes 26.
    5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 437.
    5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 12240.
    ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 13632.
     */
    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partOne("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(26457l, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partOne("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(36382392389406l, l);
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partTwo("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(694173l, l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partTwo("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(381107029777968l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}