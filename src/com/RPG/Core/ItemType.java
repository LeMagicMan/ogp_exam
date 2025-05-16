package com.RPG.Core;

/**
 * enumerator containing all types of items
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public enum ItemType {
    /**
     * Item that can be anything
     */
    ANY,

    /**
     * A Weapon Item
     */
    WEAPON,

    /**
     * An armor Item
     */
    ARMOR,

    /**
     * A money pouch Item
     */
    MONEY_POUCH,

    /**
     * A backpack Item
     */
    BACKPACK;

    /**
     * constructor for an ItemType
     */
    ItemType() {
    }
}
