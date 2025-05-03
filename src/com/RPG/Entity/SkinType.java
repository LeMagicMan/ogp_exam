package com.RPG.Entity;

import be.kuleuven.cs.som.annotate.Basic;

public enum SkinType {

    /**
     * A skinType for an entity with tough skin
     */
    TOUGH(5),

    /**
     * A skinType for an entity with thick skin
     */
    THICK(8),

    /**
     * A skinType for an entity with scaled skin
     */
    SCALED(15),

    /**
     * A skinType for an entity with normal skin
     */
    NORMAL(0);

    /**
     * the protection of a certain skinType
     */
    private int Protection = 0;

    /**
     * constructor for a skinType
     *
     * @param protection
     *      the amount of protection of a skinType
     *
     * @pre Protection must be valid
     *      | (protection >= 0)
     */
    private SkinType(int protection) {
        this.Protection = protection;
    }

    /**
     * getter for the protection of a SkinType
     *
     * @return the amount of protection
     */
    @Basic
    public int getProtection() {
        return Protection;
    }
}
