package com.aoc.benjm;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class DayN {
    public long partOne(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        long sum = 0l;
        Set<Character> group = new HashSet<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            for (char c : line.toCharArray()) group.add(c);
            if (line.isEmpty() || !scanner.hasNextLine()) {
                sum += group.size();
                group.clear();
            }
        }
        return sum;
    }

    public long partTwo(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        long sum = 0l;
        Set<Character> group = new HashSet<>();
        boolean hasNone = false;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (!line.isEmpty() && group.isEmpty() && ! hasNone) {
                for (char c : line.toCharArray()) {
                    group.add(c);
                }
            } else if (!line.isEmpty()) {
                Set<Character> tempGroup = new HashSet<>();
                for (char c : line.toCharArray()) {
                    if(group.contains(c)) tempGroup.add(c);
                }
                group = tempGroup;
                if (tempGroup.isEmpty()) hasNone = true;
            }
            if (line.isEmpty() || !scanner.hasNextLine()) {
                sum += group.size();
                group.clear();
                hasNone = false;
            }
        }
        return sum;
    }
}
