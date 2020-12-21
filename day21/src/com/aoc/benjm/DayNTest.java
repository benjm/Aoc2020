package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partOne("/testInput.txt").count;
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(5l, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().partOne("/input.txt").count;
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(2659l, l);
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        String l = new DayN().partTwo("/testInput.txt").canonical;
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("mxmxvkd,sqjhc,fvjkl", l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        String l = new DayN().partTwo("/input.txt").canonical;
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals("rcqb,cltx,nrl,qjvvcvz,tsqpn,xhnk,tfqsb,zqzmzl", l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}