package com.RPG.Core;

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

    public Weapon(double weight, Entity Holder, com.RPG.Core.ShineLevel ShineLevel, int Damage) throws InvalidHolderException, InvalidValueException {
        super(weight, Damage, Holder, ShineLevel, ItemType.WEAPON);
        if (!isValidDamage(Damage)){
            this.Damage = DefaultDamage;
        } else this.Damage = Damage;
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
        if (!isValidDamage(Damage)){
            return DefaultDamage * valuePerDamage;
        }
        return Damage * valuePerDamage;
    }

    /**
     * checks if the Damage of a weapon is valid
     *
     * @return true if Damage is bigger than zero, and smaller than maxDamage, false otherwise
     *      | result == (Damage > 0 && Damage <= maxDamage)
     */
    public boolean isValidDamage(int Damage){
        return Damage > 0 && Damage <= maxDamage;
    }
}
