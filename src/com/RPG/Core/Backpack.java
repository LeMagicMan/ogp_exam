package com.RPG.Core;

import com.RPG.Exception.*;

import java.util.ArrayList;

/**
 * A class representing a BackPack Item
 *
 * @invar backpack must have proper Items
 *      | HasProperItems()
 *
 * @invar capacity of a backpack must be bigger than zero
 *      | isValidCapacity()
 */
public class Backpack extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    private ArrayList<Item> Content; //TODO: ask about Hashset

    private int Capacity = 0;

    private static final int defaultCapacity = 20;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Backpack(double weight, int Value, int Capacity, Entity Holder, com.RPG.Core.ShineLevel ShineLevel, ArrayList<Item> Content) throws InvalidHolderException, InvalidValueException, InvalidItemsException {
        super(ShineLevel, ItemType.BACKPACK); //TODO: ask about checker after super
        if (!hasValidHolder(Holder)){
            throw new InvalidHolderException("Holder cannot be terminated");
        }
        if(!isValidValue(Value)){
            throw new InvalidValueException("Value cannot be negative");
        }
        if(!hasProperItems()){
            throw new InvalidItemsException("Items of backpack must belong to the same holder as the backpack");
        }
        try {
            this.setHolder(Holder);
        }catch (InvalidHolderException e){
            assert false; //Can never happen
        }
        this.setWeight(weight);
        this.setCapacity(Capacity);
        this.setValue(Value);
        this.Content = Content;
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * sets the capacity of a backpack
     *
     * @pre capacity must be valid
     *      | isValidCapacity(capacity)
     *
     * @effect if the capacity is not valid set capacity to the defaultCapacity
     *      | if(!isValidCapacity())
     *      |   then this.Capacity = defaultCapacity
     *
     * @param capacity
     *      the capacity that needs to be set
     *
     * @post the capacity of the backpack is set to the given capacity
     *      | this.Capacity = capacity
     */
    private void setCapacity(int capacity){
        if (!isValidCapacity(capacity)){
            this.Capacity = defaultCapacity;
        } else this.Capacity = capacity;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * checks if all item in the backpack belong the backpacks holder
     *
     * @return true if all items belong to the backpacks holder, false otherwise
     *      | for each item in content
     *      |       if (!(item.getHolder == this.getHolder))
     *      |           result == false
     *      | result == true
     */
    public boolean hasProperItems(){
        for (Item item : Content){
            if (item.getHolder() != this.getHolder()){
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the capacity of a backpack is bigger than zero
     *
     * @param capacity
     *      capacity that needs to be checked
     *
     * @return true if capacity is bigger than zero, false otherwise
     *      | result == capacity > 0
     */
   public boolean isValidCapacity(int capacity){
       return capacity > 0;
   }
}
