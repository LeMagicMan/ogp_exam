package com.RPG.Item;

import com.RPG.Entity.Entity;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

public class Weapon extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    private int Damage;


    /**********************************************************
     * Constructors
     *********************************************************/

    public Weapon(double weight, Entity Holder, ShineLevel ShineLevel) throws InvalidHolderException, InvalidValueException {
        super(weight, value, Holder, ShineLevel, ItemType.WEAPON);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/
}
