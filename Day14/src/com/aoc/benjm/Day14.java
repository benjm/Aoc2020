package com.aoc.benjm;

import java.util.*;

public class Day14 {
    public long run(String filename) {
        Scanner scanner = new Scanner(Day14.class.getResourceAsStream(filename));
        long start = System.currentTimeMillis();
        Long result = partOne(scanner);
        log("TIME TAKEN : " + (System.currentTimeMillis() - start));
        return result;
    }

    private Long partOne(Scanner scanner) {
        String mask = ""; //scanner.nextLine().replace(" ", "").split("=")[1];
        Map<String, Long> map = new HashMap<>();
        int count = 0;
        int masks = 0;
        while(scanner.hasNextLine()) {
            String[] line = scanner.nextLine().replace(" ", "").split("=");
            if(line[0].equals("mask")) {
                mask = line[1];
                //log("mask updated to " + mask);
                masks++;
            } else {
                String address = line[0].substring(4, line[0].length() - 1);
                long value = Long.valueOf(line[1]);
                long newValue = applyMask(value, mask);
                //log(address + " : " + newValue + " (was: " + value + ")");
                map.put(address, newValue);
                count++;
            }
        }
        long sum = 0;
        for (long v : map.values()) {
            sum += v;
        }
        log("masks  : " + masks);
        log("changes: " + count);
        log("num_mem: " + map.size());
        log("sum    : " + sum);
        return sum;
    }

    private long applyMask(long value, String mask) {
        char maskArray[] = mask.toCharArray();
        char valueArray[] = Long.toBinaryString(value).toCharArray();
        char outArray[] = new char[maskArray.length];
        int d = maskArray.length - valueArray.length;
        for (int i = maskArray.length-1; i >= 0; i--) {
            char c = maskArray[i];
            if (i > d) {
                char v = valueArray[i - d];
                if(c == 'X') {
                    outArray[i] = v;
                } else {
                    outArray[i] = c;
                }
            } else if (c == 'X') {
                outArray[i] = '0';
            } else {
                outArray[i] = c;
            }
        }
        String newString = new String(outArray);
        long out = Long.parseLong(newString, 2);
        return out;
    }

    private static void log(final String s) {
        System.out.println(s);
    }
}

