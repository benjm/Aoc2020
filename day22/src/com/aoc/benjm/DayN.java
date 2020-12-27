package com.aoc.benjm;

import java.util.*;

/**
 * CRAB CUPS
 */
public class DayN {
    private static final int oneMillion = 1000000;
    private boolean isPartTwo = false;
    private int maxNumber = 9;

    public DayN partTwo() {
        this.isPartTwo = true;
        maxNumber = oneMillion;
        return this;
    }

    public String play(final String state, final int rounds) {
        Map<Integer, Integer> mapToNextCup = stateAsMapNextCup(state);
        int head = Integer.parseInt(state.substring(0,1));
        int excluded[] = new int[3];
        for (int round = 0; round < rounds; round++) {
            excluded[0] = mapToNextCup.get(head);
            excluded[1] = mapToNextCup.get(excluded[0]);
            excluded[2] = mapToNextCup.get(excluded[1]);
            mapToNextCup.put(head, mapToNextCup.get(excluded[2]));
            int dest = head - 1;
            if (dest < 1) dest = maxNumber;
            while (dest == excluded[0] || dest == excluded[1] || dest == excluded[2]) {
                dest--;
                if (dest < 1) dest = maxNumber;
            }
            head = mapToNextCup.get(excluded[2]);
            mapToNextCup.put(excluded[2], mapToNextCup.get(dest));
            mapToNextCup.put(excluded[1], excluded[2]);
            mapToNextCup.put(excluded[0], excluded[1]);
            mapToNextCup.put(dest, excluded[0]);
        }

        StringBuilder sb = new StringBuilder();
        if (isPartTwo) {
            int first = mapToNextCup.get(1);
            int second = mapToNextCup.get(first);
            long product = (long) first * (long) second;
            sb.append(product);
        } else {
            int next = mapToNextCup.get(1);
            while(next != 1) {
                sb.append(next);
                next = mapToNextCup.get(next);
            }
        }
        return sb.toString();
    }

    private Map<Integer,Integer> stateAsMapNextCup(final String state) {
        List<Integer> list = new ArrayList<>(9);
        for (int i = 0; i < state.length(); i++) {
            list.add(Integer.parseInt(state.substring(i,i+1)));
        }

        Map<Integer,Integer> mapToNext = new HashMap<>(maxNumber);
        int prev = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            int cup = list.get(i);
            mapToNext.put(prev, cup);
            prev = cup;
        }

        if (isPartTwo) {
            for (int cup = 10; cup <= oneMillion; cup++) {
                mapToNext.put(prev,cup);
                prev = cup;
            }
        }

        mapToNext.put(prev, list.get(0));
        return mapToNext;
    }
}