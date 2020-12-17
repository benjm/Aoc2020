package com.aoc.benjm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Cube {
    public final int x, y, z;
    public final String id;
    private final Set<String> neighbourIds;
    
    private Cube(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = makeId(x,y,z);
        this.neighbourIds = makeNeighbourIdsFor(x,y,z);
    }

    public Set<String> getNeighbourIds() {
        return neighbourIds;
    }

    public Set<Cube> makeNeighbours() {
        return makeCubes(getNeighbourIds());
    }

    @Override
    public String toString() {
        return id;
    }

    private static Set<String> makeNeighbourIdsFor(int x0, int y0, int z0) {
        final Set<String> set = new HashSet<>(26);
        for (int x = x0 - 1; x <= x0 + 1; x++) {
            for (int y = y0 - 1; y <= y0 + 1; y++) {
                for (int z = z0 - 1; z <= z0 + 1; z++) {
                    if (x == x0 && y == y0 && z == z0) {
                        // skip
                    } else {
                        set.add(makeId(x,y,z));
                    }
                }
            }
        }
        return set;
    }

    private static String makeId(int x, int y, int z) {
        return "[" + x + ',' + y +',' + z + ']';
    }

    public static Cube makeCubeFromId(String id) {
        int[] ids = Arrays.stream(id.substring(1, id.length() - 1).split(",")).mapToInt(s -> Integer.parseInt(s)).toArray();
        return makeCube(ids[0],ids[1],ids[2]);
    }

    public static Cube makeCube(int x, int y, int z) {
        return new Cube(x,y,z);
    }

    public static Set<Cube> makeCubes(Set<String> ids) {
        Set<Cube> cubes = new HashSet<>(ids.size());
        for (String id : ids) cubes.add(makeCubeFromId(id));
        return cubes;
    }
}
