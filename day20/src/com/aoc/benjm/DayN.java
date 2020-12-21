package com.aoc.benjm;

import java.lang.reflect.Array;
import java.util.*;

public class DayN {
    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Map<Integer, Tile> tileMap = new HashMap<>();
        while (scanner.hasNextLine()) {
            Tile tile = readTile(scanner);
            tileMap.put(tile.id, tile);
            System.out.println(tile);
        }
        return 0l;
    }

    /*

topleft Tile{flipped=false, id=1951, sides=[710, 498, 177, 587], sidesReversed=[841, 564, 318, 397]}
topmidd Tile{flipped=false, id=2311, sides=[210, 89, 924, 318], sidesReversed=[498, 231, 616, 300]}
leftmid Tile{flipped=false, id=2729, sides=[85, 576, 397, 962], sidesReversed=[271, 710, 9, 680]}

     */

    private Tile readTile(Scanner scanner) {
        String idLine = scanner.nextLine();
        boolean stillReading = true;
        List<String> tileData = new ArrayList<>(10);
        while (stillReading && scanner.hasNextLine()) {
            String dataLine = scanner.nextLine();
            if (dataLine.isEmpty() || dataLine.isBlank()) {
                stillReading = false;
            } else {
                String asBinary = dataLine.replace('#','1').replace('.','0');
                tileData.add(asBinary);
            }
        }
        return Tile.create(idLine, tileData);
    }
}

class Tile {
    private boolean flipped = false;
    public final int id;
    private final List<Integer> sides;
    private final List<Integer> sidesReversed;

    private Tile(int id, List<Integer> sides, List<Integer> sidesReversed) {
        this.id = id;
        this.sides = sides;
        this.sidesReversed = sidesReversed;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "flipped=" + flipped +
                ", id=" + id +
                ", sides=" + sides +
                ", sidesReversed=" + sidesReversed +
                '}';
    }

    public static Tile create(String idLine, List<String> tileData) {
        int id = Integer.parseInt(idLine.replace(":","").split(" ")[1]);
        List <String> binarySides = new ArrayList<>(4);
        String topRow = tileData.get(0);
        binarySides.add(topRow);
        binarySides.add(extractVertical(tileData, topRow.length() - 1, false));
        binarySides.add(reverseString(tileData.get(tileData.size()-1)));
        binarySides.add(extractVertical(tileData, 0, true));
        List<Integer> sides = toIntegers(binarySides, false);
        List<Integer> sidesReversed = toIntegers(binarySides, true);
        return new Tile(id,sides,sidesReversed);
    }

    private static List<Integer> toIntegers(List<String> binarySides, boolean reverse) {
        List<Integer> asIntegers = new ArrayList<>(4);
        if (!reverse) {
            for (int i = 0; i < binarySides.size(); i++) {
                asIntegers.add(Integer.parseInt(binarySides.get(i), 2));
            }
        } else {
            for (int i = binarySides.size() - 1; i >= 0; i--) {
                asIntegers.add(Integer.parseInt(reverseString(binarySides.get(i)), 2));
            }
        }
        return asIntegers;
    }

    private static String reverseString(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = input.length() -1; i >= 0; i--) {
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }

    private static String extractVertical(List<String> tileData, int x, boolean readUpwards) {
        StringBuilder sb = new StringBuilder(10);
        if (readUpwards) {
            for (int i = tileData.size() - 1; i >= 0; i--) {
                sb.append(tileData.get(i).charAt(x));
            }
        } else {
            for (int i = 0; i < tileData.size(); i++) {
                sb.append(tileData.get(i).charAt(x));
            }
        }
        return sb.toString();
    }
}
