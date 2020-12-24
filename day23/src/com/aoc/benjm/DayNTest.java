package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    @Test
    void testMoves() {
        DayN d = new DayN();
        assertEquals("-2,0", d.getFinalCoord("ww")); // oh...
        assertEquals("0,0", d.getFinalCoord("ewewew"));
        assertEquals("0,0", d.getFinalCoord("nesw"));
        assertEquals("0,0", d.getFinalCoord("nwse"));
        assertEquals("0,0", d.getFinalCoord("senw"));
        assertEquals("0,0", d.getFinalCoord("neesww"));
        assertEquals("0,0", d.getFinalCoord("nenweswwse"));
        assertEquals("0,0", d.getFinalCoord("nenwnenwseswsesw"));
        assertEquals("0,0", d.getFinalCoord("nwnweeeswswswswwnene"));
        assertEquals(d.getFinalCoord("nenene"), d.getFinalCoord("nwnwnweee"));
        assertEquals(d.getFinalCoord("nesenese"), d.getFinalCoord("ee"));
        assertEquals(d.getFinalCoord("nwswnwsw"), d.getFinalCoord("ww"));
    }

    @Test
    void partOneExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().run("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(10l, l);
    }

    @Test
    void partOneFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().run("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(-1l, l);
        //346 result, example passing fine, is "too low"
    }

    @Test
    void partTwoExample() {
        long start = System.currentTimeMillis();
        Long l = new DayN().isPartTwo().run("/testInput.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(-1l, l);
    }

    @Test
    void partTwoFinal() {
        long start = System.currentTimeMillis();
        Long l = new DayN().isPartTwo().run("/input.txt");
        log("outp: " + l + "\ntime : " + (System.currentTimeMillis() - start));
        assertEquals(-1l, l);
    }

    private static void log(final Object o) {
        System.out.println(o);
    }
}