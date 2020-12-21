package com.aoc.benjm;

import java.util.*;

public class DayN {
    // the number of messages that completely match rule 0
    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        boolean readingRules = true;
        Map<Integer, String> rawRules = new HashMap<>();
        Set<String> allowedValues = new HashSet<>();
        long count = 0l;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (readingRules && (line.isEmpty() || line.isBlank())) {
                allowedValues.addAll(getAllowedValues(rawRules));
                readingRules = false;
                System.out.println("ALLOWED:");
                for (String r : allowedValues) {
                    System.out.println(r);
                }
            } else if (readingRules) {
                String split[] = line.split(": ");
                rawRules.put(Integer.parseInt(split[0]), split[1]);
            } else {
                boolean isValid = allowedValues.contains(line);
                System.out.println(line + " (" + isValid + ")");
                if (isValid) {
                    count++;
                }
            }
        }
        return count;
    }

    /*
        0: 4 1 5
        1: 2 3 | 3 2
        2: 4 4 | 5 5
        3: 4 5 | 5 4
        4: "a"
        5: "b"

        ababbb
        bababa
        abbbab
        aaabbb
        aaaabbb
     */
    private List<String> getAllowedValues(Map<Integer, String> rawRules) {
        return processRule(rawRules.get(0), rawRules);
    }

    int branchChecker = 0;
    private List<String> processRule(String rule, Map<Integer, String> rawRules) {
        int branch = branchChecker++;
        System.out.println("("+branch+")rule: " + rule);
        if (rule.contains("|")) {
            List<String> orRules = new ArrayList<>();
            for (String orRule : rule.split(" \\| ")) {
                orRules.addAll(processRule(orRule, rawRules));
            }
            System.out.print("(" + branch + ") returning: ");
            for(String orRule : orRules) {
                System.out.print("\"" + orRule + "\", ");
            }
            System.out.println();
            return orRules;
        }
        int countReplaced = 0;
        List<String> toReturn = new ArrayList<>();
        for (String s : rule.split(" ")) {
            if (s.contains("\"") || Character.isLetter(s.charAt(0))) {
                String aLetter = s.replace("\"","");
                //tag to end of every string in toReturn
                toReturn = addToEnds(toReturn, aLetter);
            } else { // must (hopefully!) be a number...
                //process value of number
                String ss = rawRules.get(Integer.parseInt(s));
                for (String someLetters : processRule(ss, rawRules)) {
                    List<String> evenMore = new ArrayList<>();
                    evenMore = addToEnds(toReturn, someLetters);
                    toReturn.addAll(evenMore);
                }
                countReplaced++;
            }
        }
        if (countReplaced == 0) {
            //TODO
        }
        System.out.print("(" + branch + ") returning: ");
        for(String orRule : toReturn) {
            System.out.print("\"" + orRule + "\", ");
        }
        System.out.println();
        return toReturn;
    }

    private List<String> addToEnds(List<String> strings, String toAppend) {
        List<String> newStrings = new ArrayList<>();
        if (strings.isEmpty()) {
            newStrings.add(toAppend);
        } else {
            for (String s : strings) {
                newStrings.add(s + toAppend);
            }
        }
        return newStrings;
    }
}
