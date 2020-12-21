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

    //ATTEMPT TWO, WITH REGEX
    //...possibly it could work the other way around? ie start with the text and follow the path through the rule tree
    private long run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        boolean readingRules = true;
        Map<Integer, String> rawRules = new HashMap<>();
        Set<String> allowedValues = new HashSet<>();
        long count = 0l;
        String regex = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (readingRules && (line.isEmpty())) {
                regex = convertToRegex(rawRules);
                readingRules = false;
                System.out.println("REGEX: " + regex);
            } else if (readingRules) {
                String split[] = line.split(": ");
                rawRules.put(Integer.parseInt(split[0]), split[1]);
            } else {
                boolean isValid = line.matches(regex);
                System.out.println(line + " (" + isValid + ")");
                if (isValid) {
                    count++;
                }
            }
        }
        return count;
    }

    private String convertToRegex(final Map<Integer, String> rawRules) {
//        for (Integer entry : rawRules.keySet()) {
//            String rule = rawRules.get(entry);
//            if(rule.contains(entry.toString())) {
//                System.out.println("Rule " + entry + " loops to itself: " + rule);
//                final String newRule = findLoop(rule, rawRules);
//                System.out.println("Rule " + entry + " replaced with: " + newRule);
//                rawRules.put(entry, newRule);
//            }
//        }
        String ruleZero = rawRules.get(0);
        System.out.println("start: " + ruleZero);
        int someReplaced = Integer.MAX_VALUE;
        int iteration = 0;
        while (someReplaced > 0) {
            someReplaced = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ruleZero.length(); i++) {
                final char c = ruleZero.charAt(i);
                if (c == ' ' || c == '(' || c == ')' || c == '|' || Character.isLetter(c)) {
                    sb.append(c);
                } else if (c == '+') {
                    sb.append(c);
                } else {
                    StringBuilder next = new StringBuilder();
                    next.append(c);
                    while (i + 1 < ruleZero.length() && Character.isDigit(ruleZero.charAt(i + 1))) {
                        next.append(ruleZero.charAt(i+1));
                        i++;
                    }
                    String nextRule = rawRules.get(Integer.parseInt(next.toString())).replace("\"","");
                    if (nextRule.contains("|")) {
                        sb.append('(').append(nextRule).append(')');
                    } else {
                        sb.append(nextRule);
                    }
                    someReplaced++;
                }
            }
            ruleZero = sb.toString();
        }
        ruleZero = ("^" + ruleZero + "$").replace(" ", "");
        return ruleZero;
    }

    private long runRecursive(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        boolean readingRules = true;
        Map<Integer, String> rawRules = new HashMap<>();
        Set<String> allowedValues = new HashSet<>();
        long count = 0l;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (readingRules && (line.isEmpty())) {
                allowedValues.addAll(getAllowedValues(rawRules));
                readingRules = false;
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

    private List<String> getAllowedValues(Map<Integer, String> rawRules) {
        return processRule(rawRules.get(0), rawRules);
    }

    int branchChecker = 0;
    private List<String> processRule(String rule, Map<Integer, String> rawRules) {
        int branch = branchChecker++;
        if (rule.contains("|")) {
            List<String> orRules = new ArrayList<>();
            for (String orRule : rule.split(" \\| ")) {
                orRules.addAll(processRule(orRule, rawRules));
            }
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
