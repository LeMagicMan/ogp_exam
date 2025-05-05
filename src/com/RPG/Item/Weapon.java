package com.RPG.Item;

import com.RPG.Entity.Entity;
import com.RPG.Exception.InvalidHolderException;

public class Weapon extends Item {
    /**********************************************************
     * Constructors
     *********************************************************/

    public Weapon(double weight, Entity Holder, ShineLevel ShineLevel) throws InvalidHolderException {
        super(weight, Holder, ShineLevel, ItemType.ANY);
    }
}
