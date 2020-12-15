package com.aoc.benjm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DayN {
    //(.), an empty seat (L), or an occupied seat (#)
    private final char FLOOR = '.';
    private final char EMPTY = 'L';
    private final char OCCUP = '#';

    public long partOne(String filename) {
        return runSim(filename, false);
    }

    public long partTwo(String filename) {
        return runSim(filename, true);
    }

    private long runSim(String filename, boolean usePartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        List<String> prevRows = new ArrayList<>();
        while(scanner.hasNextLine()) {
            prevRows.add(scanner.nextLine());
        }
        final int height = prevRows.size();
        final int width = prevRows.get(0).length();
        boolean changed = true; // first iteration is a change from nothing
        long iterations = 0;
        long count = 0;
        while(changed) {
            List<String> nextRows = new ArrayList<>();
            iterations++;
            count = 0;
            changed = false;
            for(int y = 0; y < height; y++) {
                StringBuilder sb = new StringBuilder();
                for(int x = 0; x < width; x++) {
                    final char c = prevRows.get(y).charAt(x);
                    final char k;
                    if (usePartTwo) {
                        k = partTwoResult(c,prevRows,x,y,height,width);
                    } else {
                        k = partOneResult(c,prevRows,x,y,height,width);
                    }
                    if (c != k) changed = true;
                    if (k == OCCUP) count++;
                    sb.append(k);
                }
                nextRows.add(sb.toString());
            }
            prevRows = nextRows;
        }
        return count;
    }

    private char partTwoResult(final char c, final List<String> prevRows, final int x, final int y, final int height, final int width) {
        if (c == FLOOR) return FLOOR;
        int countVisibleOccup = 0;
        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                if (canSeeOccup(prevRows, x+dx, y+dy, dx, dy)) countVisibleOccup++;
            }
        }
        if (c == OCCUP && countVisibleOccup >= 5) return EMPTY;
        if (c == EMPTY && countVisibleOccup == 0) return OCCUP;
        return c;
    }

    private boolean canSeeOccup(final List<String> prevRows, final int x, final int y, final int dx, final int dy) {
        if (dx == 0 && dy == 0) return false; // invalid direction
        if (x < 0 || y < 0 || x >= prevRows.get(0).length() || y >= prevRows.size()) return false; // beyond edge
        char c = prevRows.get(y).charAt(x);
        if (c == EMPTY) return false;
        if (c == OCCUP) return true;
        return canSeeOccup(prevRows,x+dx,y+dy,dx,dy);
    }

    private char partOneResult(final char c, final List<String> prevRows, final int x, final int y, final int height, final int width) {
        if (c == FLOOR) return FLOOR;
        int countOccup = 0;
        for(int h = y-1; h <= y+1; h++) {
            for(int w = x-1; w <= x+1; w++) {
                if (h >= 0 && h < height && w >= 0 && w < width && (x != w || y != h)) {
                    char c2 = prevRows.get(h).charAt(w);
                    if (c2 == OCCUP) {
                        countOccup++;
                    }
                }
            }
        }
        if (c == OCCUP && countOccup >= 4)  return EMPTY;
        if (c == EMPTY && countOccup == 0) return OCCUP;
        return c;
    }
}
