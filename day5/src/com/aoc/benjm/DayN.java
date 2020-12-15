package com.aoc.benjm;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class DayN {
    public long partOne(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        int max = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int row = minmax(line.substring(0,7),127,'B');
            int seat = minmax(line.substring(7,10),7,'R');
            int id = 8 * row + seat;
            if (id > max) max = id;
        }
        return max;
    }

    private int minmax(final String chars, final int top, final char upper) {
        int min = 0;
        int max = top;
        for (int i = 0; i < chars.length(); i++) {
            int d = (max - min + 1) / 2;
            if (chars.charAt(i) == upper) {
                min += d;
            } else {
                max -= d;
            }
        }
        if (min != max) {
            throw new RuntimeException(min + " (min) != (max) " + max);
        }
        return min;
    }

    public long partTwo(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        int max = 0;
        Set<Integer> ids = new HashSet<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int row = minmax(line.substring(0,7),127,'B');
            int seat = minmax(line.substring(7,10),7,'R');
            int id = 8 * row + seat;
            if (id > max) max = id;
            ids.add(id);
        }
        int prev = max;
        for (int i = 0; i <= max; i++) {
            if (ids.contains(i)) {
                if (i - prev == 2) return i - 1;
                prev = i;
            }
        }
        return 0l;
    }
}
