package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

    @Test
    void testRun() {
        Long l = new Day13().run("/testInput.txt");
        assertEquals(1068781, l);
    }
}