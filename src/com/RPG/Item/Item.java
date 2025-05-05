package com.RPG.Item;

import com.RPG.Entity.Entity;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Item {

    /**********************************************************
     * Variables
     **********************************************************/

    private static final AtomicLong idGenerator = new AtomicLong(6); // Start bij 6: positief, even, deelbaar door 3

    private ItemType itemType;

    private final long Id;

    private final double Weight;

    private final int maxValue = 500;

    private int Value = 0;

    private Entity Holder; //TODO kan ook monster zijn?

    private ShineLevel ShineLevel;

    /**********************************************************
     * Constructors
     **********************************************************/

    protected Item(double weight, int damage, Entity Holder, ShineLevel ShineLevel) {
        if (!isValidHolder(Holder)){
            throw new InvalidHolderException("Holder cannot be terminated");
        }
        if (!isValidWeight(weight)){
            this.Weight =10;
        } else this.Weight = weight;

        this.Id = generateUniqueId();
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * generates an unique id for every Item
     *
     * @return the uniquely created Id
     *      | //TODO: ask about formal
     */
    private long generateUniqueId() {
        long nextId;
        do {
            nextId = idGenerator.getAndAdd(6);
        } while (nextId <= 0 || nextId % 6 != 0);
        return nextId;
    }

    /**
     * checks if the weight of an Item is a valid one
     *
     * @param weight
     *      weight that needs to be checked
     *
     * @return true if weight is valid, otherwise false
     *      | result == weight >= 0
     */
    public boolean isValidWeight(double weight) {
        return weight >= 0;
    }

    public boolean isValidHolder(Entity Holder){
        //return (!Holder.isTerminated);
        return true;//&& !Holder.isTerminated ; //TODO: add function to

    }
}
