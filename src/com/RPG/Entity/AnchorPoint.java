package com.RPG.Entity;

import be.kuleuven.cs.som.annotate.Basic;
import com.RPG.Item.Item;
import com.RPG.Item.ItemType;

/**
 * an enumerator containing all types of Anchorpoints
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
    BELT(ItemType.MONEY_POUCH_ONLY);

    /**
     * The allowed ItemType for a certain Anchorpoint
     */
    private final ItemType allowedItemType;

    /**
     * Contructor for an Anchorpoint
     *
     * @param allowedItemType
     *      The allowed ItemType for that AnchorPoint
     */
    AnchorPoint(ItemType allowedItemType) {
        this.allowedItemType = allowedItemType;
    }

    /**
     * getter for the allowed ItemType on an anchorpoint
     *
     * @return the allowedItemType for that anchorpoint
     *      | this.allowedItemType
     */
    @Basic
    public ItemType getAllowedItemType() {
        return allowedItemType;
    }

//    public boolean canAttach(Item item) {
//        return allowedItemType == ItemType.ANY || item.getType() == allowedItemType; //TODO: make type
//    }

}
