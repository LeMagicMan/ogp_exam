package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Enumerator for the shineLevel of an Item
 *
 * @invar ValueMultiplier must be Valid
 *      | isValidvalueMultiplier()
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public enum ShineLevel {

    /**
     * Item without any shine
     */
    NONE(1F),

    /**
     * Item with a low amount of Shine
     */
    LOW(1.2F),

    /**
     * Item with a medium amount of shine
     */
    MEDIUM(1.5F),

    /**
     * Item with a high amount of shine
     */
    HIGH(2F),

    /**
     * Item with a Legendary amount of shine
     */
    LEGENDARY(3F);

    /**
     * variable representing how much more valuable an item gets because of shine
     */
    private final float valueMultiplier;

    /**
     * constructor for a shineLevel
     *
     * @param valueMultiplier
     *      valueMultiplier of the shineLevel
     *
     * @pre ValueMultiplier must be Valid
     *      | isValidValueMultiplier()
     */
    private ShineLevel(float valueMultiplier) {
        if (!isValidValueMultiplier(valueMultiplier)) {
            this.valueMultiplier = 1F;
        } else this.valueMultiplier = valueMultiplier;
    }

    /**
     * getter for the value multiplier of a shineLevel
     *
     * @return the valueMultiplier
     *      | this.valueMultiplier
     */
    @Basic
    public float getValueMultiplier() {
        return valueMultiplier;
    }

    /**
     * checks if given value multiplier is valid
     *
     * @param valueMultiplier
     *      valueMultiplier that needs to be checked
     *
     * @return true if bigger than 0, false otherwise
     *      | result == valueMultiplier >= 1
     */
    @Raw
    public boolean isValidValueMultiplier(float valueMultiplier){
        return valueMultiplier >= 1;
    }
}
