package com.aoc.benjm;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class DayN {
    public long partOne(String filename) {
        return run(filename, 5, false);
    }
    public long partOneFinal(String filename) {
        return run(filename, 25, false);
    }
    public long partTwo(String filename) { return run(filename, 25, true); }
    public long partTwoFinal(String filename) {
        return run(filename, 25, true);
    }

    private long run(String filename, final int historySize, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        LinkedList<Integer> list = new LinkedList<>();
        while (scanner.hasNextLine()) {
            final int next = Integer.parseInt(scanner.nextLine());
            if (list.size() < historySize) {
                list.add(next); // preamble : just add to queue
            } else {
                if (!isValid(next, list)) {
                    if (isPartTwo) return findContiguousFour(next, list);
                    return next;
                }
                list.pollFirst();
                list.add(next);
            }
        }
        return 0l;
    }

    private long findContiguousFour(final int val, LinkedList<Integer> list) {
        /*
        find a contiguous set of at least two numbers in list which sum to val.
        */
    }

    private boolean isValid(final int val, LinkedList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) + list.get(j) == val) {
                    return true;
                }
            }
        }
        return false;
    }
}
