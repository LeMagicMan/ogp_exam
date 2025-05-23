package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

/**
 * abstract class representing all items
 *
 * @invar every item must have a Valid holder
 *      | isValidHolder()
 *
 * @invar each different type of item must have a unique ID
 *      | !(item1.getItemType() == item2.getItemType())
 *
 * @invar Value cannot exceed maxValue
 *      | isValidValue()
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public abstract class Item {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A variable representing the default Damage of a weapon
     */
    private static final int defaultDamage = 0;

    /**
     * A variable representing the itemType of an item
     */
    private final ItemType itemType;

    /**
     * A variable representing the Id of an Item
     */
    private final Long Id;

    /**
     * A variable representing the weight of an Item
     */
    private double Weight;

    /**
     * A variable representing the defaultWeight of an Item
     */
    private static final double defaultWeight = 10;

    /**
     * A variable representing the maxValue of an Item
     */
    private final int maxValue = 500;

    /**
     * A variable representing the value of an Item
     */
    private int Value = 0;

    /**
     * A variable representing the Holder of an Item
     */
    private Entity Holder;

    /**
     * A variable representing the Shinelevel of an item
     */
    private final ShineLevel ShineLevel;

    /**
     * A variable representing the terminated state of an item
     */
    private boolean terminated = false;

    /**
     * A variable representing the backpack an item is stored in
     */
    private Backpack backpack = null;

    /**
     * the maximum weight an item can be
     */
    private static final double maxWeight = 150;

    /**
     * A variable representing the damage of a weapon
     */
    private int Damage = 0;

    /**
     * A variable representing the maxDamage of a weapon
     */
    private static final int maxDamage = 100;


    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * A constructor for an item with given weight, Value, Holder, anchorpoint, ShineLevel and itemType
     *
     * @pre Holder must be Valid
     *      | !Holder.isTerminated()
     *
     * @pre Value must be Valid
     *      | isValidValue(value)
     *
     * @pre weight must be valid
     *      | isValidWeight(weight)
     *
     * @param weight
     *      weight of the item
     *
     * @param Value
     *      value of the item
     *
     * @param Holder
     *      Holder of the Item
     *
     * @param anchorpoint
     *      anchorpoint of the item
     *
     * @param ShineLevel
     *      ShineLevel of the item
     *
     * @param itemType
     *      ItemType of the Item
     *
     * @throws InvalidHolderException gets thrown when Holder is not Valid
     *      | Holder.isTerminated()
     *
     * @throws InvalidValueException gets thrown when value isn't valid
     *      | !isValidValue(value)
     *
     * @post if weight is not valid set it to the defaultWeight
     *      | this.setWeight(defaultWeight)
     *
     * @post weight of Item is set
     *      | this.weight = weight
     *
     * @post value of item is set
     *      | this.setValue(Value)
     *
     * @post itemType of item is set
     *      | this.ItemType = itemType
     *
     * @post shineLevel of Item is set
     *      | this.ShineLevel = ShineLevel
     *
     * @post item is equiped to Holder on the right anchorpoint
     *      | Holder.equip(anchorpoint, this)
     *
     * @post item is created with a unique Id
     *      | this.Id = generateUniqueId()
     */
    protected Item(double weight, int Value, Entity Holder, AnchorPoint anchorpoint, ShineLevel ShineLevel, ItemType itemType) throws InvalidHolderException, InvalidValueException {
        if (Holder != null) {
            if (Holder.isTerminated()) {
                throw new InvalidHolderException("Holder cannot be terminated");
            }
        }
        if (!isValidWeight(weight)){
            this.Weight = defaultWeight;
        } else this.Weight = weight;
        this.setValue(Value);
        this.itemType = itemType;
        this.ShineLevel = ShineLevel;
        if (Holder != null && Holder.canEquip(this)) {
            Holder.equip(anchorpoint, this);
        }
        this.Id = generateUniqueId();
    }

    /**
     * Auxiliary constructor for an Item with given weight, Value, ShineLevel and itemType
     *
     * @pre Value must be Valid
     *      | isValidValue(value)
     *
     * @pre weight must be valid
     *      | isValidWeight(weight)
     *
     * @param weight
     *      weight of the item
     *
     * @param Value
     *      value of the item
     *
     * @param ShineLevel
     *      ShineLevel of the item
     *
     * @param itemType
     *      ItemType of the Item
     *
     * @throws InvalidValueException gets thrown when value isn't valid
     *      | !isValidValue(value)
     *
     * @post if weight is not valid set it to the defaultWeight
     *      | this.setWeight(defaultWeight)
     *
     * @post weight of Item is set
     *      | this.weight = weight
     *
     * @post value of item is set
     *      | this.setValue(Value)
     *
     * @post itemType of item is set
     *      | this.ItemType = itemType
     *
     * @post shineLevel of Item is set
     *      | this.ShineLevel = ShineLevel
     *
     * @post item is created with a unique Id
     *      | this.Id = generateUniqueId()
     */
    protected Item(double weight, int Value, ShineLevel ShineLevel, ItemType itemType) throws InvalidValueException {
        if (!isValidWeight(weight)){
            this.Weight = defaultWeight;
        } else this.Weight = weight;

        this.setValue(Value);
        this.itemType = itemType;
        this.ShineLevel = ShineLevel;
        this.Id = generateUniqueId();
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * getter for the Id of an Item
     *
     * @return Id of the Item
     *      | this.Id
     */
    @Basic
    public long getId() {
        return Id;
    }

    /**
     * getter for the Damage of an Item
     *
     * @return the Damage of this Item
     *      | this.Damage
     */
    @Basic
    public int getDamage() {
        return Damage;
    }

    /**
     * setter for the damage of a weapon
     *
     * @param damage
     *      damage we want to set
     *
     * @pre Damage must be valid
     *      | isValidDamage(damage)
     */
    @Raw
    protected void setDamage(int damage) {
        this.Damage = damage;
    }

    /**
     * getter for the shineLevel of an Item
     *
     * @return the ShineLevel of an Item
     *      | this.ShineLevel
     */
    @Basic
    public ShineLevel getShineLevel(){
        return this.ShineLevel;
    }

    /**
     * abstract getter for the amount of Items an item holds
     *
     * @return the amount of Items in the Item
     */
    @Raw
    public abstract int getAmountOfItems();

    /**
     * abstract getter for an item at a certain index in another item
     *
     * @param index
     *      index of the item contained in this item
     *
     * @return the item at the index
     */
    @Raw
    public abstract Item getItemAt(int index);

    /**
     * getter for the maxValue of an Item
     *
     * @return the maxvalue of that Item
     *      | this.maxValue
     */
    @Basic
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * getter for the holder of an Item
     *
     * @return the holder of the Item
     *      | this.Holder
     */
    @Basic @Raw
    public Entity getHolder() {
        return Holder;
    }

    /**
     * returns the default damage of an item
     *
     * @return the defaultDamage
     *      | this.defaultDamage
     */
    public int getDefaultDamage(){
        return defaultDamage;
    }

    /**
     * sets the holder of an Item
     *
     * @pre Holder must be Valid
     *      | isValidHolder(Holder)
     *
     * @param Holder
     *      the holder it needs to be set to
     *
     * @throws InvalidHolderException
     *      gets thrown when Holder is not valid
     *          | !isValidHolder(Holder)
     *
     * @post the Holder of the Item is set to the given Holder
     *      | this.Holder = Holder
     */
    @Raw @Model
    protected void setHolder(Entity Holder) throws InvalidHolderException {
        if (isTerminated()){
            throw new InvalidHolderException("Holder cannot be terminated");
        }
        this.Holder = Holder;
    }

    /**
     * getter for the weight of an item
     *
     * @return the weight of the item
     *      | this.Weight
     */
    @Basic @Raw
    public double getWeight() {
        return Weight;
    }

    /**
     * getter for the value of an item
     *
     * @return the value of that item
     *      | this.value
     */
    public int getValue(){
        return Value;
    }


    /**
     * sets the weight of an Item
     *
     * @pre weight must be valid
     *      | isValidWeight(weight)
     *
     * @effect if weight is not valid set weight to the default weight
     *      | if (!isValidWeight)
     *      | then this.Weight = defaultWeight
     *
     * @param weight
     *      the weight that needs to be set
     *
     * @post the Weight of the Item is set to the given weight
     *      | this.Weight = weight
     */
    protected void setWeight(double weight){
        if (!isValidWeight(weight)){
            this.Weight = defaultWeight;
        } else this.Weight = weight;
    }

    /**
     * getter for the totalweight of an Item
     *
     * @return the weight of an item and all items it contains
     */
    @Raw
    public double getTotalWeight(){
        return getWeight();
    }

    /**
     * setter for the value of an Item
     *
     * @param value value that needs to be set
     *
     * @pre value needs to be bigger than or equal to 0
     *      | value >= 0
     *
     * @pre value needs to be smaller than or equal to MaxValue
     *      | value <= MaxValue
     *
     * @throws InvalidValueException
     *      gets thrown when value is smaller than zero or bigger than maxValue
     *          | result == !(isValidValue())
     *
     * @post value is set as th value of the Item
     *      | this.Value = value
     */
    @Raw
    protected void setValue(int value) throws InvalidValueException {
        value = calculateValue(value);
        if(!isValidValue(value)){
            throw new InvalidValueException("Value cannot be negative");
        }
        this.Value = value;
    }

    /**
     * getter for the ItemType of an Item
     *
     * @return the ItemType
     *      | this.itemType
     */
    @Basic @Raw
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * sets the backpack of an Item
     *
     * @pre Item must be in Content of backpack, if backpack null, backpack of item needs to be set to null
     *      | if backpack == null
     *      | then this.setBackpack(null)
     *      | backpack.hasAsItem(this)
     *
     * @param backpack
     *      the backpack we want to add an item to
     */
    @Raw @Model
    protected void setBackpack(Backpack backpack){
        this.backpack = backpack;
    }

    /**
     * getter for the Backpack of an Item
     *
     * @return the backpack of this item
     *      | this.backpack
     */
    @Raw
    public Backpack getBackpack() {
        return backpack;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * calculates the Value of an Item using a variable the value depends on
     *
     * @pre dependedValue cannot be negative
     *      | dependedValue >= 0
     *
     * @param dependendValue
     *      the variable Value depends on
     *
     * @return dependedValue
     */
    @Raw
    protected int calculateValue(int dependendValue){
        return dependendValue;
    }

    /**
     * generates an unique id for every Item
     *
     * @return the uniquely created Id
     */
    @Raw
    protected abstract long generateUniqueId();

    /**
     * checks if the weight of an Item is a valid one
     *
     * @param weight
     *      weight that needs to be checked
     *
     * @return true if weight is valid, otherwise false
     *      | result == weight >= 0
     */
    @Raw
    public boolean isValidWeight(double weight) {
        return weight >= 0 && weight <= maxWeight;
    }

    /**
     * checks if the holder of an item is a valid one
     *
     * @param Holder
     *      the holder that needs to be checked
     *
     * @return true if holder is valid, false otherwise
     *      | !Holder.isTerminated()
     */
    public boolean isValidHolder(Entity Holder){
        if (Holder == null) return true;
        return !Holder.isTerminated() && Holder.hasAsItem(this);
    }

    /**
     * checks if the value of an item is valid
     *
     * @param value
     *      the value that needs to be checked
     *
     * @return true if the value is valid, false otherwise
     *      | result == value >= 0 && value <= this.MaxValue
     */
    @Raw
    public boolean isValidValue(int value){
        return value >= 0 && value <= this.getMaxValue();
    }

    /**
     * checks if a given Item is terminated
     *
     * @return true if Item is terminated, false otherwise
     *      | this.Terminated
     */
    @Basic @Raw
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * checks if the Item is valid
     *
     * @return true if Item is not terminated and has a valid Holder, false otherwise
     *      | if(!isTerminated && isValidHolder)
     *      | then result == true
     */
    @Raw
    public boolean isValidItem(){
        return !isTerminated() && isValidHolder(this.getHolder());
    }

    /**
     * checks if the Damage of a weapon is valid
     *
     * @return true
     */
    public boolean isValidDamage(int Damage){
        return Damage == 0;
    }

    /**
     * terminates an Item and its relations with other classes
     *
     * @effect makes sure both the bidirectional relation with Entity and Backpack is removed properly
     *      | if backpack != null
     *      | then backpack.unpackItem(this)
     *      | Holder != null
     *      | this.getHolder().unequip(this.getHolder().getAnchorPointWithItem(this), this )
     *
     * @effect if this item has content remove everything out of its content
     *      | if this.getAmountOfItems() > 0
     *      |   for index < this.getAmountOfItems(); index++
     *      |       this.getBackpack().unpackItem(this.getItemAt(index))
     */
    public void terminate(){
        terminated = true;
        if (this.getAmountOfItems() > 0){
            for (int index = 0; index < this.getAmountOfItems(); index++){
                this.getBackpack().unpackItem(this.getItemAt(index));
            }
        }
        if (backpack != null) {
            backpack.unpackItem(this);
        }
        if (Holder != null) {
            this.getHolder().unequip(this.getHolder().getAnchorPointWithItem(this), this );
        }
    }
}
