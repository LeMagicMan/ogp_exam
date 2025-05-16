package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

/**
 * A factory that creates a basic backpack Item
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class BackpackFactory implements MonsterLootFactory {
    /**
     * creates a basic backpackItem
     *
     * @param owner
     *      owner of the backpack
     *
     * @param anchorPoint
     *      anchorpoint we want to add the backpack to
     *
     * @return created Item
     *      | result == new Backpack(owner, anchorPoint)
     *
     * @throws InvalidValueException should not get thrown
     *
     * @throws InvalidHolderException when Holder is not Valid
     *      | !owner.isValidHolder
     */
    @Override @Raw
    public Item createItem(Monster owner, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        return new Backpack(owner, anchorPoint);
    }
}

