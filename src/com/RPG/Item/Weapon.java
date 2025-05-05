package com.RPG.Item;

import com.RPG.Entity.Entity;

public class Weapon extends Item {

    private static int maxDamage = 100;
    private static int valuePerDamageUnit = 2;

    private int damage = 0;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Weapon(double weight, int damage, Entity owner) {
        super(weight, damage, owner);
    }
}
