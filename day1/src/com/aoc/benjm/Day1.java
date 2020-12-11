package com.aoc.benjm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Day1 {
    public long run(String filename) {
        Scanner scanner = new Scanner(Day1.class.getResourceAsStream(filename));
        Set<Long> numbers = new HashSet<>();
        while(scanner.hasNextLine()) {
            numbers.add(scanner.nextLong());
        }
        long start = System.currentTimeMillis();
        //long result = calcTwo(numbers);
        long result = calcThree(numbers);
        log("time: " + (System.currentTimeMillis() - start));
        return result;
    }

    private static void log(final String s) {
        System.out.println(s);
    }

    private long calcTwo(final Set<Long> numbers) {
        Set<Long> remaining = new HashSet<>();
        remaining.addAll(numbers);

        for(Long l : numbers) {
            remaining.remove(l);
            for(Long l2 : remaining) {
                if (l + l2 == 2020l) {
                    long p = l * l2;
                    log(l + " * " + l2 + " = " + p);
                    return p;
                }
            }
        }
        return 0l;
    }

    private long calcThree(final Set<Long> numbers) {
        List<Long> sorted = new ArrayList<>();
        sorted.addAll(numbers);
        sorted.sort(Comparator.naturalOrder());
        int len = sorted.size();
        for (int i = 0; i < len-2; i++) {
            for (int j = i + 1; j < len-1; j++) {
                if (sorted.get(i) + sorted.get(j) < 2020) {
                    for (int k = j + 1; k < len; k++) {
                        if (sorted.get(i) + sorted.get(j) + sorted.get(k) == 2020) {
                            long p = sorted.get(i) * sorted.get(j) * sorted.get(k);
                            log(sorted.get(i) + " * " + sorted.get(j) + " * " + sorted.get(k) + " = " + p);
                            return p;
                        }
                    }
                }
            }
        }
        return 0l;
    }
}
