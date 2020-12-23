package com.aoc.benjm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 {
    public long run(String filename) {
        Data data = new Data(new Scanner(Day13.class.getResourceAsStream(filename)));
        //for (Bus bus : data.buses) {log(bus.toString());}
        long start = System.currentTimeMillis();
        Long result = partOne(data);
        result = partTwo(data);
        log("time: " + (System.currentTimeMillis() - start));
        return result;
    }

    private Long partTwoBetter(Data data) {
        return -1l; // TODO;
    }

    private Long partTwo(Data data) {
        boolean found = false;
        Long start = System.currentTimeMillis();
        // jumps of biggest "rotatesEvery", using its firstMatch
        BusOld incrementer = null;
        for (BusOld bus : data.buses) {
            if (incrementer == null) {
                incrementer = bus;
            } else if (bus.getRotatesEvery() > incrementer.getRotatesEvery()) {
                incrementer = bus;
            }
        }
        // sort by biggest rotation
        // combo to
        while (!found) {
            data.bus0.incrementJustUnder(incrementer.getRotatesEvery());
            Long t0 = data.bus0.getTime();
            found = true;
            for (BusOld bus : data.buses) {
                bus.incrementRelativeTo(t0);
                if (found) {found = found && bus.inPlace(t0);}
            }
            if (System.currentTimeMillis() > start + 5000) {
                log("t0: " + t0);
                start = System.currentTimeMillis();
            }
        }
        return data.bus0.getTime();
    }

    private Long partOne(Data data) {
        long chosen = -1;
        long delta = Long.MAX_VALUE;
        for(BusOld bus : data.buses) {
            long d = bus.id - (data.time % bus.id);
            if (d < delta) {
                delta = d;
                chosen = bus.id;
            }
        }
        return chosen * delta;
    }

    private static void log(final String s) {
        System.out.println(s);
    }
}

class Data {
    public final long time;
    public final List<BusOld> buses;
    public final BusOld bus0;

    public Data(Scanner scanner) {
        long position = 0;
        time = Long.valueOf(scanner.nextLine());
        buses = new ArrayList<>();
        BusOld bus0 = null;
        BusOld combo = null;
        for (String s : scanner.nextLine().split(",")) {
            if (s.charAt(0) != 'x') {
                BusOld bus = new BusOld(position,Long.valueOf(s));
                if(position == 0) {
                    bus0 = bus;
                    combo = new BusOld(0, bus.id);
                } else {
                    bus.relative(combo);
                    // combo-bus : bus0:bus --> "id = rotatesEvery" and time @ firstMatch [bus 0] - essentially a "new bus 0"
                    buses.add(bus);
                    combo = new BusOld(0, bus.getRotatesEvery());
                    combo.setTime(bus.getFirstMatch());
                }
            }
            position++;
        }
        System.out.println("combo: " + combo);
        System.out.println("lastB: " + buses.get(buses.size()-1));
        this.bus0 = bus0;
    }
}

class BusOld {
    public final long position;
    public final long id;
    private Long time = 0l;
    private long rotatesEvery = -1;
    private long firstMatch = -1;

    public BusOld(long position, long id) {
        this.position = position;
        this.id = id;
    }

    public void incrementOnce() {
        time = time + id;
    }

    public void incrementJustUnder(long rotatesEvery) {
        long actual = rotatesEvery - (rotatesEvery % id);
        time = time + actual;
    }

    public void incrementRelativeTo(Long newtime) {
        while (newtime > time) {
            incrementOnce();
        }
    }

    public Long getTime() {
        return time;
    }

    public boolean inPlace(Long t0) {
        return time - t0 == position;
    }

    public void relative(BusOld bus0) {
        // n * b0.id == m * id + position == firstMatch
        long n = 1, m = 1;
        long mInc = bus0.id > id ? 1 : n/m;
        long nInc = bus0.id > id ? m/n : 1;
        boolean found = false;
        rotatesEvery = 0;
        while (n * bus0.id != m * id + position) {
            if (!found && n * bus0.id == m * id) {
                found = true;
                rotatesEvery = n * bus0.id;
            }
            if (n * bus0.id < m * id + position) {
                n += nInc;
            } else {
                m += mInc;
            }
        }
        firstMatch = n * bus0.id;
        if (!found) {
            while (n * bus0.id != m * id) {
                if (n * bus0.id < m * id) {
                    n += nInc;
                } else {
                    m += mInc;
                }
            }
            rotatesEvery = n * bus0.id;
        }
    }

    public long getRotatesEvery() {
        return rotatesEvery;
    }

    public long getFirstMatch() {
        return firstMatch;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "position=" + position +
                ", id=" + id +
                ", time=" + time +
                ", rotatesEvery=" + rotatesEvery +
                ", firstMatch=" + firstMatch +
                '}';
    }

    public void setTime(long time) {
        this.time = time;
    }

}


