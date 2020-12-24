package com.aoc.benjm;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class DayN {

    private boolean isPartTwo = false;

    public DayN() {}

    public DayN isPartTwo() {
        isPartTwo = true;
        return this;
    }

    public long run(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Set<String> flipped = new HashSet<>();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String coord = getFinalCoord(line);
            if(flipped.contains(coord)) {
                flipped.remove(coord);
            } else {
                flipped.add(coord);
            }
        }
        return flipped.size();
    }

    private void log(final Object o) {
        System.out.println(o);
    }

    /*
    OFFSET CO-ORD
    if y is even the rows above or below are offset right
     */
    public String getFinalCoord(final String line) {
        int x = 0;
        int y = 0;
        char prev = ' ';
//        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            switch (c) {
                case ' ': /*nothing*/ break;
                case 'n': y+=1; break;
                case 's': y-=1; break;
                case 'e': {
                    if (prev == 'e' || prev == 'w' || y % 2 == 0) {
                        x+=1;
                    }
                } break;
                case 'w': {
                    if (prev == 'e' || prev == 'w' || y % 2 != 0) {
                        x-=1;
                    }
                } break;
                default: /*nothing*/ break;
            }
//            if (prev == 'e' || prev == 'w') sb.append(' ');
//            sb.append(c);
            prev = c;
        }
        String coord = x+","+y;
//        sb.append(" == " + coord);
//        log(sb.toString());
        return coord;
    }
}

class Coord {
    private int x, y;
    public Coord() {
        this(0,0);
    }
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
