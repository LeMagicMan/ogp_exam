package com.RPG.Mechanics;

/**
 * an enum representing ways entity's can loot
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public enum LootStrategy {
    /**
     * A way of looting where the entity can choose
     */
    INTELLIGENT,

    /**
     * A way of looting where the entity chooses the most shiny thing
     */
    SHINE_BASED
}
