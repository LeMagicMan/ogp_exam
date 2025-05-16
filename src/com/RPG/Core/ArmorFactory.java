package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Raw;

/**
 * A factory that creates a basic armor Item
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class ArmorFactory implements MonsterLootFactory {
    /**
     * creates a basic armor Item
     *
     * @param owner
     *      owner of the armor
     *
     * @param anchorPoint
     *      AnchorPoint of the armor
     *
     * @return The created Item
     *      | result == null
     */
    @Override @Raw
    public Item createItem(Monster owner, AnchorPoint anchorPoint)  {
        return null;
    }
}