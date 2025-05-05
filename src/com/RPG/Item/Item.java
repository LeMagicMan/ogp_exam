package com.RPG.Item;

import com.RPG.Entity.Entity;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Item {

    /**********************************************************
     * Variables
     **********************************************************/

    private static final AtomicLong idGenerator = new AtomicLong(6); // Start bij 6: positief, even, deelbaar door 3
    private static int maxDamage = 100;
    private static int valuePerDamageUnit = 2;

    private final long id;
    private final double weight;
    private int damage;
    private Integer value;
    private Entity owner; //TODO kan ook monster zijn?

    /**********************************************************
     * Constructors
     **********************************************************/

    protected Item(double weight, int damage, Entity owner) {

    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/
    private long generateUniqueId() {
        long nextId;
        do {
            nextId = idGenerator.getAndAdd(6); // +6 garandeert even, positief en deelbaar door 3
        } while (nextId <= 0 || nextId % 2 != 0 || nextId % 3 != 0);
        return nextId;
    }

}
