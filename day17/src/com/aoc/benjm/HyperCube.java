package com.aoc.benjm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HyperCube {
    public final int x, y, z, w;
    public final String id;
    private final Set<String> neighbourIds;

    private HyperCube(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.id = makeId(x,y,z,w);
        this.neighbourIds = makeNeighbourIdsFor(x,y,z,w);
    }

    public Set<String> getNeighbourIds() {
        return neighbourIds;
    }

    public Set<HyperCube> makeNeighbours() {
        return makeHyperCubes(getNeighbourIds());
    }

    @Override
    public String toString() {
        return id;
    }

    private static Set<String> makeNeighbourIdsFor(int x0, int y0, int z0, int w0) {
        final Set<String> set = new HashSet<>(26);
        for (int x = x0 - 1; x <= x0 + 1; x++) {
            for (int y = y0 - 1; y <= y0 + 1; y++) {
                for (int z = z0 - 1; z <= z0 + 1; z++) {
                    for (int w = w0 - 1; w <= w0 + 1; w++) {
                        if (x == x0 && y == y0 && z == z0 && w == w0) {
                            // skip
                        } else {
                            set.add(makeId(x, y, z, w));
                        }
                    }
                }
            }
        }
        return set;
    }

    private static String makeId(int x, int y, int z, int w) {
        return "[" + x + ',' + y +',' + z +',' + w + ']';
    }

    public static HyperCube makeHyperCubeFromId(String id) {
        int[] ids = Arrays.stream(id.substring(1, id.length() - 1).split(",")).mapToInt(s -> Integer.parseInt(s)).toArray();
        return makeHyperCube(ids[0],ids[1],ids[2],ids[3]);
    }

    public static HyperCube makeHyperCube(int x, int y, int z, int w) {
        return new HyperCube(x,y,z,w);
    }

    public static Set<HyperCube> makeHyperCubes(Set<String> ids) {
        Set<HyperCube> cubes = new HashSet<>(ids.size());
        for (String id : ids) cubes.add(makeHyperCubeFromId(id));
        return cubes;
    }
}
