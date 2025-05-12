package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;

/**
 * An enumerator representing the damageTypes of a monster
 *
 * @invar Each DamageType must have a baseDamage above or equal to 0
 *      | hasValidBaseDamage()
 */
public enum DamageType {

    /**
     * DamageType for an entity that fights with its claws
     */
    CLAWS(100),

    /**
     * DamageType for an entity that fights with its horns
     */
    HORNS(70),

    /**
     * DamageType for an entity that fights with normal limbs
     */
    NORMAL(0),

    /**
     * DamageType for an entity that fights with its tail
     */
    TAIL(60),

    /**
     * DamageType for an entity that fights with its teeth
     */
    TEETH(80);

    /**
     * The Damage the DamageType does
     */
    private long baseDamage = 0;

    /**
     * constructor for a damageType
     *
     * @param baseDamage
     *      The Damage of that damageType
     *
     * @pre baseDamage must be bigger than or equal to 0
     *      | hasValidDamage
     */
    private DamageType(long baseDamage) {
        if (!isValidDamage(baseDamage)) {
            return;
        }
        this.baseDamage = baseDamage;
    }

    /**
     * getter for the baseDamage of a damageType
     *
     * @return the baseDamage
     */
    @Basic
    public long getBaseDamage() {
        return baseDamage;
    }

    /**
     * checks if a given baseDamage is Valid
     *
     * @param baseDamage
     *      the baseDamage to check
     *
     * @return true if baseDamage bigger than or equal to 0, false otherwise
     *      | baseDamage >= 0
     */
    public boolean isValidDamage(long baseDamage) {
        return this.baseDamage >= 0;
    }
}
