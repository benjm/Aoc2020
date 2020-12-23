package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

    @Test
    void testRun() {
        Long l = new Day13Again().partTwo("/testInput.txt");
        assertEquals(1068781, l);
    }

    @Test
    void actualRun() {
        Long l = new Day13Again().partTwo("/input.txt");
        assertEquals(-1l, l);
    }
}