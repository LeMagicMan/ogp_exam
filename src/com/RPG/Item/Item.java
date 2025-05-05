package com.RPG.Item;

import com.RPG.Entity.Entity;
import com.RPG.Exception.*;

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

    protected Item(double weight, int Value,  Entity Holder, ShineLevel ShineLevel, ItemType itemType) throws InvalidHolderException, InvalidValueException {
        if (!isValidHolder(Holder)){
            throw new InvalidHolderException("Holder cannot be terminated");
        }

        if (!isValidWeight(weight)){
            this.Weight = 10;
        } else this.Weight = weight;

        this.setValue(Value);
        this.itemType = itemType;
        this.ShineLevel = ShineLevel;
        this.Holder = Holder;
        this.Id = generateUniqueId();
    }

    protected Item(ShineLevel ShineLevel, ItemType itemType){
        this.itemType = itemType;
        this.ShineLevel = ShineLevel;
        this.Id = generateUniqueId();
        Weight = 0;
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/
    /**
     * getter for the maxValue of an Item
     *
     * @return the maxvalue of that Item
     *      | this.maxValue
     */
    //@Basic //TODO
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * getter for the holder of an Item
     *
     * @return the holder of the Item
     *      | this.Holder
     */
    //@Basic //TODO
    public Entity getHolder() {
        return Holder;
    }

    /**
     * setter for the value of an Item
     *
     * @param value value that needs to be set
     *
     * @pre value needs to be bigger than or equal to 0
     *      | value >= 0
     *
     * @pre value needs to be smaller than or equal to MaxValue
     *      | value <= MaxValue
     *
     * @throws InvalidValueException
     *      gets thrown when value is smaller than zero or bigger than maxValue
     *          | result == !(isValidValue())
     *
     * @post value is set as th value of the Item
     *      | this.Value = value
     */
    public void setValue(int value) throws InvalidValueException {
        if(!isValidValue(value)){
            throw new InvalidValueException("Value cannot be negative");
        }
        this.Value = value;
    }

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

    /**
     * checks if the holder of an item is a valid one
     *
     * @param Holder
     *      the holder that needs to be checked
     *
     * @return true if holder is valid, false otherwise
     *      | !Holder.isTerminated()
     */
    public boolean isValidHolder(Entity Holder){
        //return (!Holder.isTerminated);
        return true;//&& !Holder.isTerminated() ; //TODO: add function to
    }

    /**
     * checks if the value of an item is valid
     *
     * @param value
     *      the value that needs to be checked
     *
     * @return true if the value is valid, false otherwise
     *      | result == value >= 0 && value <= this.MaxValue
     */
    public boolean isValidValue(int value){
        return value >= 0 && value <= this.getMaxValue();
    }

}
