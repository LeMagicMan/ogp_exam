package com.RPG.Item;

import com.RPG.Entity.Entity;
import com.RPG.Exception.InvalidHolderException;

import java.util.ArrayList;

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
        super(ShineLevel, ItemType.BACKPACK);


    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/
}
