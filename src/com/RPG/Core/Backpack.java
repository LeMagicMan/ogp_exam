package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidItemsException;
import com.RPG.Exception.InvalidValueException;

import java.util.ArrayList;

/**
 * A class representing a BackPack Item
 *
 * @invar backpack must have proper Items
 *      | HasProperItems()
 *
 * @invar capacity of a backpack must be bigger than zero
 *      | isValidCapacity()
 */
public class Backpack extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    private ArrayList<Item> Content; //TODO: ask about Hashset

    private int Capacity = 0;

    private static final int defaultCapacity = 20;

    private static final double defaultWeight = 5;

    private static final int defaultValue = 20;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Backpack(double weight, int Value, int Capacity, Entity Holder, AnchorPoint anchorPoint, ShineLevel shinelevel, ArrayList<Item> Content) throws InvalidHolderException, InvalidValueException, InvalidItemsException {
        super(weight, Value, shinelevel, ItemType.BACKPACK); //TODO: ask about checker after super
        if (this.hasProperItems(Content)){
            throw new InvalidItemsException("all backpack items must belong to this backpack");
        }
        this.Content = Content;
        Holder.equip(anchorPoint, this);
        this.Capacity = Capacity;
    }

    public Backpack( double weight, int Value, int Capacity, Entity Holder, AnchorPoint anchorPoint, ShineLevel shinelevel) throws InvalidValueException, InvalidHolderException {
        super(weight, Value, Holder, anchorPoint, shinelevel, ItemType.BACKPACK);
        this.Capacity = Capacity;
        this.Content = new ArrayList<Item>();
    }

    public Backpack(Entity Holder, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        this(defaultWeight, defaultValue, defaultCapacity, Holder, anchorPoint, ShineLevel.LOW);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * sets the capacity of a backpack
     *
     * @pre capacity must be valid
     *      | isValidCapacity(capacity)
     *
     * @effect if the capacity is not valid set capacity to the defaultCapacity
     *      | if(!isValidCapacity())
     *      |   then this.Capacity = defaultCapacity
     *
     * @param capacity
     *      the capacity that needs to be set
     *
     * @post the capacity of the backpack is set to the given capacity
     *      | this.Capacity = capacity
     */
    private void setCapacity(int capacity){
        if (!isValidCapacity(capacity)){
            this.Capacity = defaultCapacity;
        } else this.Capacity = capacity;
    }

    /**
     * getter for an item at a given Index
     *
     * @param index
     *      index of the item
     *
     * @pre index must be smaller than size of Content
     *
     * @return the Item at the given Index
     *      | this.Content.get(index)
     */
    public Item getItemAt(int index){
        if(index >= this.getAmountOfItems()){
            return null;
        }
        return Content.get(index);
    }

    /**
     * getter for the AMount of items in the content of  a backoack
     *
     * @return the amount of items in a backpack
     *      | this.Content.size()
     */
    public int getAmountOfItems(){
        return Content.size();
    }

    /**
     * getter for the totalWeight of a backpack
     *
     * @return the weight of every item in the backpack combined plus the backpacks own weight
     *      | TotalWeight == this.getWeight
     *      | for each item in Content
     *      |   TotalWeight += item.getWeight
     *      | result == TotalWeight
     */
    public double getTotalWeight(){
        double totalWeight = this.getWeight();
        for (Item item : Content){ //TODO: ask about this
            totalWeight += item.getWeight();
        }
        return totalWeight;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * stores an item in a backpack
     *
     * @param item
     *      the item we want to store
     */
    @Raw
    public void storeItem(Item item){
        this.AddItem(item);
    }

    /**
     * unpacks an item from a backpack
     *
     * @param item
     *      item we want to unpack
     */
    public void unpackItem(Item item){
        this.removeItem(item);
    }

    /**
     * removes an item from the content of a backpack
     *
     * @param item //TODO
     */
    private void removeItem(Item item){
        if (!this.hasAsItem(item)){
            return;
        }
        this.Content.remove(item);
        try {
            item.setHolder(null);
        } catch (InvalidHolderException e) {
            assert false;
        }
    }

    /**
     * a method to add an item to the content of a backpack
     *
     * @pre must be able to store Item
     *      | canAddItem(item) //TODO: ask if also needed in store
     *
     * @param item
     *      the item we want to add to the content
     */
    @Raw @Model
    private void AddItem(Item item){
        if (!this.canAddItem(item)){
            return;
        }
        if (item.getBackpack() == this){
            return;
        }
        if (item.getHolder() != null){
            item.getHolder().unequip(item.getHolder().getAnchorPointWithItem(item), item);
        }
        if (item.getBackpack() != null){
            item.getBackpack().removeItem(item);
        }
        try {
            item.setHolder(this.getHolder());
        } catch (InvalidHolderException e) {
            assert false;
        }
        Content.add(item);
    }

    /**
     * checks if we can add a given item to the content of a backpack
     *
     * @param item
     *      the item we want to check
     *
     * @return true if The Holder of the backpack isn't terminated, backpack isn't terminated, item isn't terminated
     * and if TotalWeight + item's weight is smaller than capacity, false otherwise
     *      | if (!(this.getTotalWeight() + item.getWeight() >= this.Capacity) || item.isTerminated() || this.isTerminated() || this.getHolder().isTerminated())
     *      |       result == false
     */
    public boolean canAddItem(Item item){
        if (!(this.getTotalWeight() + item.getWeight() >= this.Capacity)){
            return false;
        }
        return !item.isTerminated() && !this.isTerminated() && !this.getHolder().isTerminated();
    }

    /**
     * checks if all item in the backpack belong the backpacks holder
     *
     * @return true if all items belong to the backpacks holder, false otherwise
     *      | for each item in content
     *      |       if (!(item.getHolder == this.getHolder))
     *      |           result == false
     *      | result == true
     */
    public boolean hasProperItems(ArrayList<Item> content){
        for (Item item : content){
            if (item.getHolder() != this.getHolder()){ //TODO: ask if this is enough because i ensure all items in backpack belong to the same Holder
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the capacity of a backpack is bigger than zero
     *
     * @param capacity
     *      capacity that needs to be checked
     *
     * @return true if capacity is bigger than zero, false otherwise
     *      | result == capacity > 0
     */
   public boolean isValidCapacity(int capacity){
       return capacity > 0;
   }

    /**
     * //TODO
     * @param item
     * @return
     */
   public boolean hasAsItem(Item item){
       return this.Content.contains(item); //TODO: ask about method in public
   }
}
