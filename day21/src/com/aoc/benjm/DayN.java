package com.aoc.benjm;

import java.util.*;

public class DayN {
    private final boolean isPartTwo;
    public DayN(boolean isPartTwo) {
        this.isPartTwo = isPartTwo;
    }

    public long run(String filename) {
        Subgame.resetCounter();
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        LinkedList<Integer> player1 = readPlayer(scanner);
        LinkedList<Integer> player2 = readPlayer(scanner);
        GameResult result = new Subgame(isPartTwo, player1, player2, player1.size(), player2.size()).resolve();
        System.out.println("total (sub)games: " + Subgame.howMany());
        return score(result.winningHand);
    }

    private long score(final List<Integer> cards) {
        long score = 0L;
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

class Subgame {
    private static int counter = 0;
    private final LinkedList<Integer> player1 = new LinkedList<>();
    private final LinkedList<Integer> player2 = new LinkedList<>();
    private final boolean isPartTwo;

    public Subgame(final boolean isPartTwo, List<Integer> player1, List<Integer> player2, int n1, int n2) {
        if (counter % 10000 == 0) {
            System.out.println(counter + " subgames");
        } else if (counter % 500 == 0) {
            System.out.print(".");
        }
        counter++;
        this.isPartTwo = isPartTwo;
        for (int i = 0; i < n1; i++) {
            this.player1.add(player1.get(i));
        }
        for (int i = 0; i < n2; i++) {
            this.player2.add(player2.get(i));
        }
    }

    public static int howMany() {
        return counter;
    }

    public static void resetCounter() {
        counter = 0;
    }

    public GameResult resolve() {
        List<String> previousStates = new ArrayList<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {
            if (isPartTwo) {
                String currentState = makeStateString();
                for (String previous : previousStates) {
                    if (previous.equals(currentState)){
                        return player1Won();
                    }
                }
                previousStates.add(currentState);
            }

            int p1 = player1.pollFirst();
            int p2 = player2.pollFirst();

            boolean p1WinsRound;
            if (isPartTwo && p1 <= player1.size() && p2 <= player2.size()) {
                p1WinsRound = new Subgame(isPartTwo, player1, player2, p1, p2).resolve().player1Won;
            } else {
                p1WinsRound = p1 > p2;
            }

            if(p1WinsRound) {
                player1.addLast(p1);
                player1.addLast(p2);
            } else {
                player2.addLast(p2);
                player2.addLast(p1);
            }
        }
        if (player1.isEmpty()) return player2Won();
        if (player2.isEmpty()) return player1Won();
        throw new RuntimeException("no result found!");
    }

    private String makeStateString() {
        StringBuilder sb = new StringBuilder();
        for(int i : player1) {
            sb.append(i).append(" ");
        }
        sb.append(-1).append(" "); // divider
        for(int i : player2) {
            sb.append(i).append(" ");
        }
        return sb.toString();
    }

    private GameResult player1Won() {
        return new GameResult(true, player1);
    }

    private GameResult player2Won() {
        return new GameResult(false, player2);
    }
}

class GameResult {
    public final boolean player1Won;
    public final List<Integer> winningHand;

    public GameResult (boolean player1Won, List<Integer> winningHand) {
        this.player1Won = player1Won;
        this.winningHand = winningHand;
    }
}
