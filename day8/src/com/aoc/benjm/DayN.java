package com.aoc.benjm;

import java.util.*;
import java.util.stream.Collectors;

public class DayN {
    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(String filename, boolean isPartTwo) {
        Programme programme = readProgramme(filename);
        long accAfterLoop = programme.runUntilLoop();
        if (!isPartTwo) return accAfterLoop;
        // TODO
        Map<Integer, Cmd> cmdsUntilLoop = programme.getCommandsUntilLoop();
        Set<Integer> nopAndJmpIds = cmdsUntilLoop.entrySet().stream()
                .filter(entry -> entry.getValue().cmd.equals("nop") || entry.getValue().cmd.equals("jmp"))
                .map(entry -> entry.getKey()).collect(Collectors.toSet());
        return bruteForceIt(programme, nopAndJmpIds);
    }

    private long bruteForceIt(Programme programme, Set<Integer> nopAndJmpIds) {
        programme.reset();
        Integer lastChanged = -1;
        Iterator<Integer> iterator = nopAndJmpIds.iterator();
        while (programme.hasLoop()) {
            programme.reset();
            if (lastChanged >= 0) programme.flip(lastChanged); // flipItBack
            lastChanged = iterator.next();
            programme.flip(lastChanged);
        }
        return programme.getAcc();
    }

    private Programme readProgramme(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Integer index = 0;
        Map<Integer, Cmd> cmdMap = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line[] = scanner.nextLine().split(" ");
            int val = Integer.parseInt(line[1].replace("+", ""));
            cmdMap.put(index++, new Cmd(line[0],val));
        }
        return new Programme(cmdMap);
    }
}

class Programme {
    private Map<Integer, Cmd> cmdMap = new HashMap<>();
    private int counter = 0;
    private long acc = 0;

    public Map<Integer, Cmd> getCommandsUntilLoop() {
        return commandsUntilLoop;
    }

    public void update(Integer counter, Cmd cmd) {
        cmdMap.put(counter, cmd);
    }

    private Map<Integer, Cmd> commandsUntilLoop = new HashMap<>();

    public Programme(Map<Integer, Cmd> cmdMap) {
        this.cmdMap = cmdMap;
    }

    public void reset() {
        counter = 0;
        acc = 0;
        for (Cmd cmd : cmdMap.values()) cmd.reset();
    }

    public long getAcc() {
        return acc;
    }

    public int getCounter() {
        return counter;
    }

    public long runUntilLoop() {
        hasLoop();
        return acc;
    }

    public boolean hasLoop() {
        boolean gotToEnd = false;
        commandsUntilLoop.clear();
        while (!gotToEnd) {
            Cmd cmd = cmdMap.get(counter);
            if (cmd.isHasBeenRun()) {
                return true; // <-- RETURN ON LOOP
            }
            commandsUntilLoop.put(counter, cmd);
            if (cmd.cmd.equals("jmp")) {
                counter += cmd.val;
            } else if (cmd.cmd.equals("acc")) {
                acc += cmd.val;
                counter++;
            } else {
                counter++;
            }
            cmd.setHasBeenRun();
            if(counter >= cmdMap.size()) {
                gotToEnd = true;
            }
        }
        return false;
    }

    public void flip(Integer counter) {
        Cmd old = cmdMap.get(counter);
        if (old.cmd.equals("jmp")) cmdMap.put (counter, new Cmd("nop", old.val));
        else if (old.cmd.equals("nop")) cmdMap.put (counter, new Cmd("jmp", old.val));
        else System.out.println("cannot flip ["+ old.cmd + "] at " + counter);
    }
}

class Cmd {
    public final String cmd;
    public final int val;
    private boolean hasBeenRun = false;

    public Cmd(String cmd, int val) {
        this.cmd = cmd;
        this.val = val;
    }

    public boolean isHasBeenRun() {
        return hasBeenRun;
    }

    public void setHasBeenRun() {
        this.hasBeenRun = true;
    }

    public void reset() {
        this.hasBeenRun = false;
    }

    @Override
    public String toString() {
        return cmd + " " + val + " (hasBeenRun=" + hasBeenRun + ')';
    }
}
