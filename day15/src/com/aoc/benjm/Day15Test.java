package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day15Test {

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Day15 day = new Day15();

        assertEquals(0l, day.partOne("0,3,6", 4l));
        assertEquals(3l, day.partOne("0,3,6", 5l));
        assertEquals(3l, day.partOne("0,3,6", 6l));
        assertEquals(1l, day.partOne("0,3,6", 7l));
        assertEquals(0l, day.partOne("0,3,6", 8l));
        assertEquals(4l, day.partOne("0,3,6", 9l));
        assertEquals(0l, day.partOne("0,3,6", 10l));
        //then the 2020 examples
        assertEquals(436l, day.partOne("0,3,6", 2020l));
        assertEquals(1l, day.partOne("1,3,2", 2020l));
        assertEquals(10l, day.partOne("2,1,3", 2020l));
        assertEquals(27l, day.partOne("1,2,3", 2020l));
        assertEquals(78l, day.partOne("2,3,1", 2020l));
        assertEquals(438l, day.partOne("3,2,1", 2020l));
        assertEquals(1836l, day.partOne("3,1,2", 2020l));
        log("time : " + (System.currentTimeMillis() - start));
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new Day15().partOne("0,13,1,16,6,17", 2020l);
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(234l, l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        Long l = new Day15().partOne("0,13,1,16,6,17", 30000000l);
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(8984l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}