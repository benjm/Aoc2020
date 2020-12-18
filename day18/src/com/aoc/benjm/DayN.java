package com.aoc.benjm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DayN {
    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        long sumTotal = 0l;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String tokenArray[] = line.replace("(","( ").replace(")"," )").split(" ");
            List<String> tokens;
            tokens = Arrays.asList(tokenArray);
            sumTotal += eval(tokens, isPartTwo);
        }
        return sumTotal;
    }

    private long eval(List<String> tokens, boolean isPartTwo) {
        if (tokens.size() == 1) return Integer.parseInt(tokens.get(0));
        int bracketCount = 0;
        List<String> subBracket = new ArrayList<>();
        List<String> bracketsEvaluated = new ArrayList<>();
        for(String token : tokens) {
            if(token.equals("(")) {
                if (bracketCount > 0) {
                    subBracket.add(token);
                }
                bracketCount++;
            } else if(token.equals(")")) {
                bracketCount--;
                if (bracketCount > 0) {
                    subBracket.add(token);
                } else {
                    bracketsEvaluated.add(String.valueOf(eval(subBracket, isPartTwo)));
                    subBracket.clear();
                }
            } else if (bracketCount > 0) {
                subBracket.add(token);
            } else {
                bracketsEvaluated.add(token);
            }
        }
        // evaluation left to right
        // if (isPartTwo) do + before *
        if (isPartTwo) return evalPartTwoFinal(bracketsEvaluated);
        int index = 0;
        Long result = Long.valueOf(bracketsEvaluated.get(index++));
        while (index < bracketsEvaluated.size() - 1) {
            String function = bracketsEvaluated.get(index++);
            Long next = Long.parseLong(bracketsEvaluated.get(index++));
            if (function.equals("+")) {
                result += next;
            } else if (function.equals("*")) {
                result *= next;
            } else {
                throw new RuntimeException("oops ... " + function + " is not a function...?");
            }
        }
        return result;
    }

    private long evalPartTwoFinal(List<String> tokens) {
        List<String> noPluses = new ArrayList<>();
        if (tokens.contains("+")) {
            int index = 0;
            String left = tokens.get(0);
            for (int i = 0; i < tokens.size() - 2; i+=2) {
                String function = tokens.get(i+1);
                String right = tokens.get(i+2);
                if (function.equals("+")) {
                    Long plusResult = Long.parseLong(left) + Long.parseLong(right);
                    left = String.valueOf(plusResult);
                    if (i+2 == tokens.size() - 1) {
                        noPluses.add(left);
                    }
                } else {
                    // copy i and i+1
                    noPluses.add(left);
                    noPluses.add(function);
                    if (i+2 == tokens.size() - 1) {
                        noPluses.add(right);
                    }
                    left = right;
                }
            }
        } else {
            noPluses = tokens;
        }

        int index = 0;
        Long result = Long.valueOf(noPluses.get(index++));
        while (index < noPluses.size() - 1) {
            String function = noPluses.get(index++);
            Long next = Long.parseLong(noPluses.get(index++));
            if (function.equals("+")) {
                result += next;
            } else if (function.equals("*")) {
                result *= next;
            } else {
                throw new RuntimeException("oops ... " + function + " is not a function...?");
            }
        }
        return result;
    }
}
