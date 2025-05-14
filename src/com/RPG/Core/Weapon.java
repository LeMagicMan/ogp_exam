package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A class representing a backpack Item
 *
 * @invar Weapon must have a valid damage
 *      | isValidDamage
 */
public class Weapon extends Item {
    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A variable Representing A thread-safe atomic counter used to generate unique IDs starting at 6
     */
    private static final AtomicLong idGenerator = new AtomicLong(6);

    /**
     * A variable representing the damage of a weapon
     */
    private int Damage;

    /**
     * A variable representing the value per Damage of a weapon
     */
    private static int valuePerDamage = 2;

    /**
     * A variable representing the maxValue of a weapon
     */
    private final int maxValue = 200;

    /**
     * A variable representing the maxDamage of a weapon
     */
    private static final int maxDamage = 100;

    /**
     * A variable representing the default Damage of a weapon
     */
    private static final int defaultDamage = 10;

    /**
     * A variable representing the default weight of a weapon
     */
    private static final double defaultWeight= 10;

    /**********************************************************
     * Constructors
     *********************************************************/

    /**
     * A constructor for a weapon with a given weight, Holder, anchorpoint, ShineLevel and Damage
     *
     * @pre Damage must be valid
     *      | isValidDamage(Damage)
     *
     * @param weight
     *      weight of the weapon
     *
     * @param Holder
     *      Holder of the weapon
     *
     * @param anchorPoint
     *      anchorpoint of the weapon
     *
     * @param ShineLevel
     *      ShineLevel of the weapon
     *
     * @param Damage
     *      Damage of the Weapon
     *
     * @post if damage is not valid set it to the defaultDamage
     *      | if (!isValidDamage(Damage))
     *      |   then this.Damage = defaultDamage
     */
    public Weapon(double weight, Entity Holder, AnchorPoint anchorPoint, ShineLevel ShineLevel, int Damage) throws InvalidHolderException, InvalidValueException {
        super(weight, Damage, Holder, anchorPoint, ShineLevel, ItemType.WEAPON);
        if (!isValidDamage(Damage)){
            this.Damage = defaultDamage;
        } else this.Damage = Damage;
    }

    /**
     * A constructor for a weapon with a given Holder and anchorpoint
     *
     * @param Holder
     *      Holder of the weapon
     *
     * @param anchorPoint
     *      anchorpoint of the weapon
     *
     */
    public Weapon( Entity Holder, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        this(defaultWeight, Holder, anchorPoint, ShineLevel.LOW, defaultDamage);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * getter for the Damage of a weapon
     *
     * @return the Damage of this weapon
     *      | this.Damage
     */
    @Override @Basic //TODO: basic, think about if override is nescesary
    public int getDamage() {
        return Damage;
    }

    /**
     * getter for the amount of items in a weapon
     *
     * @return 0
     */
    @Override
    public int getAmountOfItems() {
        return 0;
    }

    /**
     * returns the default damage of a weapon
     *
     * @return the defaultDamage
     *      | this.defaultDamage
     */
    @Override
    public int getDefaultDamage(){
        return defaultDamage;
    }

    /**
     * getter for an Item at a given Index in a weapon
     *
     * @param index
     *      index of the item contained in this item
     *
     * @return null
     */
    @Override
    public Item getItemAt(int index) {
        return null;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * generates an unique id for every Item
     *
     * @return the uniquely created Id
     *      | //TODO
     */
    @Override
    protected long generateUniqueId() {
        long nextId;
        do {
            nextId = idGenerator.getAndAdd(6);
        } while (nextId <= 0 || nextId % 6 != 0);
        return nextId;
    }

    /**
     * calculates the value of weapon using its Damage
     *
     * @pre Damage must be valid
     *      | isValidDamage(Damage)
     *
     * @effect if damage is not valid calculate value using defaultDamage
     *      | if(!isValidDamage(Damage))
     *      |   then result == DefaultDamage * valuePerDamage
     *
     * @param Damage
     *      the variable Value depends on
     *
     * @return the calculated Value
     *      | Damage * valuePerDamage
     */
    @Override
    protected int calculateValue(int Damage) {
        if (!isValidDamage(Damage)){
            return defaultDamage * valuePerDamage;
        }
        return Damage * valuePerDamage;
    }

    /**
     * checks if the Damage of a weapon is valid
     *
     * @return true if Damage is bigger than zero, and smaller than maxDamage, false otherwise
     *      | result == (Damage > 0 && Damage <= maxDamage)
     */
    @Override
    public boolean isValidDamage(int Damage){
        return Damage > 0 && Damage <= maxDamage;
    }
}
