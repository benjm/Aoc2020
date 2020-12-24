package com.aoc.benjm;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;


public class DayN {

    private boolean isPartTwo = false;

    public DayN() {}

    public DayN isPartTwo() {
        this.isPartTwo = true;
        return this;
    }

    public long run(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Set<Coord> flipped = new HashSet<>();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Coord coord = getFinalCoord(line);
            if(flipped.contains(coord)) {
                flipped.remove(coord);
            } else {
                flipped.add(coord);
            }
        }
        if(!isPartTwo) return flipped.size();
        for (int i = 0; i < 100; i++) {
            flipped = conwayHex(flipped);
        }
        return flipped.size();
    }

    private Set<Coord> conwayHex(final Set<Coord> flipped) {
        Set<Coord> next = new HashSet<>();
        Set<Coord> nextToFlipped = new HashSet<>();
        for (Coord currentlyFlipped : flipped) {

        }
        //set of all neighbours

        return next;
    }

    private void log(final Object o) {
        System.out.println(o);
    }

    /*
    OFFSET CO-ORD
    if y is even the rows above or below are offset right
     */
    public Coord getFinalCoord(final String line) {
        boolean extraLogging = false;
        int x = 0;
        int y = 0;
        char prev = 'e'; // needed to correctly process e or w as first instruction in the line
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            switch (c) {
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
            if (extraLogging) {
                if (prev == 'e' || prev == 'w')
                    sb.append(' ');
                sb.append(c);
            }
            prev = c;
        }
        String coord = x+","+y;
        if (extraLogging) {
            sb.append(" == " + coord);
            log(sb.toString());
        }
        return new Coord(x,y);
    }
}

class Coord {
    public static final String delim = ",";
    public final int x, y;
    public final String id;
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = x+delim+y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Coord coord = (Coord) o;
        return id.equals(coord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
