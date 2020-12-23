package com.aoc.benjm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DayN {
    private boolean isPartTwo = false;

    public DayN partTwo() {
        this.isPartTwo = true;
        return this;
    }

    public String play(final String state, int rounds) {
        LinkedList<Integer> numbers = stateAsLinkedListOfNumbers(state);
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
//            System.out.println("round " + i + " state : " + numbers);
        }
        return orderAfter(1, numbers);
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
        for (int i = 0; i < state.length(); i++) {
            numbers.add(Integer.parseInt(state.substring(i,i+1)));
        }
        return numbers;
    }
}

//current cup, starts at position 0, moves clockwise after the round