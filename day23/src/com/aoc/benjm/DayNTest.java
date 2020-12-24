package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

    @Test
    void testMoves() {
        DayN d = new DayN();
        assertEquals("0,1", d.getFinalCoord("ne").id);
        assertEquals("-1,1", d.getFinalCoord("nw").id);
        assertEquals("-2,0", d.getFinalCoord("ww").id);
        assertEquals("0,0", d.getFinalCoord("ewewew").id);
        assertEquals("0,0", d.getFinalCoord("nesw").id);
        assertEquals("0,0", d.getFinalCoord("nwse").id);
        assertEquals("0,0", d.getFinalCoord("senw").id);
        assertEquals("0,0", d.getFinalCoord("neesww").id);
        assertEquals("0,0", d.getFinalCoord("nenweswwse").id);
        assertEquals("0,0", d.getFinalCoord("nenwnenwseswsesw").id);
        assertEquals("0,0", d.getFinalCoord("nwnweeeswswswswwnene").id);
        assertEquals(d.getFinalCoord("nenene").id, d.getFinalCoord("nwnwnweee").id);
        assertEquals(d.getFinalCoord("nesenese").id, d.getFinalCoord("ee").id);
        assertEquals(d.getFinalCoord("nwswnwsw").id, d.getFinalCoord("ww").id);
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
        assertEquals(400l, l);
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