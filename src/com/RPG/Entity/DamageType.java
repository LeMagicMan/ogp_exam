package com.RPG.Entity;

import be.kuleuven.cs.som.annotate.Basic;

/**
 * An enumerator representing the damageTypes of a monster
 */
public enum DamageType {

    /**
     * DamageType for a monster that fights with its claws
     */
    CLAWS(100),

    /**
     * DamageType for a monster that fights with its teeth
     */
    TEETH(80),

    /**
     * DamageType for a monster that fights with its tail
     */
    TAIL(60),

    /**
     * DamageType for a monster that fights with its horns
     */
    HORNS(70);

    /**
     * The Damage the DamageType does
     */
    private final long baseDamage;

    /**
     * constructor for a damageType
     *
     * @param baseDamage
     *      The Damage of that damageType
     *
     * @pre baseDamage must be bigger than 0
     *      | (baseDamage > 0)
     */
    private DamageType(long baseDamage) {
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
}
