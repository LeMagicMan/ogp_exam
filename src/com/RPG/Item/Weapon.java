package com.RPG.Item;

import com.RPG.Entity.Entity;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

public class Weapon extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    private int Damage;

    private static int valuePerDamage = 2;

    private final int maxValue = 200;

    private static final int maxDamage = 100;

    private static final int DefaultDamage = 10;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Weapon(double weight, Entity Holder, ShineLevel ShineLevel, int Damage) throws InvalidHolderException, InvalidValueException {
        super(weight, Damage, Holder, ShineLevel, ItemType.WEAPON);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * calculates the value of weapon using its Damage
     *
     * @pre Damage must be valid
     *      | isValidDamage(Damage)
     *
     * @effect if damage is not valid calculate value using defaultDamage
     *      | if(!isValidDamage(Damage))
     *      |   then result == DefaultDamage * valuePerDamage
     *
     * @param Damage
     *      the variable Value depends on
     *
     * @return the calculated Value
     *      | Damage * valuePerDamage
     */
    @Override
    protected int calculateValue(int Damage) {
        if (!isValidDamage()){
            return DefaultDamage * valuePerDamage;
        }
        return Damage * valuePerDamage;
    }

    /**
     *
     * @return
     */
    public boolean isValidDamage(){
        return Damage > 0 && Damage <= maxDamage;
    }
}
