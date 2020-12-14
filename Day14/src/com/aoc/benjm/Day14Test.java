package com.aoc.benjm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test {

    @Test
    void run() {
        Long l = new Day14().run("/input.txt");
        assertEquals(165, l);
    }
}