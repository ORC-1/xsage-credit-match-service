package com.xsage.xsagecreditmatchservice.shared.util;

import java.util.Random;

public class RandomIDGen {
    public static long generateID() {

        Random rand = new Random();
        long numbers = rand.nextInt(1_000_000_000) + (rand.nextInt(90) + 10) * 1_000_000_000L;

        return numbers;
    }
}
