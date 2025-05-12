package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

/**
 * abstract class representing all items
 *
 * @invar every item must have a Valid holder
 *      | hasValidHolder()
 *
 * @invar each different type of item must have a unique ID
 *      | !(item1.getItemType() == item2.getItemType())
 *
 * @invar Value cannot exceed maxValue
 *      | isValidValue()
 *
 */
public abstract class Item {

    /**********************************************************
     * Variables
     **********************************************************/

    private ItemType itemType;

    private final long Id;

    private double Weight;

    private static final double defaultWeight = 10;

    private final int maxValue = 500;

    private int Value = 0;

    private Entity Holder; //TODO: can be monster?

    private ShineLevel ShineLevel;

    private Boolean terminated = false;

    private Backpack backpack = null;

    /**********************************************************
     * Constructors
     **********************************************************/

    protected Item(double weight, int Value, Entity Holder, AnchorPoint anchorpoint, ShineLevel ShineLevel, ItemType itemType) throws InvalidHolderException, InvalidValueException {
        if (Holder != null) {
            if (Holder.isTerminated()) {
                throw new InvalidHolderException("Holder cannot be terminated");
            }
        }
        if(!isValidValue(Value)){
            throw new InvalidValueException("Value cannot be negative");
        }
        if (!isValidWeight(weight)){
            this.Weight = 10;
        } else this.Weight = weight;

        this.setValue(Value);
        this.itemType = itemType;
        this.ShineLevel = ShineLevel;
        if (Holder != null) {
            Holder.equip(anchorpoint, this);
        }
        this.Id = generateUniqueId(); //TODO: override for weapon and backpack
    }

    /**
     * Auxiliary constructor //TODO
     *
     * @param ShineLevel
     * @param itemType
     */
    protected Item(double weight, int Value, ShineLevel ShineLevel, ItemType itemType) throws InvalidHolderException, InvalidValueException {
        if (!hasValidHolder(Holder)){
            throw new InvalidHolderException("Holder cannot be terminated");
        }
        if(!isValidValue(Value)){
            throw new InvalidValueException("Value cannot be negative");
        }
        if (!isValidWeight(weight)){
            this.Weight = 10;
        } else this.Weight = weight;

        this.setValue(Value);
        this.itemType = itemType;
        this.ShineLevel = ShineLevel;
        this.Id = generateUniqueId(); //TODO: overide for weapon and backpack
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    public long getId() {
        return Id;
    }

    public int getDamage(){
        return 0;
    }

    public ShineLevel getShineLevel(){
        return this.ShineLevel;
    }

    public abstract int getAmountOfItems();

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
    @Basic
    public Entity getHolder() {
        return Holder;
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
    protected void setHolder(Entity Holder) throws InvalidHolderException {
        if (isTerminated()){ //TODO: ask about this checker and the hasValidHolder
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
    @Basic
    public double getWeight() {
        return Weight;
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
            this.Weight = 10;
        } else this.Weight = weight;
    }

    /**
     * getter for the totalweight of an Item
     *
     * @return the weight of an item
     *      | this.getWeight
     */ //TODO: ask
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
    protected void setValue(int value) throws InvalidValueException {
        if(!isValidValue(value)){
            throw new InvalidValueException("Value cannot be negative");
        }
        this.Value = calculateValue(value);
    }

    /**
     * getter for the ItemType of an Item
     *
     * @return the ItemType
     *      | this.itemType
     */
    @Basic
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * getter for the Backpack of an Item
     * @return
     */
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
    protected int calculateValue(int dependendValue){
        return dependendValue;
    }

    /**
     * generates an unique id for every Item
     *
     * @return the uniquely created Id
     *      | //TODO: ask about formal
     */
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
    public boolean isValidWeight(double weight) {
        return weight >= 0;
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
    public boolean hasValidHolder(Entity Holder){
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
    public boolean isValidValue(int value){
        return value >= 0 && value <= this.getMaxValue();
    }

    /**
     * checks if a given Item is terminated
     *
     * @return true if Item is terminated, false otherwise
     *      | this.Terminated
     */
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * checks if the Item is valid
     *
     * @return true if Item is not terminated and has a valid Holder, false otherwise
     *      | if(!isTerminated && hasValidHolder)
     *      | then result == true
     */
    public boolean isValidItem(){
        return !isTerminated() && hasValidHolder(this.getHolder());
    }
}
