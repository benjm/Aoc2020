package com.aoc.benjm;

import java.util.Scanner;

public class DayN {
    public long run(String filename) {
        Scanner scanner = new Scanner(DayN.class.getResourceAsStream(filename));
        final long cardPublicKey = scanner.nextLong();
        final long doorPublicKey = scanner.nextLong();
        scanner.close();

        final long subject = 7;
        final long divisor = 20201227;

        //final long cardLoopSize = getLoopSize(cardPublicKey, subject, divisor);
        final long doorLoopSize = getLoopSize(doorPublicKey, subject, divisor);

        final long encryptionKey = getEncryptKey(cardPublicKey, doorLoopSize, divisor);

        return encryptionKey;
    }

    private long getEncryptKey(long publicKey, long loopSize, long divisor) {
        long value = 1;
        for (int i = 0; i < loopSize; i++) {
            value = loop(value, publicKey, divisor);
        }
        return value;
    }

    private long getLoopSize(long publicKey, long subject, long divisor) {
        long value = 1;
        long loopSize = 0;
        while (value != publicKey) {
            value = loop(value, subject, divisor);
            loopSize++;
        }
        return loopSize;
    }

    private long loop(long value, long subject, long divisor) {
        return (value * subject) % divisor;
    }
}
