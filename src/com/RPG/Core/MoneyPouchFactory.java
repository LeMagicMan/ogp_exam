package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Raw;

/**
 * creates a basic money pouch Item
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class MoneyPouchFactory implements MonsterLootFactory{
    /**
     * creates  basic money pouch Item
     *
     * @param owner
     *      owner of the money Pouch
     *
     * @param anchorPoint
     *      anchorPoint the money Pouch is attached to
     *
     * @return the created money Pouch
     *      | result == null
     *
     */
    @Override @Raw
    public Item createItem(Monster owner, AnchorPoint anchorPoint)  {
        return null;
    }
}
