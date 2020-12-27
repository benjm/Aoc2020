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

    private String playAgain(final String state, final int rounds) {
        Map<Integer, Integer> numberToPosition = stateAsMapNumPositions(state);
        Map<Integer, Integer> positionToNumber = reverseMapping(numberToPosition);
        int excluded[] = new int[3];
        long start = System.currentTimeMillis();
        int index = 0;
        for (int round = 0; round < rounds; round++) {
            excluded[0] = positionToNumber.get(incrementIndex(index, 1));
            excluded[1] = positionToNumber.get(incrementIndex(index, 2));
            excluded[2] = positionToNumber.get(incrementIndex(index, 3));

            int nextNumber = positionToNumber.get(index) - 1;
            if (nextNumber == 0) nextNumber = maxNumber;
            while (nextNumber == excluded[0] || nextNumber == excluded[1] || nextNumber == excluded[2]) {
                nextNumber--;
                if(nextNumber <= 0) nextNumber = maxNumber;
            }

            int destinationIndex = numberToPosition.get(nextNumber);

            if (destinationIndex < index) {
                for (int i = incrementIndex(index, 1); i < maxNumber && i > index; i++) {
                    int number = positionToNumber.get(incrementIndex(i,3));
                    positionToNumber.put(i,number);
                    numberToPosition.put(number,i);
                }
                for (int i = 0; i < destinationIndex + 1; i++) {
                    int number = positionToNumber.get(incrementIndex(i,3));
                    positionToNumber.put(i,number);
                    numberToPosition.put(number,i);
                }
            } else {
                for (int i = index + 1; i < destinationIndex + 1; i++) {
                    int number = positionToNumber.get(incrementIndex(i,3));
                    positionToNumber.put(i,number);
                    numberToPosition.put(number,i);
                }
            }

            destinationIndex = numberToPosition.get(nextNumber);
            int newIndex[] = new int[3];
            newIndex[0] = incrementIndex(destinationIndex, 1);
            newIndex[1] = incrementIndex(destinationIndex, 2);
            newIndex[2] = incrementIndex(destinationIndex, 3);
            numberToPosition.put(excluded[0], newIndex[0]);
            numberToPosition.put(excluded[1], newIndex[1]);
            numberToPosition.put(excluded[2], newIndex[2]);
            positionToNumber.put(newIndex[0], excluded[0]);
            positionToNumber.put(newIndex[1], excluded[1]);
            positionToNumber.put(newIndex[2], excluded[2]);
            index = incrementIndex(index, 1);
        }
        if (isPartTwo) throw new RuntimeException("monkeys"); //return multiplyTwoAfter(1, numbers);

        int posOfOne = numberToPosition.get(1);
        StringBuilder sb = new StringBuilder(8);
        int counter = 1;
        while (counter < maxNumber) {
            sb.append(positionToNumber.get(incrementIndex(posOfOne, counter)));
            counter++;
        }
        return sb.toString();
    }

    private int incrementIndex(int index, int delta) {
        int out = index + delta;
        if (out >= maxNumber) return out - maxNumber;
        return out;
    }

    private Map<Integer, Integer> reverseMapping(Map<Integer, Integer> input) {
        Map<Integer, Integer> reverse = new HashMap<>(input.size());
        for(Map.Entry<Integer, Integer> entry : input.entrySet()) {
            reverse.put(entry.getValue(), entry.getKey());
        }
        return reverse;
    }

    private Map<Integer,Integer> stateAsMapNumPositions(final String state) {
        Map<Integer,Integer> numbers = isPartTwo ? new HashMap<>(oneMillion) : new HashMap<>(10);
        for (int i = 0; i < state.length(); i++) { // pad to dr evil standards
            numbers.put(Integer.parseInt(state.substring(i,i+1)), i);
        }
        if (isPartTwo) {
            for (int i = 10; i <= oneMillion; i++) {
                numbers.put(i,i);
            }
        }
        return numbers;
    }

    public String play(final String state, int rounds) {
        if (true) return playAgain(state, rounds);
        //TODO need to rethink part two ... this version looks like it'd take over 2 *days* ... maybe using HashMap(s) for index
        LinkedList<Integer> numbers = stateAsLinkedListOfNumbers(state);
        long start = System.currentTimeMillis();
        for (int i = 0; i < rounds; i++) {
            //get current val and move to end
            int current = numbers.pollFirst();
            numbers.add(current);
            //save the next three
            LinkedList<Integer> saved = new LinkedList<>();
            saved.add(numbers.pollFirst());
            saved.add(numbers.pollFirst());
            saved.add(numbers.pollFirst());
            //index of next
            int nextIndex = -1;
            while (nextIndex < 0) {
                current--;
                if(current < 1) current = 9;
                nextIndex = numbers.indexOf(current); // wrap around
            }
            int insertAt = nextIndex + 1;
            if(insertAt == numbers.size()) insertAt = 0;
            //push saved back in
            for (int t = saved.size(); t >0; t--) {
                numbers.add(insertAt, saved.pollLast());
            }
            if(isPartTwo && i > 0 && i % 5000 == 0) {
                long now = System.currentTimeMillis();
                long secondsForLastBatch = ((now - start)/1000);
                start = now;
                long minToEnd = (((rounds - i) / 5000) * secondsForLastBatch)/60;
                System.out.println("round " + i + " last 5k took " + secondsForLastBatch + "s and approx. " + minToEnd + " minutes to finish");
            }
        }
        if (isPartTwo) return multiplyTwoAfter(1, numbers);
        return orderAfter(1, numbers);
    }

    private String multiplyTwoAfter(final int startVal, final LinkedList<Integer> numbers) {
        int startIndex = numbers.indexOf(startVal);
        int a = startIndex + 1;
        int b = startIndex + 2;
        if(a >= numbers.size()) a -= numbers.size();
        if(b >= numbers.size()) b -= numbers.size();
        return String.valueOf(numbers.get(a) * numbers.get(b));
    }

    private String orderAfter(final int startVal, final LinkedList<Integer> numbers) {
        int startIndex = numbers.indexOf(startVal);
        StringBuilder sb = new StringBuilder(10);
        for (int i : numbers.subList(startIndex + 1, numbers.size())) {
            sb.append(i);
        }
        for (int i : numbers.subList(0, startIndex)) {
            sb.append(i);
        }
        return sb.toString();
    }

    private LinkedList<Integer> stateAsLinkedListOfNumbers(final String state) {
        LinkedList<Integer> numbers = new LinkedList<>();
        for (int i = 0; i < state.length(); i++) { // pad to dr evil standards
            numbers.add(Integer.parseInt(state.substring(i,i+1)));
        }
        if (isPartTwo) {
            for (int i = 10; i <= oneMillion; i++) {
                numbers.add(i);
            }
        }
        return numbers;
    }
}

//current cup, starts at position 0, moves clockwise after the round