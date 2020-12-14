package com.aoc.benjm;

import java.util.*;
import java.util.stream.LongStream;

public class Day14 {
    public long partOne(String filename) {
        Scanner scanner = new Scanner(Day14.class.getResourceAsStream(filename));
        long orMask = 0;
        long andMask = 0;
        Map<String, Long> map = new HashMap<>();
        while(scanner.hasNextLine()) {
            String[] line = scanner.nextLine().replace(" ", "").split("=");
            if(line[0].equals("mask")) {
                String mask = line[1];
                orMask = Long.parseUnsignedLong(mask.replace('X','0'),2);
                andMask = Long.parseUnsignedLong(mask.replace('X', '1'), 2);
            } else {
                String address = line[0].substring(4, line[0].length() - 1);
                long value = Long.parseLong(line[1]);
                long newValue = (value & andMask) | orMask;
                map.put(address, newValue);
            }
        }
        long sum = 0;
        for (long v : map.values()) {
            sum += v;
        }
        return sum;
    }

    public long partTwo(String filename) {
        Scanner scanner = new Scanner(Day14.class.getResourceAsStream(filename));
        long andMask = 0l;
        final Set<Long> orMasks = new HashSet<>();
        Map<Long, Long> map = new HashMap<>();
        while(scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" = ");
            if(line[0].equals("mask")) {
                final String mask = line[1];
                //andMask keep everything that is not an X
                andMask = Long.parseUnsignedLong(mask.replace('0','1').replace('X','0'),2);
                orMasks.clear();
                //orMasks create all the possible binary combinations where you replace numX Xs in the mask
                final long numX = mask.length() - mask.replace("X","").length();
                for (long miniMask = 0; miniMask < (1l << (numX)); miniMask++) {
                    StringBuilder sb = new StringBuilder();
                    String m = Long.toBinaryString(miniMask);
                    for(int i = 0; i < numX - m.length(); i++) {
                        sb.append('0');
                    }
                    sb.append(m);
                    String replacements = sb.toString();
                    int rep = 0;
                    StringBuilder masked = new StringBuilder();
                    for(int i = 0; i < mask.length(); i++) {
                        if (mask.charAt(i) == 'X') {
                            masked.append(replacements.charAt(rep));
                            rep++;
                        } else {
                            masked.append(mask.charAt(i));
                        }
                    }
                    String maskedString = masked.toString();
                    orMasks.add(Long.parseUnsignedLong(maskedString, 2));
                }
            } else {
                final long address = Long.parseUnsignedLong(line[0].substring(4, line[0].length() - 1));
                final long value = Long.parseUnsignedLong(line[1]);
                for (long orMask : orMasks) {
                    long newAddr = ((address & andMask) | orMask);
                    map.put(newAddr, value);
                }
            }
        }
        long out = map.values().stream().reduce(0l, (sum, l) -> sum + l);
        return out;
    }

    private static void log(final String s) {
        System.out.println(s);
    }
}

