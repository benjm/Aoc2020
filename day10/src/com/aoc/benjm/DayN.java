package com.aoc.benjm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class DayN {
    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(final String filename, final boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        List<Integer> list = new ArrayList<>();
        while(scanner.hasNext()) {
            int next = scanner.nextInt();
            list.add(next);
        }
        if (!isPartTwo) {
            return onesAndThrees(list);
        }
        return countDistinctArrangements(list);
    }

    private long onesAndThrees(final List<Integer> list) {
        list.add(0);
        list.sort(Comparator.naturalOrder());
        int max = list.get(list.size()-1) + 3;
        list.add(max);
        int ones = 0;
        int threes = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            int diff = list.get(i+1) - list.get(i);
            if (diff == 1) {
                ones++;
            } else if (diff == 3) {
                threes++;
            }
        }
        return ones * threes;
    }

    private long countDistinctArrangements(final List<Integer> list) {
        list.add(0);
        list.sort(Comparator.naturalOrder());
        list.add(list.get(list.size()-1) + 3);
        List<Integer> onwardPaths = new ArrayList<>(list.size());
        Map<Integer, Set<Integer>> actualOnwardsOptions = new HashMap<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            int x0 = list.get(i);
            int count = 0;
            Set<Integer> options = new HashSet<>(3);
            for (int j = i + 1; j <= i + 3; j++) {
                if (j < list.size() && list.get(j) - x0 <= 3) {
                    count++;
                    options.add(j);
                }
            }
            onwardPaths.add(count);
            actualOnwardsOptions.put(i, options);
        }
        int prev = 0;
        List<Integer> cluster = new ArrayList<>();
        long product = 1l;
        for (int i = 0; i < list.size(); i++) {
            int mine = onwardPaths.get(i);
            cluster.add(i);
            if (prev == 1) {
                product *= countClusterDistinctPaths(cluster, actualOnwardsOptions);
                cluster.clear();
                cluster.add(i);
            }
            prev = mine;
        }
        product *= countClusterDistinctPaths(cluster, actualOnwardsOptions);
        return product;
    }

    private long countClusterDistinctPaths(final List<Integer> cluster,
                                           final Map<Integer, Set<Integer>> actualOnwardsOptions) {
        if (cluster.isEmpty()) return 1l;
        Integer last = cluster.get(cluster.size() - 1);
        List<Integer> pathEnds = new ArrayList<>();
        long count = 0l;
        boolean carryOn = true;
        pathEnds.add(cluster.get(0));
        while (carryOn) {
            List<Integer> newPathEnds = new ArrayList<>();
            for (Integer pathEnd : pathEnds) {
                if (pathEnd == last) {
                    count++;
                } else {
                    // update possible paths
                    for (int option : actualOnwardsOptions.get(pathEnd)) {
                        newPathEnds.add(option);
                    }
                }
            }
            pathEnds = newPathEnds;
            if (newPathEnds.isEmpty()) carryOn = false;
        }
        return count;
    }

    private void log(final List<Integer> cluster, final Object count) {
        StringBuilder sb = new StringBuilder();
        sb.append("cluster: ");
        for (int i : cluster) {
            sb.append(i);
            sb.append(" ");
        }
        sb.append(" --> count: " + count);
        System.out.println(sb.toString());
    }
}
