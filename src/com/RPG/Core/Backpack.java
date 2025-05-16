package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidItemsException;
import com.RPG.Exception.InvalidValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class representing a BackPack Item
 *
 * @invar backpack must have proper Items
 *      | areProperItems()
 *
 * @invar capacity of a backpack must be bigger than zero
 *      | isValidCapacity()
 *
 * @invar capacity of a backpack must be bigger than or equal to the combined weight of all its items
 *      | canHoldItems()
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class Backpack extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * An arraylist representing the content of the backpack
     */
    private ArrayList<Item> Content;

    /**
     * A variable representing the capacity of  a backpack
     */
    private int Capacity = 0;

    /**
     * A variable representing the defaultCapacity of a backpack
     */
    private static final int defaultCapacity = 20;

    /**
     * A variable representing the defaultWeight of a backpack
     */
    private static final double defaultWeight = 5;

    /**
     * A variable representing the defaultValue
     */
    private static final int defaultValue = 20;

    /**
     * A variable Representing A thread-safe atomic counter used to generate unique IDs starting at 0
     */
    private static final AtomicLong idGenerator = new AtomicLong(0);

    /**********************************************************
     * Constructors
     *********************************************************/

    /**
     * A constructor for a backpack with given weight, value, capacity, Holder, anchorpoint, shineLevel, Content
     *
     * @pre All items must fit in backpack
     *      | canStoreAll(Content)
     *      
     * @pre All items must be not terminated
     *      | areProperItems(Content)
     *
     * @param weight
     *      the weight of the backpack
     *
     * @param Value
     *      the value of the backpack
     *
     * @param Capacity
     *      the capacity of this backpack
     *
     * @param Holder
     *      the holder of this backpack
     *
     * @param anchorPoint
     *      the anchorpoint this backpack attaches to
     *
     * @param shinelevel
     *      the shineLevel of this backpack
     *
     * @param Content
     *      the content of this backpack
     *
     * @post Content of the backpack is set to Content
     *      | this.Content = Content
     *      
     * @effect creates a backpack using the auxilliary item constructor
     *      | item(weight, Value, shinelevel, ItemType.BACKPACK)
     *
     * @throws InvalidItemsException gets thrown when items are invalid or cant be stored
     *      | !(canStoreAll(Content) || areProperItem(Content))
     *
     */
    public Backpack(double weight, int Value, int Capacity, Entity Holder, AnchorPoint anchorPoint, ShineLevel shinelevel, ArrayList<Item> Content) throws InvalidHolderException, InvalidValueException, InvalidItemsException {
        super(weight, Value, shinelevel, ItemType.BACKPACK);
        if (this.areProperItems(Content)){
            throw new InvalidItemsException("all backpack items must belong to this backpack");
        }
        if (!canStoreAll(Content)){
            throw new InvalidItemsException("all items must fit in this backpack");
        }
        this.setCapacity(Capacity);
        if (Holder != null && Holder.canEquip(this)) {
            Holder.equip(anchorPoint, this);
        }
        for (Item item : Content) {
            this.storeItem(item);
        }
    }

    /**
     * A constructor for a backpack with given weight, value, capacity, Holder, anchorpoint, shineLevel
     *
     * @param weight
     *      the weight of the backpack
     *
     * @param Value
     *      the value of the backpack
     *
     * @param Capacity
     *      the capacity of this backpack
     *
     * @param Holder
     *      the holder of this backpack
     *
     * @param anchorPoint
     *      the anchorpoint this backpack attaches to
     *
     * @param shinelevel
     *      the shineLevel of this backpack
     *
     * @effect creates a backpack using an item constructor
     *      | Item(weight, Value, Holder, anchorPoint, shinelevel, ItemType.BACKPACK)
     */
    public Backpack( double weight, int Value, int Capacity, Entity Holder, AnchorPoint anchorPoint, ShineLevel shinelevel) throws InvalidValueException, InvalidHolderException {
        super(weight, Value, Holder, anchorPoint, shinelevel, ItemType.BACKPACK);
        this.setCapacity(Capacity);
        this.Content = new ArrayList<Item>();
    }

    /**
     *A constructor for a backpack with a given Holder and anchorpoint
     *
     * @param Holder
     *      the holder of this backpack
     *
     * @param anchorPoint
     *      the anchorpoint this backpack attaches to
     *
     * @effect creates a backpack using a more advanced backpack constructor
     *      | this(defaultWeight, defaultValue, defaultCapacity, Holder, anchorPoint, ShineLevel.LOW)
     */
    public Backpack(Entity Holder, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        this(defaultWeight, defaultValue, defaultCapacity, Holder, anchorPoint, ShineLevel.LOW);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * getter for the capacity of a backpack
     *
     * @return the capacity of the backpack
     *      | this.Capacity
     */
    @Basic
    public int getCapacity(){
        return Capacity;
    }
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
    @Raw
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
    @Raw
    public Item getItemAt(int index){
        if(index >= this.getAmountOfItems()){
            return null;
        }
        return Content.get(index);
    }

    /**
     * generates an unique id for every backpack
     *
     * @return the uniquely created Id
     *      | nextId = IdGenerator
     *      | idGenerator + 1
     *      | result = nextId
     */
    @Override @Raw
    protected long generateUniqueId() {
        long nextId;
        do {
            nextId = idGenerator.getAndAdd(1);
        } while (nextId <= 0);
        return nextId;
    }

    /**
     * getter for the AMount of items in the content of  a backoack
     *
     * @return the amount of items in a backpack
     *      | this.Content.size()
     */
    @Raw
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
    @Override @Raw
    public double getTotalWeight(){
        double totalWeight = this.getWeight();
        if (!(Content ==  null)){
            for (Item item : Content){
                totalWeight += item.getTotalWeight();
            }
            return totalWeight;
        }
        return totalWeight;
    }

    /**
     * getter for the weight of the content of a backpack
     *
     * @return the weight of every item in the backpack combined plus the backpacks own weight
     *      | TotalWeight == 0
     *      | for each item in Content
     *      |   TotalWeight += item.getWeight
     *      | result == TotalWeight
     */
    @Raw
    public double getContentWeight(){
        double totalWeight = 0;
        if (!(Content ==  null)) {
            for (Item item : Content) {
                totalWeight += item.getTotalWeight();
            }
        }
        return totalWeight;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * checks if a backpack can store all its items
     *
     * @return true if it can store all its items, false otherwise
     *      | result == this.getTotalWeight() <= this.Capacity
     */
    public boolean canHoldItems(){
        return this.getTotalWeight() <= this.Capacity;
    }

    /**
     * stores an item in a backpack
     *
     * @param item
     *      the item we want to store
     */
    @Raw
    public void storeItem(Item item){
        this.addItem(item);
    }

    /**
     * unpacks an item from a backpack
     *
     * @param item
     *      item we want to unpack
     */
    @Raw
    public void unpackItem(Item item){
        this.removeItem(item);
    }

    /**
     * removes an item from the content of a backpack
     *
     * @pre Backpack must have item in its content
     *      | this.hasAsItem(item)
     *
     * @param item
     *      item we want to remove
     *
     * @effect backpack of item is set to null
     *      | item.setBackpack(null)
     *
     * @post item is removed from the backpacks content
     *      | this.Content.remove(item);
     */
    @Raw
    private void removeItem(Item item){
        if (!this.hasAsItem(item)){
            return;
        }
        try {
            item.setHolder(null);
        } catch (InvalidHolderException e) {
            assert false;
        }
        this.Content.remove(item);
        item.setBackpack(null);
    }

    /**
     * a method to add an item to the content of a backpack
     *
     * @pre must be able to store Item
     *      | canAddItem(item)
     *
     * @param item
     *      the item we want to add to the content
     */
    @Raw @Model
    private void addItem(Item item){
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
        item.setBackpack(this);
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
        if ((this.getContentWeight() + item.getWeight() >= this.Capacity)){
            return false;
        }
        if (this.getHolder() == null || item.getHolder() == null){
            return !item.isTerminated() && !this.isTerminated();
        }
        if (!this.getHolder().canEquip(item)){
            return false;
        }
        return !item.isTerminated() && !this.isTerminated() && !this.getHolder().isTerminated() && !item.getHolder().isTerminated();
    }

    /**
     * checks if all item in the backpack belong the backpacks holder
     *
     * @return true if all items belong to the backpacks holder, false otherwise
     *      | for each item in content
     *      |       if (item.isTerminated())
     *      |           result == false
     *      | result == true
     */
    @Raw
    public boolean areProperItems(ArrayList<Item> content){
        for (Item item : content){
            if (item.isTerminated()){
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
    @Raw
   public boolean isValidCapacity(int capacity){
       return capacity > 0;
   }

    /**
     * checks whether a backpack contains a certain item
     *
     * @param item
     *      the item we are looking for
     *
     * @return true if item is in backpack, false otherwise
     *      | result == this.Content.contains(item)
     */
    @Raw
   public boolean hasAsItem(Item item){
       return this.Content.contains(item);
   }

    /**
     * checks if a backpack can store all items from an array
     *
     * @param items
     *      items we want to check if we can store
     *
     * @return true if we can store all items, false if not
     *      | if items == null OR items.isEmpty()
     *      |   return true
     *      | for each item in items
     *      |   totalWeight += item.getWeight
     *      | currentWeight = this.getTotalWeight()
     *      | result == (currentWeight + totalWeight) <= this.Capacity
     */
    @Raw
   public boolean canStoreAll(List<Item> items) {
       if (items == null || items.isEmpty()) return true;

       double totalWeight = items.stream().mapToDouble(Item::getWeight).sum();

       double currentWeight = this.getContentWeight();

       return (currentWeight + totalWeight) <= this.Capacity;
    }
}
