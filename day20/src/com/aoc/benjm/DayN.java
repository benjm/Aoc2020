package com.aoc.benjm;

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
        Map<Integer, Set<String>> tileSides = new HashMap<>();
        while (scanner.hasNextLine()) {
            Tile tile = readTile(scanner);
            tileMap.put(tile.id, tile);
            tileSides.put(tile.id, tile.getSideStringValues());
        }

        Set<Integer> maybeCorners = new HashSet<>();
        Set<Integer> maybeSides = new HashSet<>();
        Set<Integer> maybeMiddle = new HashSet<>();
        Set<Integer> theRest = new HashSet<>();
        long product = 1l;
        for (int id : tileMap.keySet()) {
            int maybeNeighbours = 0;
            for (int otherId : tileMap.keySet()) {
                if (id != otherId) {
                    final Set<String> sideStrings = tileMap.get(id).getSideStringValues();
                    int startSizeStrings = sideStrings.size();
                    sideStrings.removeAll(tileMap.get(otherId).getSideStringValues());
                    int diffString = (startSizeStrings - sideStrings.size()) / 2;
                    if(diffString > 0) {
                        maybeNeighbours++;
                    }
                }
            }
            if (maybeNeighbours == 2) {
                maybeCorners.add(id);
                product *= id;
            } else if (maybeNeighbours == 3) {
                maybeSides.add(id);
            } else if (maybeNeighbours == 4) {
                maybeMiddle.add(id);
            } else {
                theRest.add(id);
            }
        }

        System.out.println("corners: " + maybeCorners.size()); // 4
        System.out.println("sides  : " + maybeSides.size()); // 40 (4x10)
        System.out.println("middles: " + maybeMiddle.size()); // 100 (10x10)
        System.out.println("theRest: " + theRest.size()); // 0 (nice : no nasty multiple-possibilities

        //start at a corner, build "top"


        if (!isPartTwo && maybeCorners.size() == 4) return product; // TODO this is not ideal...just a clever guess

        return 0l;
    }

    private Tile readTile(Scanner scanner) {
        String idLine = scanner.nextLine();
        boolean stillReading = true;
        List<String> tileData = new ArrayList<>(10);
        while (stillReading && scanner.hasNextLine()) {
            String dataLine = scanner.nextLine();
            if (dataLine.isEmpty()) {
                stillReading = false;
            } else {
                tileData.add(dataLine);
            }
        }
        return new Tile(idLine, tileData);
    }
}

class Tile {
    public final int id;
    private String topLR;
    private String bottomLR;
    private String leftTB;
    private String rightTB;
    private final String startTopLR;
    private final String startBottomLR;
    private final String startLeftTB;
    private final String startRightTB;

    public Tile(final String idLine, final List<String> tileData) {
        id = Integer.parseInt(idLine.replace(":","").split(" ")[1]);
        topLR = tileData.get(0);
        bottomLR = tileData.get(tileData.size() - 1);
        leftTB = readDown(tileData, 0);
        rightTB = readDown(tileData, tileData.get(0).length() - 1);
        startBottomLR = bottomLR;
        startTopLR = topLR;
        startLeftTB = leftTB;
        startRightTB = rightTB;
    }

    public List<String> getTBLRList() {
        List<String> list = new ArrayList<>(4);
        list.add(topLR);
        list.add(bottomLR);
        list.add(leftTB);
        list.add(rightTB);
        return list;
    }

    /*
    orientations...
    I think it's: reset right right right reset+flipH right right right
     */


    public String getTopLR() {
        return topLR;
    }

    public String getBottomLR() {
        return bottomLR;
    }

    public String getLeftTB() {
        return leftTB;
    }

    public String getRightTB() {
        return rightTB;
    }

    public Set<String> getSideStringValues() {
        final Set<String> sideStringValues = new HashSet<>();
        for (String s : getTBLRList()) {
            sideStringValues.add(s);
            sideStringValues.add(reverseString(s));
        }
        return sideStringValues;
    }

    public void reset() {
        bottomLR = startBottomLR;
        topLR = startTopLR;
        leftTB = startLeftTB;
        rightTB = startRightTB;
    }

    public void flipHorizontal() {
        String temp = rightTB;
        rightTB = leftTB;
        leftTB = temp;
        topLR = reverseString(topLR);
        bottomLR = reverseString(bottomLR);
    }

    public void flipVertical() {
        String temp = topLR;
        topLR = bottomLR;
        bottomLR = temp;
        leftTB = reverseString(leftTB);
        rightTB = reverseString(rightTB);
    }

    public void rotate180() {
        String temp = rightTB;
        rightTB = reverseString(leftTB);
        leftTB = reverseString(temp);
        temp = topLR;
        topLR = reverseString(bottomLR);
        bottomLR = reverseString(temp);
    }

    public void rotateRight() {
        String temp = rightTB;
        rightTB = topLR;
        topLR = reverseString(leftTB);
        leftTB = bottomLR;
        bottomLR = reverseString(temp);
    }

    public void rotateLeft() {
        String temp = leftTB;
        leftTB = reverseString(topLR);
        topLR = rightTB;
        rightTB = reverseString(bottomLR);
        bottomLR = temp;
    }

    public static String asOnesAndZeros(String dataLine) {
        return dataLine.replace('#','1').replace('.','0');
    }

    private static String reverseString(String input) {
        return new StringBuilder().append(input).reverse().toString();
//        StringBuilder sb = new StringBuilder();
//        for (int i = input.length() -1; i >= 0; i--) {
//            sb.append(input.charAt(i));
//        }
//        return sb.toString();
    }

    private static String readDown(List<String> tileData, int x) {
        StringBuilder sb = new StringBuilder(tileData.size());
        for (int i = 0; i < tileData.size(); i++) {
            sb.append(tileData.get(i).charAt(x));
        }
        return sb.toString();
    }
}
