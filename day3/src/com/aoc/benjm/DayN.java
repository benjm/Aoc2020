package com.aoc.benjm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DayN {
    private static final char TREE = '#';

    public long partOne(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        List<String> list = new ArrayList<>();
        while(scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        return checkSlope(list,3,1);
    }

    public long partTwo(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        List<String> list = new ArrayList<>();
        while(scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
//        Right 1, down 1.
//        Right 3, down 1. (This is the slope you already checked.)
//        Right 5, down 1.
//        Right 7, down 1.
//        Right 1, down 2.
        long product = 1;
        product *= checkSlope(list,1,1);
        product *= checkSlope(list,3,1);
        product *= checkSlope(list,5,1);
        product *= checkSlope(list,7,1);
        product *= checkSlope(list,1,2);
        return product;
    }

    private long checkSlope(List<String> list, int dx, int dy) {
        int height = list.size();
        int width = list.get(0).length();
        int y = 0;
        int x = 0;
        long trees = 0;
        while(y < height) {
            x += dx;
            y += dy;
            if (x >= width) x -= width;
            if (y >= height) return trees;
            char c = list.get(y).charAt(x);
            if (c == TREE){
                trees++;
            }
        }
        return trees;
    }
}
