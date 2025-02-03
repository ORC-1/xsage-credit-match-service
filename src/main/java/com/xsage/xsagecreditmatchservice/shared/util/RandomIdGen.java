package com.xsage.xsagecreditmatchservice.shared.util;

import java.util.Random;

public class RandomIdGen {
    static Random rand = new Random();
    public static long generateID() {
        return rand.nextInt(1_000_000_000) + (rand.nextInt(90) + 10) * 1_000_000_000L;
    }
}
