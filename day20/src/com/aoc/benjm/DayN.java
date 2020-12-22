package com.aoc.benjm;

import java.util.*;
import java.util.stream.Collectors;


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
            Tile tile = tileMap.get(id);
            int maybeNeighbours = 0;
            for (int otherId : tileMap.keySet()) {
                if (id != otherId) {
                    final Set<String> sideStrings = tile.getSideStringValues();
                    final Set<String> matchingSides = new HashSet<>();
                    matchingSides.addAll(sideStrings);
                    int startSizeStrings = sideStrings.size();
                    sideStrings.removeAll(tileMap.get(otherId).getSideStringValues());
                    int diffString = (startSizeStrings - sideStrings.size()) / 2;
                    matchingSides.removeAll(sideStrings);
                    if(diffString > 0) {
                        maybeNeighbours++;
                        tile.setMatchingSides(otherId, matchingSides);
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

        final int sideLength = (int) Math.round(Math.sqrt(tileMap.size())); // blind faith...
        //start at a corner, build the rest, tests suggest only one will match
        Tile[][] grid = new Tile[sideLength][sideLength];
        Tile aCorner = tileMap.get(maybeCorners.iterator().next());
        Set<String> cornersides = aCorner.getSidesWithMatchesNonFlipped();
        final List<String> tblrList = aCorner.getTBLRList();
        while (!cornersides.contains(aCorner.getRightTB()) && !cornersides.contains(aCorner.getBottomLR())) {
            aCorner.rotate();
        }
        //ugh ... also will only work for bit grid
        for(int row = 0; row < sideLength; row++) {
            for(int col = 0; col < sideLength; col++) {
                // TODO fill in to right and down at same time, and use if not null skip to move to next
                if (row == 0 && col == 0) {
                    grid[row][col] = aCorner;
                } else if (row == 0) {
                    Tile toLeft = grid[row][col-1];
                    int rotates = 0;
                    boolean found = false;
                    for (Map.Entry<Integer, Set<String>> entry : toLeft.getNeighbourSidesMap().entrySet()) {
                        String leftMatch = toLeft.getRightTB();
                        if (!found && entry.getValue().contains(leftMatch)) {
                            Tile tile = tileMap.get(entry.getKey());
                            if (!tile.getTBLRList().contains(leftMatch)) tile.flip();
                            while (!tile.getLeftTB().equals(leftMatch)) {
                                tile.rotate();
                            }
                            grid[row][col] = tile;
                            found = true;
                        }
                    }
                } else {
                    Tile above = grid[row-1][col];
                    int rotates = 0;
                    boolean found = false;
                    for (Map.Entry<Integer, Set<String>> entry : above.getNeighbourSidesMap().entrySet()) {
                        String topMatch = above.getBottomLR();
                        if (!found && entry.getValue().contains(topMatch)) {
                            Tile tile = tileMap.get(entry.getKey());
                            if (!tile.getTBLRList().contains(topMatch)) tile.flip();
                            while (!tile.getTopLR().equals(topMatch)) {
                                tile.rotate();
                            }
                            grid[row][col] = tile;
                            found = true;
                        }
                    }
                }
            }
        }

        // TODO strip sides, convert to one big List<String>
        // TODO count total '#' while doing this
        int tileSideSize = 8; // magic!
        List<StringBuilder> bigPicture = new ArrayList<>();
        for(int row = 0; row < sideLength; row++) {
            for(int tileRow = 1; tileRow <= tileSideSize; tileRow++) {
                for (int col = 0; col < sideLength; col++) {
                    //TODO get tileRow (orientation)
                }
            }
        }
        // TODO hunt for nessie incl. rotated and flipped patterns until find one, then find total
        // TODO replace with 'O'

        // TODO subtract (num nessies) * (num # that make nessie)


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
    private Map<Integer, Set<String>> neighbourSidesMap = new HashMap<>();
    private boolean flipped = false;
    private int orientation = 0;

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
    orientation ids (flip == id+4) ... if "flipped" rotate rotates left, else rotates right
    0       1       2       3       4                 5      6      7
    reset + right + right + right + (reset + flipH) + left + left + left
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
        flipped = false;
        orientation = 0;
    }

    private void flipHorizontal() {
        String temp = rightTB;
        rightTB = leftTB;
        leftTB = temp;
        topLR = reverseString(topLR);
        bottomLR = reverseString(bottomLR);
    }

    private void flipVertical() {
        String temp = topLR;
        topLR = bottomLR;
        bottomLR = temp;
        leftTB = reverseString(leftTB);
        rightTB = reverseString(rightTB);
    }

    private void rotate180() {
        String temp = rightTB;
        rightTB = reverseString(leftTB);
        leftTB = reverseString(temp);
        temp = topLR;
        topLR = reverseString(bottomLR);
        bottomLR = reverseString(temp);
    }

    public void flip() {
        flipHorizontal();
        incOrientation(4);
        flipped = !flipped;
    }

    public void rotate() {
        if (flipped) rotateLeft();
        else rotateRight();
        incOrientation(1);
    }

    private void incOrientation(final int i) {
        orientation += i;
        while(orientation>7) {
            orientation-=7;
        }
    }

    private void rotateRight() {
        String temp = rightTB;
        rightTB = topLR;
        topLR = reverseString(leftTB);
        leftTB = bottomLR;
        bottomLR = reverseString(temp);
    }

    private void rotateLeft() {
        String temp = leftTB;
        leftTB = reverseString(topLR);
        topLR = rightTB;
        rightTB = reverseString(bottomLR);
        bottomLR = temp;
    }

    private static String asOnesAndZeros(String dataLine) {
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

    public Map<Integer, Set<String>> getNeighbourSidesMap() {
        return neighbourSidesMap;
    }

    final private Set<String> sidesWithMatchesNonFlipped = new HashSet<>();
    public void setMatchingSides(final int otherId, final Set<String> matchingSides) {
        neighbourSidesMap.put(otherId, matchingSides);
        final List<String> nonFlipped = matchingSides.stream().filter(s -> getTBLRList().contains(s)).collect(Collectors.toList());
        sidesWithMatchesNonFlipped.addAll(nonFlipped);
    }

    public Set<String> getSidesWithMatchesNonFlipped() {
        return sidesWithMatchesNonFlipped;
    }
}
