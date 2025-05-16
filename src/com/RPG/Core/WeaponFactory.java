package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

/**
 * creates a basic weapon Item
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class WeaponFactory implements MonsterLootFactory{
    /**
     * creates a basic weapon Item
     *
     * @param owner
     *      owner of the Item
     *
     * @param anchorPoint
     *      anchorPoint we want to attach the item to
     *
     * @return The created weapon
     *      | result == new Weapon(owner, anchorPoint)
     *
     * @throws InvalidValueException should not get thrown
     *
     * @throws InvalidHolderException gets thrown when Holder isn't Valid
     *      | !owner.isValidHolder()
     */
    @Override @Raw
    public Item createItem(Monster owner, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        return new Weapon(owner, anchorPoint);
    }
}
