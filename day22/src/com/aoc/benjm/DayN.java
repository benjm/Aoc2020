package com.aoc.benjm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DayN {
    private boolean isPartTwo = false;
    private static final int oneMillion = 1000000;

    public DayN partTwo() {
        this.isPartTwo = true;
        return this;
    }

    public String play(final String state, int rounds) {
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