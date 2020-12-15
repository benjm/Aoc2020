package com.aoc.benjm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day15 {

    public long partOne(String csv, long finishTurn) {
        long turn = 1l;
        Map<Long, Long> map = new HashMap<>();
        long previous = -1l;
        long nextWillBe = 0l;
        for(String s : csv.replace(" ","").split(",")) {
            previous = Long.parseLong(s);
            if (map.containsKey(previous)) {
                nextWillBe = turn - map.get(previous);
            } else {
                nextWillBe = 0l;
            }
            map.put(previous, turn++);
        }
        while(turn < finishTurn) {
            long diff = 0l;
            if(map.containsKey(nextWillBe)) {
                diff = (turn) - map.get(nextWillBe);
            }
            map.put(nextWillBe, turn);
            nextWillBe = diff;
            turn++;
        }
        return nextWillBe;
    }

    public long partTwo(String filename) {
        Scanner scanner = new Scanner(Day15.class.getResourceAsStream(filename));
        //TODO
        return 0l;
    }
}

class Num {
    /**
     * @param turn1 last turn this was spoken, -1 if not
     * @param turn2 turn before last when this was spoken, -1 if doesn't exist
     */
    public Num(long turn1, long turn2) {
    }
}
