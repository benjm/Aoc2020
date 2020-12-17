package com.aoc.benjm;

import java.util.HashMap;
import java.util.Set;

public class Bag {
    public final String id;
    private final HashMap<String, Integer> contains;
    private final boolean canHoldOthers;

    public Bag(String id, HashMap<String, Integer> contains) {
        this.id = id;
        this.contains = contains;
        this.canHoldOthers = !contains.isEmpty();
    }

    public HashMap<String, Integer> getContains() {
        return contains;
    }

    public boolean canHoldOthers() {
        return canHoldOthers;
    }

    public boolean canHold(String bagId) {
        return contains.keySet().contains(bagId);
    }
}
