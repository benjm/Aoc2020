package com.aoc.benjm;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.LongStream;

public class Day14 {
    public long run(String filename) {
        Scanner scanner = new Scanner(Day14.class.getResourceAsStream(filename));
        long start = System.currentTimeMillis();
        Long result = partOne(scanner);
        log("TIME TAKEN : " + (System.currentTimeMillis() - start));
        return result;
    }

    private Long partOne(Scanner scanner) {
        String mask = "";
        Map<String, Long> map = new HashMap<>();
        int count = 0;
        int masks = 0;
        while(scanner.hasNextLine()) {
            String[] line = scanner.nextLine().replace(" ", "").split("=");
            if(line[0].equals("mask")) {
                mask = line[1];
                masks++;
            } else {
                String address = line[0].substring(4, line[0].length() - 1);
                long value = Long.parseLong(line[1]);
                long newValue = applyMask(value, mask);
                map.put(address, newValue);
                count++;
            }
        }
        int checkCount = 0;
        long sum = 0;
        for (long v : map.values()) {
            sum += v;
//            log(String.format("running total: %16d", sum));
            checkCount++;
        }
        log("masks  : " + masks);
        log("changes: " + count);
        log("num_mem: " + map.size() + " (summed: " + checkCount + ")");
        log("sum    : " + sum);
        return sum;
    }

    private long applyMask(long value, String mask) {
        long orMask = Long.parseUnsignedLong(mask.replace('X','0'),2);
        long andMask = Long.parseUnsignedLong(mask.replace('X', '1'), 2);
        long other = (value & andMask) | orMask;

        char maskArray[] = mask.toCharArray();
        char valueArray[] = Long.toUnsignedString(value,2).toCharArray();
        char outArray[] = new char[maskArray.length];
        int d = maskArray.length - valueArray.length;
        for (int i = maskArray.length-1; i >= 0; i--) {
            char c = maskArray[i];
            if (i >= d) { //<-- oops
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

        long out = Long.parseUnsignedLong(newString, 2);

//        if (out != other) {
//            String inString = new String(valueArray);
//            while(inString.length() < newString.length()) {
//                inString = "0" + inString;
//            }
//            log("vin : " + inString + "\n" + "mask: " + mask + "\n" + "vout: " + newString + "\nalt : " + Long.toUnsignedString(other,2) + "\n");
//            log("oops");
//        }

        return other;
    }

    private static void log(final String s) {
        System.out.println(s);
    }
}

