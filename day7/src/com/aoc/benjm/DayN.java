package com.aoc.benjm;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

public class DayN {

    private final String MYBAGCOLOUR = "shiny gold";

    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(String filename, boolean isPartTwo) {
        Map<String, Bag> bagMap = readBags(filename);
        if (!isPartTwo) return countContainMine(bagMap);
        //PART TWO
        Map<String, Integer> bagIdCounter = new HashMap<>();
        long counter = 0l;
        Map<String, Integer> checkContentsOf = new HashMap<>();
        checkContentsOf.put(MYBAGCOLOUR, 1); // there is one of my bags ... and I do not count it
        while (!checkContentsOf.isEmpty()) {
            Map<String, Integer> nextCheckContentsOf = new HashMap<>();
            for (String bagId : checkContentsOf.keySet()) {
                // contents : type, num*vlue
                Bag bag = bagMap.get(bagId);
                HashMap<String, Integer> contentsOfOne = bag.getContains();
                //do we need to check if there is a closed loop? Let's assume no for starters
                //(ie whether something this bag contains, eventually contains this bag)
                for (String containedBagId : contentsOfOne.keySet()) {
                    int numPerBag = contentsOfOne.get(containedBagId);
                    int totalNumberOfThese = numPerBag * checkContentsOf.get(bagId);
                    counter += totalNumberOfThese;
                    if (nextCheckContentsOf.containsKey(containedBagId)) {
                        totalNumberOfThese += nextCheckContentsOf.get(containedBagId);
                    }
                    nextCheckContentsOf.put(containedBagId, totalNumberOfThese);
                }
            }
            //increment counter by total contained
            checkContentsOf = nextCheckContentsOf;
        }
        return counter;
    }

    // PART ONE
    private long countContainMine(Map<String, Bag> bagMap) {
        Set<String> canContainMine = new HashSet<>();
        boolean finished = false;
        // first ignore my bag and any bags that cannot hold anything else
        Set<String> bagsToCheck = bagMap.keySet().stream().filter(bagId -> bagMap.get(bagId).canHoldOthers() && !bagId.equals(MYBAGCOLOUR)).collect(Collectors.toSet());
        Set<String> checkedBags = new HashSet<>();
        Set<String> checkWhatHolds = new HashSet<>();
        checkWhatHolds.add(MYBAGCOLOUR);
        while(!finished) {
            Set<String> checkWhatHoldsNext = new HashSet<>();
            for (String bagId : checkWhatHolds) {
                for (String bagHoldingBagId : bagsToCheck) {
                    if (!checkedBags.contains(bagHoldingBagId) && bagMap.get(bagHoldingBagId).canHold(bagId)) {
                        checkWhatHoldsNext.add(bagHoldingBagId);
                        canContainMine.add(bagHoldingBagId);
                    }
                }
                checkedBags.add(bagId);
            }
            checkWhatHolds = checkWhatHoldsNext;
            if (checkWhatHolds.isEmpty()) finished = true;
        }
        return canContainMine.size();
    }

    private Map<String, Bag> readBags(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Map<String, Bag> bagMap = new HashMap<>();
        while(scanner.hasNextLine()) {
            String line[] = scanner.nextLine().split(" bags contain ");
            String bagId = line[0];
            HashMap<String, Integer> contains = new HashMap<>();
            if (!line[1].equals("no other bags.")) {
                String csv[] = line[1].split(", ");
                for (String input : csv) {
                    //String input = "1 fish 2 fish red fish blue fish";
                    Scanner s = new Scanner(input);
                    int num = s.nextInt();
                    String rest = s.nextLine().trim();
                    String containedId = rest.split(" bag")[0];
                    contains.put(containedId, num);
                    s.close();
                }
            }
            bagMap.put(bagId, new Bag(bagId, contains));
        }
        return bagMap;
    }
}
