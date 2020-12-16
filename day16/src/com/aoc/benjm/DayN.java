package com.aoc.benjm;

import java.util.ArrayList;
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

    private long run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Set<Integer> megaRule = new HashSet<>();
        long sumFailedMegaRule = 0l;
        List<Integer> myTicket = new ArrayList<>();
        Set<List<Integer>> otherTickets = new HashSet<>();
        Map<String, Set<Integer>> rules = new HashMap<>();
        int stage = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                stage++;
            } else if (stage == 0) {
                String lineSplit[] = line.split(": ");
                String ruleId = lineSplit[0];
                Set<Integer> validValues = new HashSet<>();
                for (String range : lineSplit[1].split(" or ")) {
                    String minmax[] = range.split("-");
                    int min = Integer.parseInt(minmax[0]);
                    int max = Integer.parseInt(minmax[1]);
                    for (int i = min; i <= max; i++) {
                        megaRule.add(i);
                        validValues.add(i);
                    }
                }
                rules.put(ruleId, validValues);
            } else if (stage == 1 && !line.startsWith("your")) {
                myTicket = readTicket(line);
            } else if (stage == 2 && !line.startsWith("nearby")) {
                List<Integer> ticket = readTicket(line);
                long invalidField = checkForInvalidField(ticket,megaRule);
                if (invalidField >= 0) {
                    sumFailedMegaRule += invalidField;
                } else {
                    otherTickets.add(ticket);
                }
            }
        }
        if (!isPartTwo) {
            return sumFailedMegaRule;
        }

        /*
        for each position in ticket, get all ticket values, see which rules they could be (always just one?)
         */
        Map<Integer, Set<String>> possibleMatches = new HashMap<>();
        for (int i = 0; i < myTicket.size(); i++) {
            Map<String, Set<Integer>> filteredRules = new HashMap<>();
            for (String ruleId : rules.keySet()) {
                filteredRules.put(ruleId, rules.get(ruleId));
            }
            Set<Integer> ticketFieldNValues = new HashSet<>();
            ticketFieldNValues.add(myTicket.get(i));
            for (List<Integer> ticket : otherTickets) {
                ticketFieldNValues.add(ticket.get(i));
            }
            Set<String> matchingRules = getMatchingRules(ticketFieldNValues, filteredRules);
            possibleMatches.put(i, matchingRules);
        }
        Map<String, Integer> rulePosition = new HashMap<>();
        final int total = possibleMatches.size();
        while (rulePosition.size() != total) {
            Set<String> foundIds = new HashSet<>();
            for (Integer position : possibleMatches.keySet()) {
                Set<String> possibles = possibleMatches.get(position);
                if (possibles.size() == 1) {
                    String ruleId = possibles.iterator().next();
                    foundIds.add(ruleId);
                    rulePosition.put(ruleId, position);
                }
            }
            if (foundIds.isEmpty()) {
                throw new RuntimeException("no more singles found...");
            }
            for (String foundId : foundIds) {
                Set<Integer> fields = new HashSet<>();
                for(Integer position : possibleMatches.keySet()) {
                    Set<String> values = possibleMatches.get(position);
                    values.remove(foundId);
                    if(values.isEmpty()) {
                        fields.add(position);
                    }
                }
                for(Integer position : fields) {
                    possibleMatches.remove(position);
                }
            }
        }

        long departureProduct = 1l;
        for (String ruleId : rulePosition.keySet()) {
            if (ruleId.startsWith("departure")) {
                departureProduct *= myTicket.get(rulePosition.get(ruleId));
            }
        }
        return departureProduct;
    }

    private Set<String> getMatchingRules(final Set<Integer> values, final Map<String, Set<Integer>> rules) {
        Set<String> possibleRules = new HashSet<>();
        for (String ruleId : rules.keySet()) {
            final Set<Integer> validValues = rules.get(ruleId);
            boolean allFound = true;
            for (Integer value : values) {
                if (!validValues.contains(value)) {
                    allFound = false;
                    break;
                }
            }
            if (allFound) {
                possibleRules.add(ruleId);
            }
        }
        return possibleRules;
    }

    private List<Integer> readTicket(final String line) {
        String values[] = line.split(",");
        List<Integer> ticket = new ArrayList<>(values.length);
        for(String s : values) {
            int val = Integer.parseInt(s);
            ticket.add(val);
        }
        return ticket;
    }

    private long checkForInvalidField(final List<Integer> ticket, final Set<Integer> megaRule) {
        for (int field : ticket) {
            if (!megaRule.contains(field)) {
                return field;
            }
        }
        return -1l;
    }
}
