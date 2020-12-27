package com.aoc.benjm;

import java.util.*;


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
            log("day " + i + " (" + flipped.size() + "): " + setToString(flipped));
            flipped = conwayHex(flipped);
        }
        return flipped.size();
    }

    private String setToString(final Set<Coord> set) {
        StringBuilder sb = new StringBuilder();
        for (Coord c : set) {
            sb.append(c.id + " ");
        }
        return sb.toString();
    }

    private Set<Coord> conwayHex(final Set<Coord> flipped) {
        Set<Coord> next = new HashSet<>();
        Map<Coord, Integer> flippedNeighbourCount = new HashMap<>();
        for (Coord currentlyFlipped : flipped) {
            List<Coord> neighbours = currentlyFlipped.getNeighbours();
            for(Coord neighbour : neighbours) {
                if (flippedNeighbourCount.containsKey(neighbour)) {
                    flippedNeighbourCount.put(neighbour, flippedNeighbourCount.get(neighbour) + 1);
                } else {
                    flippedNeighbourCount.put(neighbour, 1);
                }
            }
        }
        /*
        if (neighbours == 2) add to next
        if (neighbours == 1 && is flipped) add to next
         */
        for (Coord coord : flippedNeighbourCount.keySet()) {
            final int numNeighbours = flippedNeighbourCount.containsKey(coord) ? flippedNeighbourCount.get(coord) : 0;
            if (numNeighbours == 1 && flipped.contains(coord)) {
                next.add(coord);
            } else if (numNeighbours == 2) {
                next.add(coord);
//            } else if ((numNeighbours > 0 || numNeighbours <= 2) && flipped.contains(coord)) {
//                next.add(coord);
//            }
//        }
//        for (Coord coord : flipped) {
//            final int numNeighbours = flippedNeighbourCount.containsKey(coord) ? flippedNeighbourCount.get(coord) : 0;
//            if (numNeighbours == 0 || numNeighbours > 2) {
//                next.add(coord);
            }
        }
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
        Coord coord = new Coord(0,0);
        char prev = 'e'; // needed to correctly process e or w as first instruction in the line
        for (char c : line.toCharArray()) {
            switch (c) {
                case 'e': {
                    if (prev == 'e' || prev == 'w') {
                        coord = coord.e();
                    } else if (prev == 'n') {
                        coord = coord.ne();
                    } else if (prev == 's') {
                        coord = coord.se();
                    }
                } break;
                case 'w': {
                    if (prev == 'e' || prev == 'w') {
                        coord = coord.w();
                    } else if (prev == 'n') {
                        coord = coord.nw();
                    } else if (prev == 's') {
                        coord = coord.sw();
                    }
                } break;
                default: /*nothing*/ break;
            }
            prev = c;
        }
        return coord;
    }
}

class Coord {
    public static final String delim = ",";
    public final int x, y;
    private final int xe,xw;
    public final String id;
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = "("+x+delim+y+")";
        this.xe = (y % 2 == 0) ? 0 : 1;
        this.xw = 1 - xe;
    }

    public Coord e() {
        return new Coord(x+1, y);
    }

    public Coord w() {
        return new Coord(x-1, y);
    }

    public Coord ne() {
        return new Coord(x+xe, y+1);
    }

    public Coord nw() {
        return new Coord(x-xw, y+1);
    }

    public Coord se() {
        return new Coord(x+xe, y-1);
    }

    public Coord sw() {
        return new Coord(x-xw, y-1);
    }

    public List<Coord> getNeighbours() {
        List<Coord> neighbours = new ArrayList<>(6);
        neighbours.add(e());
        neighbours.add(w());
        neighbours.add(ne());
        neighbours.add(nw());
        neighbours.add(se());
        neighbours.add(sw());
        return neighbours;
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

    @Override
    public String toString() {
        return id;
    }
}
