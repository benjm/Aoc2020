package com.aoc.benjm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Day13Again {
    public long partOne(String filename) {
        return run(filename, false);
    }

    public long partTwo(String filename) {
        return run(filename, true);
    }

    private long run(String filename, boolean isPartTwo) {
        Scanner scanner = new Scanner(Day13Again.class.getResourceAsStream(filename));
        long time = Long.valueOf(scanner.nextLine());
        String busesRaw[] = scanner.nextLine().split(",");
        Map<Integer, Bus> buses = new HashMap<>();
        List<Integer> offsets = new ArrayList<>();
        for (int offset = 0; offset < busesRaw.length; offset++) {
            String rawId = busesRaw[offset];
            if (!rawId.equals("x")) {
                buses.put(offset, new Bus(offset, Long.parseLong(rawId)));
                offsets.add(offset);
            }
        }

        long start = System.currentTimeMillis();
        int numBuses = offsets.size();
        int onBus = 2;

        Bus temp = null;
        for(int offset : offsets) {
            if(offset == 0) {
                temp = buses.get(offset);
            } else {
                temp = temp.merge(buses.get(offset));
            }
            long timeTaken = System.currentTimeMillis() - start;
            System.out.println("bus " + (onBus++) + " of " + numBuses + " added at " + temp.start + " after " + timeTaken + "ms");
        }

        return temp.start;
    }
}

class Bus {
    public final int offset;
    public final long period;
    public final long start;

    public Bus(final int offset, final long period) {
        this(offset, period, 0);
    }

    private Bus(final int offset, final long period, final long start) {
        this.offset = offset;
        this.period = period;
        this.start = start;
    }

    public Bus merge(final Bus that) {
        // offset remains 0
        // find start where next is [next] at its offset from this
        // find how long until that occurs again : period
        long p0 = period;
        long p1 = that.period;
        long o0 = offset;
        long o1 = that.offset;
        long t0 = start;
        long t1 = t0 == 0 ? 0 : start - (start % p1) + p1; // first offset
        //delta --> amount offset changes
        long start = System.currentTimeMillis();
        long minute = 0;
        while(t1 - t0 != o1) {
            if (t1 < t0) {
                t1 += (((t0 - t1) / p1) + 1) * p1;
            } else { //TODO do we ever need to care about "if (t1 < o1)"?
                t0 += p0; // assume will be much much bigger soon!
            }
            if (System.currentTimeMillis() - start > 60000) {
                minute++;
                System.out.println(minute + " minutes elapsed:\nt0: " + t0);
            }
        }
        long newStart = t0;
        long newPeriod = lowestCommonMultiple(p0,p1);
        return new Bus(0,newPeriod,newStart);
    }

    private long lowestCommonMultiple(final long a, final long b) {
        return a*b / greatestCommonDivisor(a,b);
    }

    private long greatestCommonDivisor(final long a, final long b) {
        if (a > b) {
            return euclidianGCD(a,b);
        }
        return euclidianGCD(b,a);
    }

    private long euclidianGCD(final long bigger, final long smaller) {
        long remainder = bigger % smaller;
        if (remainder == 0) return smaller;
        else return euclidianGCD(smaller, remainder);
    }
}
