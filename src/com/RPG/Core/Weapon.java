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

    private static final int defaultDamage = 10;

    private static final double defaultWeight= 10;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Weapon(double weight, Entity Holder, AnchorPoint anchorPoint, ShineLevel ShineLevel, int Damage) throws InvalidHolderException, InvalidValueException {
        super(weight, Damage, Holder, anchorPoint, ShineLevel, ItemType.WEAPON);
        if (!isValidDamage(Damage)){
            this.Damage = defaultDamage;
        } else this.Damage = Damage;
    }

    public Weapon( Entity Holder, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        this(defaultWeight, Holder, anchorPoint, ShineLevel.LOW, defaultDamage);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    public int getDamage() {
        return Damage;
    }
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
            return defaultDamage * valuePerDamage;
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
