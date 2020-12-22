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
        LinkedList<Integer> player1 = readPlayer(scanner);
        LinkedList<Integer> player2 = readPlayer(scanner);
        boolean isAKnownState = false; // TODO track state
        while (!player1.isEmpty() && !player2.isEmpty()) {
            if (isAKnownState) {
                return score(player1); // NB might need to poll the hands first - not clear
            }

            int p1 = player1.pollFirst();
            int p2 = player2.pollFirst();

            if (p1 <= player1.size() && p2 <= player2.size()) {
                // play sub game
            } else {

            }
            // normal game
            if(p1 > p2) {
                player1.addLast(p1);
                player1.addLast(p2);
            } else {
                player2.addLast(p2);
                player2.addLast(p1);
            }
        }
        if(player1.isEmpty()) return score(player2);
        return score(player1);
    }

    private long score(final LinkedList<Integer> cards) {
        long score = 0l;
        int size = cards.size();
        for (int i = 0; i < cards.size(); i++) {
            score = score + ((size - i) * cards.get(i));
        }
        return score;
    }

    private LinkedList<Integer> readPlayer(final Scanner scanner) {
        String line = null;
        String name = null;
        LinkedList<Integer> player = new LinkedList<>();
        while(scanner.hasNextLine() && (line == null || !line.isEmpty())) {
            if(line == null && name == null) {
                name = scanner.nextLine(); // not currently used
            } else {
                line = scanner.nextLine();
                if (!line.isEmpty()) {
                    player.addLast(Integer.parseInt(line));
                }
            }
        }
        return player;
    }
}
