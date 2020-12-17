package com.aoc.benjm;

import java.util.*;

public class DayN {
    private static final char LIVE = '#';
    private static final char DEAD = '.';

    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return new DayNpart2().run(filename);
    }

    private long run(String filename, boolean isPartTwo) {
        Map<String, Cube> liveCubes = readStartStateLiveCubes(filename);
        int targetCycle = 6;
        int cycle = 0;
        boolean logGrid = true;
        // all that have at least one live = sum of neighbours
        while (cycle < targetCycle) {
            Map<String, Cube> nextCubes = new HashMap<>();
            Set<Cube> haveOneOrMoreLiveNeighbours = new HashSet<>();
            for (Cube cube : liveCubes.values()) {
                haveOneOrMoreLiveNeighbours.addAll(cube.makeNeighbours());
            }
            for (Cube cube : haveOneOrMoreLiveNeighbours) {
                char state = liveCubes.containsKey(cube.id) ? LIVE : DEAD;
                int countOfLiveNeighbours = 0;
                for (String id : cube.getNeighbourIds()) {
                    if (liveCubes.containsKey(id)) countOfLiveNeighbours++;
                }
                if ((state == DEAD && countOfLiveNeighbours == 3)
                        || (state == LIVE && (countOfLiveNeighbours == 2 || countOfLiveNeighbours == 3))) {
                    nextCubes.put(cube.id, cube);
                }
            }
            liveCubes = nextCubes;
            cycle++;
        }
        return liveCubes.size();
    }

    private Map<String, Cube> readStartStateLiveCubes(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        int y = 0, z = 0;
        Map<String, Cube> cubes = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == LIVE) {
                    Cube cube = Cube.makeCube(x,y,z);
                    cubes.put(cube.id,cube);
                }
            }
            y++;
        }
        return cubes;
    }
}
