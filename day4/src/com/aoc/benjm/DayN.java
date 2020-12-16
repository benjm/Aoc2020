package com.aoc.benjm;

import java.util.*;

public class DayN {
    private String[] requiredKeys = {"byr","iyr","eyr","hgt","hcl","ecl","pid"};
    private String[] optionalKeys = {"cid"};
    private Set<String> ecls = new HashSet<>(7);

    public DayN() {
        String ecls[] = {"amb","blu","brn","gry","grn","hzl","oth"};
        for (String s : ecls) {
            this.ecls.add(s);
        }
    }

    public long partOne(String filename) {
        return run(filename,false);
    }

    public long partTwo(String filename) {
        return run(filename,true);
    }

    private long run(String filename, boolean usePartTwo) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        Map<String,String> temp = new HashMap<>();
        long count = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (String pair : line.split(" ")) {
                String kv[] = pair.split(":");
                if(pair.length()>1) temp.put(kv[0],kv[1]);
            }
            if(line.isEmpty() || line.isBlank() || !scanner.hasNextLine()) {
                if (usePartTwo) {
                    if (validTwo(temp)) {
                        count++;
                    }
                } else {
                    if (valid(temp)) {
                        count++;
                    }
                }
                temp.clear();
            }
        }
        return count;
    }

    private boolean valid(Map<String, String> data) {
        for (String s : requiredKeys) {
            if (!data.containsKey(s)) {
                return false;
            }
        }
        return true;
    }

    private boolean validTwo(Map<String, String> data) {
        for (String s : requiredKeys) {
            if (!data.containsKey(s)) {
                return false;
            }
        }
        if(!checkYear(data,"byr",1920,2002)) return false;
        if(!checkYear(data,"iyr",2010,2020)) return false;
        if(!checkYear(data,"eyr",2020,2030)) return false;
        if(!checkPid(data.get("pid"))) return false;
        if(!checkEcl(data.get("ecl"))) return false;
        if(!checkHcl(data.get("hcl"))) return false;
        if(!checkHgt(data.get("hgt"))) return false;
        return true;
    }

    private boolean checkHgt(String hgt) {
//        hgt (Height) - a number followed by either cm or in:
//        If cm, the number must be at least 150 and at most 193.
//        If in, the number must be at least 59 and at most 76.
        if(!hgt.matches("^[0-9]{1,}(in|cm)$")) return false;
        int val = Integer.parseInt(hgt.substring(0,hgt.length()-2));
        if (hgt.endsWith("cm")) return isBetween(val,150,193);
        return isBetween(val,59,76);
    }

    private boolean checkPid(String pid) {
        //a nine-digit number, including leading zeroes.
        return pid.matches("^[0-9]{9}$");
    }

    private boolean checkHcl(String hcl) {
        //# followed by exactly six characters 0-9 or a-f
        return hcl.matches("^#([a-f]|[0-9]){6}$");
    }

    private boolean checkEcl(String ecl) {
        return ecls.contains(ecl);
    }

    private boolean checkYear(Map<String, String> data, String key, int min, int max) {
        int year = Integer.parseInt(data.get(key));
        return isBetween(year,min,max);
    }

    private boolean isBetween(int val, int min, int max) {
        return val >= min && val <= max;
    }
}
