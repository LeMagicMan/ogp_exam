package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;

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
    BELT(ItemType.MONEY_POUCH);

    /**
     * The allowed ItemType for a certain Anchorpoint
     */
    private final ItemType allowedItemType;

    /**
     * the Item in that anchorpoint
     */
    private Item item;

    /**
     * Contructor for an Anchorpoint
     *
     * @param allowedItemType
     *      The allowed ItemType for that AnchorPoint
     *
     * @pre ItemType must be valid
     *      | (allowedItemType != null)
     */
    private AnchorPoint(ItemType allowedItemType) {
        this.allowedItemType = allowedItemType;
        this.item = null;
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

    /**
     * getter for the Item in an AnchorPoint
     *
     * @return the item in the AnchorPoint
     *      | this.item
     */
    @Basic
    public Item getItem() {
        return item;
    }

    /**
     * sets the Item of an AnchorPoint
     *
     * @pre Item must be Valid
     *      | Item.isValidItem()
     *
     * @param item
     *      The Item that needs to be set
     *
     */
    @Model
    void setItem(Item item) {
        this.item = item;
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
    public boolean canAttach(Item item) {
       return allowedItemType == ItemType.ANY || item.getItemType() == allowedItemType;
    }

    /**
     * checks if a given Anchorpoint has a given Item
     *
     * @param item
     *      the item we need to check for
     *
     * @return true if AnchorPoint has Item, false otherwise
     *      | result == (this.getItem() == item)
     */
    public boolean hasAsItem(Item item) {
        return this.item == item;
    }

}
