package com.bankingsystem.simulator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionIdGenerator {

    private static int counter = 1;

    public static String generate() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return "TXN-" + date + "-" + (counter++);
    }
}
