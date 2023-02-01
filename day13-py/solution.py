import sys
import math
from dataclasses import dataclass
from datetime import datetime

datetime_start = datetime.now()

def elapsedTimeMs(since=datetime_start):
    return datetime.now()-since

@dataclass
class BusData:
    earliest: int
    ids: set
    bus_offsets: list

def processLines(lines):
    timestamp = int(lines[0])
    bus_ids = set()
    bus_offsets = []
    minutes = lines[1].split(",")
    for i in range(len(minutes)):
        b = minutes[i]
        if b.isdigit():
            bus_id = int(b)
            bus_ids.add(bus_id)
            bus_offsets.append((bus_id, i))
    return BusData(timestamp, bus_ids, bus_offsets)

def readFile(filename = sys.argv[1]):
    filename = sys.argv[1]
    lines = []
    with open(filename) as f:
        lines = f.read().splitlines()
    return processLines(lines)

def printPattern(pattern,bus_data):
    print()
    start = pattern.first
    end = start + max(offset for cycle,offset in bus_data.bus_offsets)+5
    l=len(str(end))
    header = ["."*l]
    for cycle,offset in bus_data.bus_offsets:
        header.append(str(cycle))
    print("\t".join(header))
    for i in range(start,end):
        row=[str(i).rjust(l)]
        for cycle,offset in bus_data.bus_offsets:
            if i%cycle == 0:
                row.append("D")
            else:
                row.append(".")
        print("\t".join(row))
    print()

bus_data = readFile()

print(elapsedTimeMs(),"starting part1")

def findSoonestBus(bus_data):
    first_bus = None
    soonest = math.inf
    before_after=[]
    for bus_id in bus_data.ids:
        before = (bus_data.earliest // bus_id) * bus_id
        after = before + (bus_id * (before < bus_data.earliest))
        if after<soonest:
            soonest = after
            first_bus = bus_id
        before_after.append((bus_id,before,after))
    for i,b,a in before_after:
        print(f"bus {i} arrived at {b} and then at {a}")
    wait_time = soonest - bus_data.earliest
    print(f"bus {first_bus} will arrive soonest at minute {soonest} which is {wait_time} minutes after minute {bus_data.earliest}")
    print(f"{elapsedTimeMs()} PART1 ANSWER: {first_bus} x {wait_time} = {first_bus * wait_time}")

findSoonestBus(bus_data)

print(elapsedTimeMs(),"starting part2")

@dataclass
class Pattern:
    cycle: int
    buses: set
    first: int

def findFirstBus(bus_data):
    for bus_id,offset in bus_data.bus_offsets:
        if offset==0:
            return Pattern(bus_id,[bus_id],offset)
    throw(f"ERROR finding bus with offset 0 in {bus_data.bus_offsets}")

def mergeNextBus(pattern, bus_data):
    cycle,offset = sorted([(bus_id,offset) for bus_id,offset in bus_data.bus_offsets if bus_id not in pattern.buses], key=lambda bus_offset:bus_offset[0])[-1]
    print(f"merging Bus with cycle {cycle} into {pattern} with offset {offset}")
    new_buses = pattern.buses.copy()
    new_buses.append(cycle)
    new_cycle = math.lcm(*new_buses) # how long until we're back at t0 state, and therefore how long between
    delta = 0
    print(pattern.cycle, "<--", cycle, "targetting offset of",offset)
    print(0,0,0)
    #offset > one of the cycles adds to the confusion...
    if 1: #pattern.cycle < cycle:
        pc = pattern.cycle
        pc_mult = 0
        while (pattern.first + pc*pc_mult + offset) % cycle > 0:
            pc_mult+=1
        new_first = pattern.first+pc*pc_mult
    else:
        print(f"ERROR - not sure we can merge in a cycle {cycle} that is a divisor of the pattern cycle {pattern.cycle} as the offset would always be 0...")
        exit()
    
    new_pattern = Pattern(new_cycle,new_buses,new_first)
    print(f"{elapsedTimeMs()} merged {cycle} into pattern {pattern} giving {new_pattern}")
    return new_pattern

def findPattern(bus_data):
    pattern = findFirstBus(bus_data)
    while len(pattern.buses) < len(bus_data.ids):
        print(f"{elapsedTimeMs()} There are {len(bus_data.ids) - len(pattern.buses)} buses left to merge into {pattern}")
        pattern = mergeNextBus(pattern, bus_data)
        #printPattern(pattern, bus_data)
    return pattern

print(f"{elapsedTimeMs()} Buses to berge together:")
for cycle,offset in bus_data.bus_offsets:
    print(f"\tBus {cycle} requires an offset of {offset}")
pattern = findPattern(bus_data)
printPattern(pattern,bus_data)
print(f"\n{elapsedTimeMs()} PART2 final pattern: {pattern}")