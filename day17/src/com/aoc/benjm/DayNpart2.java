package com.aoc.benjm;

import java.util.*;

public class DayNpart2 {
    private static final char LIVE = '#';
    private static final char DEAD = '.';

    public long run(String filename) {
        Map<String, HyperCube> liveHyperCubes = readStartStateLiveHyperCubes(filename);
        int targetCycle = 6;
        int cycle = 0;
        boolean logGrid = true;
        // all that have at least one live = sum of neighbours
        while (cycle < targetCycle) {
            Map<String, HyperCube> nextHyperCubes = new HashMap<>();
            Set<HyperCube> haveOneOrMoreLiveNeighbours = new HashSet<>();
            for (HyperCube cube : liveHyperCubes.values()) {
                haveOneOrMoreLiveNeighbours.addAll(cube.makeNeighbours());
            }
            for (HyperCube cube : haveOneOrMoreLiveNeighbours) {
                char state = liveHyperCubes.containsKey(cube.id) ? LIVE : DEAD;
                int countOfLiveNeighbours = 0;
                for (String id : cube.getNeighbourIds()) {
                    if (liveHyperCubes.containsKey(id)) countOfLiveNeighbours++;
                }
                if ((state == DEAD && countOfLiveNeighbours == 3)
                        || (state == LIVE && (countOfLiveNeighbours == 2 || countOfLiveNeighbours == 3))) {
                    nextHyperCubes.put(cube.id, cube);
                }
            }
            liveHyperCubes = nextHyperCubes;
            cycle++;
        }
        return liveHyperCubes.size();
    }

    private Map<String, HyperCube> readStartStateLiveHyperCubes(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        int y = 0, z = 0, w = 0;
        Map<String, HyperCube> cubes = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == LIVE) {
                    HyperCube cube = HyperCube.makeHyperCube(x,y,z,w);
                    cubes.put(cube.id,cube);
                }
            }
            y++;
        }
        return cubes;
    }
}
