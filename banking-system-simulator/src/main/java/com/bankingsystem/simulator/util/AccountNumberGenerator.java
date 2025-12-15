package com.bankingsystem.simulator.util;

import java.util.Random;

public class AccountNumberGenerator {

    public static String generate(String holderName) {
        String initials = holderName.substring(0, 2).toUpperCase();
        int randomDigits = new Random().nextInt(9000) + 1000;  // generates 1000–9999
        return initials + randomDigits;
    }
}
