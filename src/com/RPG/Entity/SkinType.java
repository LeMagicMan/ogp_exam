package com.RPG.Entity;

import be.kuleuven.cs.som.annotate.Basic;

public enum SkinType {

    /**
     * A skinTYpe for a monster with tough skin
     */
    TOUGH(5),

    /**
     * A skinTYpe for a monster with thick skin
     */
    THICK(8),

    /**
     * A skinTYpe for a monster with scaled skin
     */
    SCALED(15);

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
    public int getProtectionFactor() {
        return Protection;
    }
}
