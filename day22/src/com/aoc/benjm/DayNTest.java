package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {
    private static final int tenMillion = 10000000;

    @Test
    void partOneExample10() {
        long start = System.currentTimeMillis();
        String r = new DayN().play("389125467", 10);
        log("outp: " + r + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("92658374", r);
    }

    @Test
    void partOneExample100() {
        long start = System.currentTimeMillis();
        String r = new DayN().play("389125467", 100);
        log("outp: " + r + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("67384529", r);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        String r = new DayN().play("398254716", 100);
        log("outp: " + r + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("45798623", r);
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        String r = new DayN().partTwo().play("389125467", tenMillion);
        log("outp: " + r + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("149245887792", r);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        String r = new DayN().partTwo().play("398254716", tenMillion);
        log("outp: " + r + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("235551949822", r);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}