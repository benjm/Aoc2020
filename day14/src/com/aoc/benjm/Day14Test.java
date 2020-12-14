package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test {

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new Day14().partOne("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(165, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new Day14().partOne("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(15919415426101l, l);
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        Long l = new Day14().partTwo("/test2Input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(208, l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        Long l = new Day14().partTwo("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(3443997590975l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}