package ru.sbt.exchange.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.valueOf;

public class OrderIdGenerator {
    private static final String seed = valueOf(new Random().nextInt());
    private static final AtomicInteger counter = new AtomicInteger();

    public static String generateOrderId() {
        return seed + "#" + counter.getAndIncrement();
    }
}