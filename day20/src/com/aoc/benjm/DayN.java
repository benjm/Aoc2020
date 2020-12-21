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
        Map<Integer, Set<Integer>> tileSides = new HashMap<>();
//        Map<Integer, Integer> tileOrientationMap = new HashMap<>();
        while (scanner.hasNextLine()) {
            Tile tile = readTile(scanner);
            tileMap.put(tile.id, tile);
            tileSides.put(tile.id, tile.getSideValues());
//            tileOrientationMap.put(tile.id, 0);
            if(true) doLoggingThing(tile);
        }

        System.out.println("MATCHES:");
        for (int id : tileMap.keySet()) {
            StringBuilder sbInts = new StringBuilder();
            StringBuilder sbStrs = new StringBuilder();
            sbInts.append(id + "-int-(" + tileMap.get(id).getSideValues().size() + ")\t: ");
            sbStrs.append(id + "-str-(" + tileMap.get(id).getSideStringValues().size() + ")\t: ");
            for (int otherId : tileMap.keySet()) {
                if (id != otherId) {
                    final Set<Integer> sides = tileMap.get(id).getSideValues();
                    final Set<String> sideStrings = tileMap.get(id).getSideStringValues();
                    int startSize = sides.size();
                    int startSizeStrings = sideStrings.size();
                    sides.removeAll(tileMap.get(otherId).getSideValues());
                    sideStrings.removeAll(tileMap.get(otherId).getSideStringValues());
                    int diff = startSize - sides.size();
                    int diffString = startSizeStrings = sideStrings.size();
                    sbInts.append(diff).append(" ");
                    sbStrs.append(diffString).append(" ");
                }
            }
            System.out.println(sbInts.toString());
            System.out.println(sbStrs.toString());
        }

        return 0l;
    }

    private void doLoggingThing(final Tile tile) {
        Set<String> possibles = new HashSet<>();
        Set<Integer> sides = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            switch(new Random().nextInt(5)) {
                case 0: tile.rotateRight(); break;
                case 1: tile.rotate180(); break;
                case 2: tile.rotateLeft(); break;
                case 3: tile.flipVertical(); break;
                case 4: tile.flipHorizontal(); break;
            }
            possibles.add(tile.getIntAsStringRepresentation());
            sides.addAll(tile.getIntRepresentation());
        }
        System.out.println(possibles.size() + " orientations and " + sides.size() + " LR TB values");
        tile.reset();
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
    private Set<Integer> sideValues = new HashSet<>(8);
//    private Map<Integer, List<Integer>> tileOrientations = new HashMap<>(); // TODO ?

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
        populateOrientationMapEtc();
    }

    private void populateOrientationMapEtc() {
//        tileOrientations.put(0,getIntRepresentation());
        sideValues.addAll(getIntRepresentation());
        sideValues.addAll(getReverseIntRepresentation());
    }

    public String getIntAsStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        for(int i : getIntRepresentation()) {
            sb.append(i + " ");
        }
        return sb.toString();
    }

    public List<String> getTBLRList() {
        List<String> list = new ArrayList<>(4);
        list.add(topLR);
        list.add(bottomLR);
        list.add(leftTB);
        list.add(rightTB);
        return list;
    }

    public List<Integer> getIntRepresentation() {
        List<Integer> list = new ArrayList<>(4);
        list.add(Integer.parseInt(asOnesAndZeros(topLR), 2));
        list.add(Integer.parseInt(asOnesAndZeros(bottomLR), 2));
        list.add(Integer.parseInt(asOnesAndZeros(leftTB), 2));
        list.add(Integer.parseInt(asOnesAndZeros(rightTB), 2));
        return list;
    }

    private List<Integer> getReverseIntRepresentation() {
        List<Integer> list = new ArrayList<>(4);
        list.add(Integer.parseInt(asOnesAndZeros(reverseString(topLR)), 2));
        list.add(Integer.parseInt(asOnesAndZeros(reverseString(bottomLR)), 2));
        list.add(Integer.parseInt(asOnesAndZeros(reverseString(leftTB)), 2));
        list.add(Integer.parseInt(asOnesAndZeros(reverseString(rightTB)), 2));
        return list;
    }

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

    public Set<Integer> getSideValues() {
        return sideValues;
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
