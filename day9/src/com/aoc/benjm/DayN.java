package com.aoc.benjm;

import java.util.*;

public class DayN {
    public long partOne(String filename) {
        return run(filename, 5, false);
    }
    public long partOneFinal(String filename) {
        return run(filename, 25, false);
    }
    public long partTwo(String filename) { return run(filename, 5, true); }
    public long partTwoFinal(String filename) {
        return run(filename, 25, true);
    }

    private long run(String filename, final int historySize, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> fullList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final int next = Integer.parseInt(scanner.nextLine());
            if (list.size() < historySize) {
                list.add(next); // preamble : just add to queue
            } else {
                if (!isValid(next, list)) {
                    if (isPartTwo) return findContiguousFour(next, fullList);
                    return next;
                }
                list.pollFirst();
                list.add(next);
            }
            fullList.add(next);
        }
        return 0l;
    }

    private long findContiguousFour(final int val, List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int j = i + 1;
            int sum = list.get(i) + list.get(j);
            while (sum < val) {
                j++;
                sum += list.get(j);
            }
            if (sum == val) {
                int min = list.get(j);
                int max = min;
                for (int x = i; x < j; x++) {
                    int next = list.get(x);
                    if (next < min) min = next;
                    if (next > max) max = next;
                }
                return min + max;
            }
        }
        throw new RuntimeException("no contiguous sum found");
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
