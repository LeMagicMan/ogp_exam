package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * an enumerator containing all types of Anchorpoints
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public enum AnchorPoint {

    /**
     * A representative for the right hand of an entity
     */
    LEFTHAND(ItemType.ANY),

    /**
     * A representative for the left hand of an entity
     */
    RIGHTHAND(ItemType.ANY),

    /**
     * A representative for the back of an entity
     */
    BACK(ItemType.ANY),

    /**
     * A representative for the body of an entity
     */
    BODY(ItemType.ARMOR),

    /**
     * A representative for the belt of an entity
     */
    BELT(ItemType.MONEY_POUCH);

    /**
     * The allowed ItemType for a certain Anchorpoint
     */
    private final ItemType allowedItemType;

    /**
     * Constructor for an Anchorpoint
     *
     * @param allowedItemType
     *      The allowed ItemType for that AnchorPoint
     *
     * @pre ItemType must be valid
     *      | (allowedItemType != null)
     */
    private AnchorPoint(ItemType allowedItemType) {
        this.allowedItemType = allowedItemType;
    }

    /**
     * getter for the allowed ItemType on an anchorpoint
     *
     * @return the allowedItemType for that anchorpoint
     *      | this.allowedItemType
     */
    @Basic @Raw
    public ItemType getAllowedItemType() {
        return allowedItemType;
    }

    /**
     * checks whether a certain Item can be attached to the AnchorPoint
     *
     * @param item
     *      The item that needs to be checked
     *
     * @return true if the Item can be attached, false otherwise
     *      | if (ItemType == allowedItemType || allowedItemType == ANY)
     *      | then result == true
     */
    @Raw
    public boolean canAttach(Item item) {
       return allowedItemType == ItemType.ANY || item.getItemType() == allowedItemType;
    }

}
