package com.RPG.Item;

import com.RPG.Entity.Entity;
import com.RPG.Exception.InvalidHolderException;

import java.util.ArrayList;

/**
 * A class representing a BackPack Item
 *
 * @invar backpack must have proper Items
 *      | HasProperItems()
 */
public class Backpack extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    private ArrayList<Item> Content; //TODO: ask about Hashset

    private int Capacity = 0;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Backpack(double weight, Entity Holder, ShineLevel ShineLevel, ArrayList<Item> Content) throws InvalidHolderException {
        super(ShineLevel, ItemType.BACKPACK); //TODO: ask about checker after super
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

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
}
