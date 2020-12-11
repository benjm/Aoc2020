package com.aoc.benjm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class Day1Test {
    @Test
    public void testRun() {
        //assertEquals(514579l, new Day1().run("/testInput.txt"));
        assertEquals(241861950, new Day1().run("/testInput.txt"));
    }
}