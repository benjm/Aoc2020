package com.aoc.benjm;

import java.util.*;

public class DayN {
    public Result partOne(String filename) {
        return run(filename, false);
    }

    public Result partTwo(String filename) {
        return run(filename, true);
    }

    private Result run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Map<String, Integer> allIngredients = new HashMap<>();
        Set<String> allAllergens = new HashSet<>();
        Map<String, Set<String>> possibleAllergenMapping = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line[] = scanner.nextLine().replace(")","").split(" \\(contains ");
            Set<String> ingredients = new HashSet<>();
            for(String ingredient : line[0].split(" ")) {
                ingredients.add(ingredient);
                if (allIngredients.containsKey(ingredient)) {
                    allIngredients.put(ingredient, allIngredients.get(ingredient)+1);
                } else {
                    allIngredients.put(ingredient, 1);
                }
            }
            for (String allergen : line[1].split(", ")) {
                allAllergens.add(allergen);
                Set<String> possibles;
                if(possibleAllergenMapping.containsKey(allergen)) {
                    possibles = possibleAllergenMapping.get(allergen);
                    Set<String> newPossibles = new HashSet<>();
                    for (String ingredient : ingredients) {
                        if (possibles.contains(ingredient)) {
                            newPossibles.add(ingredient);
                        }
                    }
                    possibles = newPossibles;
                } else {
                    possibles = ingredients;
                }
                possibleAllergenMapping.put(allergen,possibles);
            }
        }
        Map<String, String> allergenToIngredient = new HashMap<>();
        while (allergenToIngredient.size() < possibleAllergenMapping.size()) {
            Map<String, String> newlyFound = new HashMap<>();
            for (String allergenPossible : possibleAllergenMapping.keySet()) {
                if (!allergenToIngredient.containsKey(allergenPossible) && possibleAllergenMapping.get(allergenPossible).size() == 1) {
                    String onlyOneInList = possibleAllergenMapping.get(allergenPossible).iterator().next();
                    allergenToIngredient.put(allergenPossible, onlyOneInList);
                    newlyFound.put(allergenPossible, onlyOneInList);
                }
            }
            for (String allergen : allAllergens) {
                if (!allergenToIngredient.containsKey(allergen)) {
                    Set<String> possibles = possibleAllergenMapping.get(allergen);
                    possibles.removeAll(newlyFound.values()); // remove all newly found values from the lists of possibles of others
                }
            }
        }
        long count = 0l;
        Set<String> allergenicIngredients = new HashSet<>();
        allergenicIngredients.addAll(allergenToIngredient.values());
        for (Map.Entry<String, Integer> ingredientCount : allIngredients.entrySet()) {
            if(!allergenicIngredients.contains(ingredientCount.getKey())) {
                count += ingredientCount.getValue();
            }
        }

        List<String> allergens = new ArrayList<>();
        allergens.addAll(allAllergens);
        allergens.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allergens.size(); i++) {
            sb.append(allergenToIngredient.get(allergens.get(i)));
            if (i + 1 < allergens.size()) sb.append(",");
        }
        String canonical = sb.toString();
        System.out.println(canonical);
        return new Result(count, canonical);
    }
}

class Result {
    public final long count;
    public final String canonical;
    public Result(long count, String canonical) {
        this.count = count;
        this.canonical = canonical;
    }
}
