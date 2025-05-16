package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

/**
 * interface for creating basic Items
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public interface MonsterLootFactory {
    /**
     * Creates A basic Item
     *
     * @param owner
     *      owner of the Item
     *
     * @param anchorPoint
     *      anchorPoint we want to attach the item to
     *
     * @return the created Item
     *
     * @throws InvalidValueException shouldn't Happen
     *
     * @throws InvalidHolderException gets Thrown when owner isn't valid
     *      | !owner.isValidHolder
     */
    @Raw
    Item createItem(Monster owner, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException;
}
