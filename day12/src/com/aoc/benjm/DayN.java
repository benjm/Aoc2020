package com.aoc.benjm;

import java.util.Scanner;


public class DayN {
    private final char N = 'N', S = 'S', E = 'E', W = 'W', L = 'L', R = 'R', F = 'F';

    public long partOne(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        long x = 0, y = 0;
        char facing = E;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char ins = line.charAt(0);
            long val = Long.parseLong(line.substring(1));
            if (ins == F) {
                ins = facing;
            }
            switch (ins) {
                case N: y += val; break;
                case S: y -= val; break;
                case E: x += val; break;
                case W: x -= val; break;
                case L: facing = rotateFacingAntiClockwise(facing, val); break;
                case R: facing = rotateFacingClockwise(facing, val); break;
                default: throw new RuntimeException("unknown instruction: " + ins);
            }
            //System.out.println(line + "  \t" + x + "\t" + y + "\t" + facing);
        }
        return Math.abs(x) + Math.abs(y);
    }

    private char rotateFacingClockwise(final char facing, final long val) {
        long newAngle = facingToAngle(facing) + val;
        while (newAngle >= 360) {
            newAngle -= 360;
        }
        while (newAngle < 0) {
            newAngle += 360;
        }
        return angleToFacing(newAngle);
    }

    private char angleToFacing(final long angle) {
        if (angle == 0) return N;
        if (angle == 90) return E;
        if (angle == 180) return S;
        if (angle == 270) return W;
        throw new RuntimeException("unknown angle: " + angle);
    }

    private long facingToAngle(final char facing) {
        switch (facing) {
            case N: return 0;
            case S: return 180;
            case E: return 90;
            case W: return 270;
            default: throw new RuntimeException("unknown facing: " + facing);
        }
    }

    private char rotateFacingAntiClockwise(final char facing, final long val) {

        return rotateFacingClockwise(facing, 360 - val);
    }

    /*
    Action N means to move the waypoint north by the given value.
    Action S means to move the waypoint south by the given value.
    Action E means to move the waypoint east by the given value.
    Action W means to move the waypoint west by the given value.
    Action L means to rotate the waypoint around the ship left (counter-clockwise) the given number of degrees.
    Action R means to rotate the waypoint around the ship right (clockwise) the given number of degrees.
    Action F means to move forward to the waypoint a number of times equal to the given value.
    The waypoint starts 10 units east and 1 unit north relative to the ship. The waypoint is relative to the ship; that is, if the ship moves, the waypoint moves with it.
     */
    public long partTwo(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        long x = 0, y = 0;
        long dx = 10, dy = 1;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char ins = line.charAt(0);
            long val = Long.parseLong(line.substring(1));
            if (ins == L) {
                ins = R;
                val = 360 - val;
            }
            switch (ins) {
                case N: dy += val; break;
                case S: dy -= val; break;
                case E: dx += val; break;
                case W: dx -= val; break;
                case R: {
                    if (val == 0 || val == 360) {
                        // nothing
                    } else if (val == 180) {
                        dx = -dx;
                        dy = -dy;
                    } else if ((dx >= 0 && dy >= 0) || (dx < 0 && dy < 0)) {
                        //lower left or upper right
                        long tempy = dy; // if 90 --> x remains same sign, y changes
                        dy = -dx;
                        dx = tempy;
                        if (val == 270) {
                            dy = -dy;
                            dx = -dx;
                        }
                    } else if ((dx >= 0 && dy < 0) || (dx < 0 && dy >= 0)) {
                        //lower right or upper left
                        long tempy = dy; // if 90 --> x changes sign, y same
                        dy = -dx;
                        dx = tempy;
                        if (val == 270) {
                            dy = -dy;
                            dx = -dx;
                        }
                    }
                }
                break;
                case F: {
                    x += (dx * val);
                    y += (dy * val);
                }
                break;
                default: throw new RuntimeException("unknown instruction: " + ins);
            }
            System.out.println(line + "  \t" + x + "," + y + "\t" + dx + "," + dy);
        }
        return Math.abs(x) + Math.abs(y);
    }
}
