package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidDamageTypesException;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidSkinTypeException;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;


/**
 * A class representing all Entity-types
 *
 * @invar Every entity must have a valid name
 *      | isValidName(getName())
 *
 * @invar Every entity must have a valid amount of hitpoints
 *      | isValidHP(getHP)
 *
 * @invar an entity can have multiple DamageTypes except if one of those is Normal then it can only have the normal type, and can only have 1 of each, and cannot be empty
 *      | hasValidDamageTypes()
 *
 * @invar every Item in an anchorpoint of an entity most have this entity as its holder, even the items contained in other items contained in this entity's anchorpoints
 *      | hasValidItems()
 *
 * @invar An entity must have a valid Strength
 *      | isValidStrength()
 *
 * @invar an entity musty have a valid capacity
 *      | isValidCapacity()
 *
 * @invar an entity can not hold more items than its capacity can hold
 *      | canHoldItems()
 */
public abstract class Entity {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A variable representing the name of an entity (defensive)
     */
    private String Name = "";

    /**
     * A variable representing the maximum amount of hitpoints of an entity (nominal)
     */
    private Long MaxHP = 0L;

    /**
     * A variable representing the current amount of hitpoints (nominal)
     */
    private long HP = 0L;

    /**
     * A variable representing the AnchorPoints of an entity
     */
    private ArrayList<AnchorPoint> AnchorPoints = new ArrayList<>();

    /**
     * A variable representing the terminated status of an entity
     */
    private boolean Terminated = false;

    /**
     * A regex that the name of an entity needs to follow
     */
    private static final String nameRegex = "^.*+$";

    /**
     * A variable representing the skinType of an entity
     */
    private SkinType skinType;

    /**
     * A variable representing the capacity of an entity
     */
    private long Capacity = 0;

    /**
     * A variable representing the DamageType of an entity
     */
    private HashSet<DamageType> DamageTypes = new HashSet<>();

    /**
     * A Variable representing the strength of a Hero
     */
    private BigDecimal Strength = BigDecimal.valueOf(0);

    /**
     * A Variable representing the protection of a Hero
     */
    private int Protection = 0;

    /**
     * A variable representing the precision of the strength variable
     */
    private static final int strengthScale = 2;

    /**
     * A variable representing the way to round the strength to the set strengthScale
     */
    private static final RoundingMode roundingMode = RoundingMode.HALF_UP;

    /**
     * A map representing the currently equipped items at each anchor point
     */
    private final Map<AnchorPoint, Item> equipment = new EnumMap<>(AnchorPoint.class);

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * A constructor for an abstract entity
     *
     * @param name
     *      name of the entity
     *
     * @param maxHP
     *      maximum HP of the entity
     *
     * @param Anchorpoints
     *      The anchorpoints of an entity
     *
     * @throws InvalidNameException gets thrown when the name isn't valid
     *      | !isValidName()
     *
     * @throws InvalidSkinTypeException gets thrown when skinType isn't valid
     *      | !isValidSkinType()
     *
     * @throws InvalidDamageTypesException gets thrown when damageTypes are not valid
     *      | !hasValidDamageTypes
     *
     * @pre name must be Valid
     * | isValidName()
     *
     * @pre maxHP must be Valid
     * | isValidHP()
     *
     * @pre SkinType must be Valid
     * | isValidSkinType()
     *
     * @pre must have valid damageTypes
     * | hasValidDamageTypes()
     */
    protected Entity(String name, Long maxHP, ArrayList<AnchorPoint> Anchorpoints, SkinType skinType, HashSet<DamageType> damageTypes) throws InvalidNameException, InvalidSkinTypeException, InvalidDamageTypesException {
        if (!isValidName(name)) {
            throw new InvalidNameException("name must follow naming rules");
        }
        if (!isValidSkinType(skinType)) {
            throw new InvalidSkinTypeException("SkinType is not Valid");
        }
        if (!areValidDamageTypes(damageTypes)) {
            throw new InvalidDamageTypesException("DamageTypes are not valid");
        }
        this.Name = name;
        this.MaxHP = maxHP;
        this.HP = maxHP;
        this.AnchorPoints.addAll(Anchorpoints);
        this.skinType = skinType;
        this.DamageTypes.addAll(damageTypes);
    }


    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * setter for the strength of an entity
     *
     * @param strength
     *      Strength we want to set
     */
    protected void setStrength(BigDecimal strength) {
        this.Strength = strength;
    }

    /**
     * setter for the protection of an entity
     *
     * @param protection
     *      protection we want to set
     */
    protected void setProtection(int protection) {
        if (isValidProtection(protection)) {
            this.Protection = protection;
        }
    }

    /**
     * getter for the roundingMode of the strength
     *
     * @return the roundingMode
     *      | this.RoundingMode
     */
    @Basic
    protected RoundingMode getRoundingMode() {
        return roundingMode;
    }

    /**
     * getter for the precision of the strength
     *
     * @return the strengthScale
     *      | this.strengthScale
     */
    @Basic
    protected int getStrengthScale() {
        return strengthScale;
    }

    /**
     * getter for the strength of a Hero
     *
     * @return strength of hero
     *      | this.Strength
     */
    @Basic
    public BigDecimal getStrength() {
        return this.Strength;
    }

    /**
     * setter for the Damagetypes of an entity
     *
     * @param damageTypes
     *      the damageTypes we want to set
     */
    protected void setDamageTypes(HashSet<DamageType> damageTypes) {
        DamageTypes = damageTypes;
    }

    /**
     * a getter for an anchorpoint at a certain index
     *
     * @param index the index of the anchorpoint
     *
     * @return the anchorpoint at the given Index
     *      | this.AnchorPoints.get(index)
     *
     * @pre Index must be within range
     */
    public AnchorPoint getAnchorPointAt(int index) {
        if (index >= this.getAmountOfAnchorPoints() || index < 0) {
            return null;
        }
        return AnchorPoints.get(index);
    }

    /**
     * a getter for the amount of anchorpoints
     *
     * @return amount of anchorpoints
     *      | this.AnchorPoints.size()
     */
    public int getAmountOfAnchorPoints() {
        return AnchorPoints.size();
    }

    /**
     * getter for the amount of DamageTypes
     *
     * @return the amount of DamageTypes
     *      | result == DamageTypes.size()
     */
    public int getAmountOfDamageTypes() {
        return DamageTypes.size();
    }

    /**
     * checks if an entity has a certain damageType
     *
     * @param damageType
     *      the damageType we are looking for
     *
     * @return true if entity has damageType, false otherwise
     *      | result == DamageTypes.contains(damageType)
     */
    public boolean hasDamageType(DamageType damageType) {
        return DamageTypes.contains(damageType);
    }

    /**
     * getter for the nameRegex of an entity
     *
     * @return the nameRegex
     *      | this.nameRegex
     */
    public abstract String getNameRegex();

    /**
     * getter for the name of an entity
     *
     * @return the name of an entity
     *      | Entity.Name
     */
    @Basic
    public String getName() {
        return Name;
    }

    /**
     * getter for the MaxHP of an entity
     *
     * @return the MaxHP
     * | Entity.MaxHP
     */
    @Basic
    public long getMaxHP() {
        return MaxHP;
    }

    /**
     * getter for the HP of an entity
     *
     * @return the HP
     * | Entity.HP
     */
    @Basic
    public long getHP() {
        return HP;
    }

    /**
     * getter for the total amount of Damage from all damagetypes
     *
     * @return the total amount of basDamage
     *      | totalDamage = 0
     *      | for each damageType in DamageTypes
     *      |   totalDamage += damageType.getBaseDamage()
     *      | result == totalDamage
     */
    @Model
    private long getTotalDamageTypeDamage() {
        long totalDamage = 0;
        for (DamageType damageType : DamageTypes) {
            totalDamage += damageType.getBaseDamage();
        }
        return totalDamage;
    }

    /**
     * getter for the total amount of damage from weapons in either the right or left hand
     *
     * @return the amount of Damage from the item in each hand
     *      | result == this.getAnchorPoint(AnchorPoint.LEFTHAND).getItem().getDamage() + this.getAnchorPoint(AnchorPoint.RIGHTHAND).getItem().getDamage()
     *
     * @effect if the entity is not intelligent we return 0
     *      | if !this.isIntelligent()
     *      |   result == 0
     */
    @Model
    private long getActiveWeaponDamage() {
        if (this.isIntelligent()) {
            long totalDamage = 0;
            if (this.AnchorPoints.contains(AnchorPoint.LEFTHAND)) {
                if (this.hasItemAt(AnchorPoint.LEFTHAND)) {
                    totalDamage = this.getItemAt(AnchorPoint.LEFTHAND).getDamage();
                }
            }
            if (this.AnchorPoints.contains(AnchorPoint.RIGHTHAND)) {
                if (this.hasItemAt(AnchorPoint.RIGHTHAND)) {
                    totalDamage = this.getItemAt(AnchorPoint.RIGHTHAND).getDamage();
                }
            }
            return totalDamage;
        }
        return 0;
    }

    public Item getItemAt(AnchorPoint anchorPoint) {
        return equipment.get(anchorPoint);
    }

    /**
     * setter for the skinType of an entity
     *
     * @param skinType
     *      skintype of that entity
     */
    @Raw
    protected void setSkinType(SkinType skinType) {
        this.skinType = skinType;
    }

    /**
     * a getter for the protection of an entity
     *
     * @return the protection of that entity
     * | this.Protection
     */
    @Basic
    public int getProtection() {
        return Protection;
    }

    /**
     * getter for the total Defense of an entity
     *
     * @return the amount of baseProtection + Protection from skinType
     *      | result == this.getProtection() + skinType.getProtection()
     */
    public int getDefense() {
        return Protection + skinType.getProtection();
    }

    /**
     * getter for the baseDamage of an entity
     *
     * @return the total amount of base Damage
     *      | totalDamage = 0
     *      | if(this.isIntelligent())
     *      |   totalDamage += this.getActiveWeaponDamage + this.getStrength + this.getTotalDamageTypeDamage - 10
     *      | else totalDamage += his.getStrength + this.getTotalDamageTypeDamage - 10
     *      | result == totalDamage
     *
     * @effect If the entity is intelligent we add the Active weapons damage
     *      | if(this.isIntelligent())
     */
    public long getBaseDamage() {
        BigDecimal totalDamage = BigDecimal.ZERO;

        BigDecimal weaponDamage = BigDecimal.valueOf(this.getActiveWeaponDamage());
        BigDecimal damageTypeDamage = BigDecimal.valueOf(this.getTotalDamageTypeDamage());

        if (this.isIntelligent()) {
            totalDamage = totalDamage.add(Strength).add(weaponDamage).add(damageTypeDamage).subtract(BigDecimal.TEN);
        } else {
            totalDamage = totalDamage.add(Strength).add(damageTypeDamage).subtract(BigDecimal.TEN);
        }

        totalDamage = totalDamage.max(BigDecimal.ZERO);

        return totalDamage.divide(BigDecimal.valueOf(2), RoundingMode.DOWN).longValue();
    }

    /**
     * getter for all items in an item on an anchorpoint, and the item itself
     *
     * @return an arraylist of items
     *          | items = item
     *          | for index = 0; index < item.getAmountOfItems; Index++
     *          |   items.add(item.getItemAt(Index))
     *          | result == items
     */
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();
        for (Item item : equipment.values()) {
            items.add(item);
            for (int i = 0; i < item.getAmountOfItems(); i++) {
                items.add(item.getItemAt(i));
            }
        }
        return items;
    }

    /**
     * a getter for the skintype of an entity
     *
     * @return skintype of the entity
     *      | Entity.skinType
     */
    @Basic
    public SkinType getSkinType() {
        return skinType;
    }

    /**
     * setter for the capacity of an entity
     *
     * @post Capacity is set to the return of calculateCapacity()
     *      | this.Capacity == calculateCapacity()
     */
    @Raw
    protected void setCapacity() {
        this.Capacity = calculateCapacity();
    }

    /**
     * getter for the total weight of all items an entity has
     *
     * @return the total weight of all the entity's items
     *      | for each item in this.getAllItems()
     *      |   totalweight += item.getTotalWeightweight()
     *      | result == totalweight
     */
    public double getTotalWeight() {
        double TotalWeight = 0;
        ArrayList<Item> items = getAllItems();
        for (Item item : items) {
            TotalWeight += item.getTotalWeight();
        }
        return TotalWeight;
    }

    /**
     * a getter for the capacity of an entity
     *
     * @return capacity of that entity
     *      | Entity.Capacity
     */
    public long getCapacity() {
        return Capacity;
    }

    /**
     * finds a given Anchorpoint of the entity
     *
     * @param anchorPoint
     *      AnchorPoint that needs to be found
     *
     * @return The AnchorPoint of the entity if found, null otherwise
     *      | for each ap in AnchorPoints
     *      |   if ap.equals(anchorpoint)
     *      |   then result == ap
     *      | result null
     *
     * @pre given Anchorpoint must be in hashset
     *      | this.hasAnchorpoint(anchorpoint)
     *
     * @effect if anchorpoint was not found return null
     *      | if (!this.hasAnchorpoint(anchorpoint))
     *      | then result == null
     */
    @Model
    protected AnchorPoint getAnchorPoint(AnchorPoint anchorPoint) {
        for (AnchorPoint ap : this.AnchorPoints) {
            if (ap.equals(anchorPoint)) {
                return ap;
            }
        }
        return null;
    }

    /**
     * getter for the adjusted roll of an entity
     *
     * @param roll
     *      roll we need to adjust
     * @return the original roll
     *      | result == roll
     */
    public int getAdjustedRoll(int roll) {
        return roll;
    }

    /**
     * setter for the HP of an entity
     * ensures the HP is minimum 0 and maximum the maxHP
     *
     * @param newHP
     *      the Hp we want to set
     */
    @Model
    private void setHP(long newHP) {
        this.HP = Math.min(Math.max(newHP, 0), this.MaxHP);
    }

    /**
     * getter for an anchorpoint with a certain item
     *
     * @param item
     *      item we want to find the anchorpoint of
     * @return anchorpoint of the item
     *      | for each anchorpoint in anchorpoints
     *      |   if anchorpoint.getItem == item
     *      |       result == anchorpoint
     *      | result == null
     */
    public AnchorPoint getAnchorPointWithItem(Item item) {
        if (item == null) return null;

        for (AnchorPoint anchorPoint : this.AnchorPoints) {
            if (equipment.containsKey(anchorPoint) && equipment.get(anchorPoint).equals(item)) {
                return anchorPoint;
            }
        }
        return null;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * normalises the HP of an entity
     *
     * @effect makes the HP prime again
     *      | makePrime(this.getHP())
     */
    public void normaliseHP() {
        makePrime(this.HP);
    }

    /**
     * checks if an entity has an item at a certain anchorPoint
     *
     * @param anchorPoint
     *      the anchorpoint we want to check
     *
     * @return tue if the anchorpoint has an item, false otherwise or anchorpoint is null
     *      | if (anchorPoint == null) return false;
     *      | result == equipment.containsKey(anchorPoint)
     */
    public boolean hasItemAt(AnchorPoint anchorPoint) {
        if (anchorPoint == null) {
            return false;
        }
        return equipment.containsKey(anchorPoint);
    }

    /**
     * getter for the intelligent of an entity
     *
     * @return true if is Intelligent, false otherwise
     *      | this.Intelligent
     */
    public abstract boolean isIntelligent();

    /**
     * getter for the next prime number
     *
     * @param HP
     *      The number we want to find the next prime number for
     * @return the next prime number
     *      | if HP <= 2
     *      |   result == 2
     *      | if HP % 2 == 0
     *      |   HP++
     *      | while Not isPrime
     *      |   HP += 2
     *      | return HP
     */
    @Model
    private long getNextPrime(long HP) {
        if (HP <= 2) return 2;
        if (HP % 2 == 0) HP++;

        while (!isPrime(HP)) {
            HP += 2;
        }
        return HP;
    }

    /**
     * makes a number prime
     *
     * @param HP
     *      the number we want to make prime
     *
     * @effect gets the next prime number
     *      | this.HP = getNextPrime(HP)
     */
    @Model
    private void makePrime(long HP) {
        this.HP = getNextPrime(HP);
    }

    /**
     * terminates an entity
     *
     * @effect unequips all items on the anchorpoints of an entity
     *      | for each anchorpoint in Anchorpoints
     *      |   if anchorpoint.hasItem()
     *      |       this.unequip(anchorpoint, anchorpoint.getItem)
     *
     * @post entity is terminated
     *      | this.Terminated = true
     */
    @Model
    private void terminate() {
        for (AnchorPoint anchorPoint : AnchorPoints) {
            if (hasItemAt(anchorPoint)) {
                Item item = equipment.get(anchorPoint);
                this.unequip(anchorPoint, item);
            }
        }
        this.Terminated = true;
    }

    /**
     * kills an entity
     *      | terminate()
     */
    public void kill() {
        this.terminate();
    }

    /**
     * reduces the Hp of an entity
     *
     * @param Damage
     *      the Damage delivered to the Hp
     *
     * @effect Hp is the current Hp - Damage
     *      | this.getHP() - Damage
     * @post when HP hits 0 the entity needs to be killed
     *      | kill()
     */
    public void reduceHP(long Damage) {
        if (Damage < 0) {
            return;
        }
        setHP(Math.max(0, this.getHP() - Damage));
    }

    /**
     * increases the HP of an entity
     *
     * @param healingAmount
     *      the amount we want to increase
     *
     * @pre entity must be healable
     */
    public void increaseHP(long healingAmount) {
        if (healingAmount < 0) {
            return;
        }
        if (this.isHealable()) {
            setHP(Math.min(this.getMaxHP(), this.getHP() + healingAmount));
        }
    }

    /**
     * equips a given item to a given anchorpoint
     *
     * @param anchorPoint
     *      the anchorpoint we want to equip the item to
     * @param item
     *      the item we want to equip to the anchorpoint
     *
     * @pre Entity must have anchorpoint
     *      | hasAnchorPoint()
     *
     * @pre item must be valid
     *      | item.isValidItem
     *
     * @pre Entity or item cant be terminated
     *      | this.isTerminated() || item.isTerminated()
     *
     * @pre if item cant be null
     *      | item != null
     *
     * @effect if Item is in a backpack unpack it first
     *      | item.getBackpack().unpack(item)
     *
     * @effect if anchorpoint already has an item, unequip it
     *      | this.unequip(getAnchorPoint(anchorPoint), getAnchorPoint(anchorPoint).getItem());
     *
     * @post Item of anchorpoint is set to given item
     *      | this.getAnchorPoint(anchorPoint).setItem(item)
     *
     * @post Holder of the item is set to this entity
     *      | item.setHolder(this)
     */
    public void equip(AnchorPoint anchorPoint, Item item) {
        if(!this.canEquip(item)){
            return;
        }
        if (item == null) return;

        if (!hasAnchorpoint(anchorPoint) || !item.isValidItem()) {
            return;
        }
        if (this.isTerminated() || item.isTerminated()) {
            return;
        }
        if (!anchorPoint.canAttach(item)) {
            return;
        }
        if (item.getBackpack() != null) {
            item.getBackpack().unpackItem(item);
        }

        if (item.getHolder() != null) {
            item.getHolder().unequip(item.getHolder().getAnchorPointWithItem(item), item);
        }

        equipment.put(anchorPoint, item);

        try {
            item.setHolder(this);
        } catch (InvalidHolderException e) {
            assert false;
        }
    }

    /**
     * unequips an item from an entity
     *
     * @param anchorPoint
     *      the anchorpoint we want to unequip the item from
     * @param item
     *      the item we want to unequip from the anchorpoint
     * @pre Entity must have anchorpoint
     *      | hasAnchorPoint()
     *
     * @pre Anchorpoint must contain Item
     *      | getAnchorPoint(anchorPoint).hasAsItem(item)
     *
     * @pre Entity or item cant be terminated
     *      | this.isTerminated() || item.isTerminated()
     *
     * @pre Anchorpoint must ba able to attach item
     *      | anchorpoint.canAttach(Item)
     *
     * @post set item on anchorpoint to null
     *      | getAnchorPoint(anchorPoint).setItem(null)
     *
     * @post Holder of Item must be set to null
     *      | item.setHolder(null)
     */
    public void unequip(AnchorPoint anchorPoint, Item item) {
        if (!hasAnchorpoint(anchorPoint)) {
            return;
        }

        if (this.isTerminated() || item.isTerminated()) {
            return;
        }

        if (!anchorPoint.canAttach(item)) {
            return;
        }

        this.equipment.remove(anchorPoint, item);

        try {
            item.setHolder(null);
        } catch (InvalidHolderException e) {
            assert false;
        }
    }

    /**
     * a method to calculate the capacity of an entity
     *
     * @return capacity of that entity
     */
    @Model
    @Raw
    protected abstract long calculateCapacity();

    /**
     * Checks whether the given name is a valid name
     *
     * @param name
     *      The name to be checked
     *
     * @return True if the name is valid, not empty and follows the nameRegex, false otherwise
     *      | if (name == null) || !name.matches(this.getNameRegex())
     *      |   result == false
     */
    @Raw
    public boolean isValidName(String name) {
        return name.matches(this.getNameRegex()) && !name.isEmpty();
    }

    /**
     * Checks whether the Hp of an entity is valid
     *
     * @param HP
     *      The Hp that needs to be checked
     * @return true if HP is bigger han or equal to zero, is lower than or Equal to its max HP and the Hp is a Prime number
     *      | result == (this.getHP() >= 0 && this.getHP <= this.getMaxHP() && isPrime(getHP()))
     *
     * @effect If the MaxHP is zero it means the maxHp has not been initialised yet, and thus is the HP allowed to be bigger than it to set it
     *      | if (this.getMaxHP() == 0){result == (this.getHP() >= 0 && isPrime(getHP()))}
     */
    public boolean isValidHp(long HP) {
        if (this.getMaxHP() == 0L) {
            return (HP >= 0 && isPrime(HP));
        }
        return (HP >= 0 && HP <= this.getMaxHP() && isPrime(HP));
    }

    /**
     * Checks whether the HP of an entity is prime
     *
     * @param HP The HP of an entity
     *           | Entity.getHP()
     *
     * @return true if HP is prime, otherwise false
     *      | if HP == 1
     *      | then result == false
     *      | for each index until HP-1
     *      |   HP % index == 0
     */
    @Model
    private boolean isPrime(long HP) {
        if (HP <= 1) {
            return false;
        }
        for (int index = 2; index <= Math.sqrt(HP); index++) {
            if (HP % index == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks whether an entity can heal
     *
     * @return true if an entity can heal, otherwise false
     *      | Entity.Healable
     */
    @Basic
    public abstract boolean isHealable();

    /**
     * checks whether the given damagetypes are valid
     *
     * @param damageTypes
     *      the damagetypes that need to be checked
     * @return true if damageTypes has a length of 1, false otherwise
     *      | result == (damageTypes.size() == 1)
     *
     * @note we are sure the elements are unique because of the used datatype
     */
    public abstract boolean areValidDamageTypes(HashSet<DamageType> damageTypes);

    /**
     * checks whether an entity has validDamageTypes
     *
     * @return if the entity has Valid DamageTypes, false otherwise
     *      | this.areValidDamageTypes(DamageTypes)
     */
    public boolean hasValidDamageTypes(){
        return this.areValidDamageTypes(DamageTypes);
    }

    /**
     * checks whether the SkinType of an entity is valid
     *
     * @param skinType
     *      SkinType we need to check
     *
     * @return true if SKinType is Valid, false otherwise
     */
    public abstract boolean isValidSkinType(SkinType skinType);

    /**
     * checks if all Items in an entity are valid
     *
     * @return true if an item is terminated or does not have this entity as its holder, false otherwise
     *      | for each anchorpoint in anchorpoints
     *      |   for each item in anchorPoint.getAllItems()
     *      |       if item.isTerminated() OR item.getHolder() != this
     *      |           result == false
     *      | result == true
     */
    public boolean hasValidItems() {
        for (AnchorPoint anchorPoint : AnchorPoints) {
            for (Item item : this.getAllItems()) {
                if (item != null) {
                    if (item.isTerminated() || item.getHolder() != this) return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if an entity has a certain item
     *
     * @param item
     *      item we want to check for
     *
     * @return true if entity has item, false otherwise
     *      | for each anchorpoint in anchorpoints
     *      |   if anchorpoint.getItem() == item
     *      |       result == true
     *      | result == false
     */
    public boolean hasAsItem(Item item) {
        if (item == null) return false;

        for (Item item1 : this.getAllItems()) {
            if (item1.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if the entity has give anchorpoint
     *
     * @param anchorPoint
     *      the anchorpoint that needs to be checked
     *
     * @return true if the entity has the anchorpoint, false otherwise
     *      | for Index < getAmountOfAnchorPoints(); Index++
     *      |   if this.getAnchorpointAt(Index) == anchorpoint
     *      |       result == true
     *      | result == false
     */
    public boolean hasAnchorpoint(AnchorPoint anchorPoint) {
        return AnchorPoints.contains(anchorPoint);
    }

    /**
     * checks if a given Entity is terminated
     *
     * @return true if Entity is terminated, false otherwise
     *      | this.Terminated
     */
    @Basic
    public boolean isTerminated() {
        return Terminated;
    }

    /**
     * A checker to see if the protection is valid
     *
     * @param Protection
     *      the protection that needs to be checked
     * @return true if protection is valid, otherwise false
     *      | result == (Protection >= 0)
     */
    public boolean isValidProtection(int Protection) {
        return Protection >= 0;
    }

    /**
     * checks if capacity is Valid
     *
     * @param Capacity
     *      the capacity we want to check
     * @return true if capacity is bigger than 0, false otherwise
     *      | capacity >= 0
     */
    public boolean isValidCapacity(long Capacity) {
        return Capacity >= 0;
    }

    /**
     * checks if an entity can hold all its items
     *
     * @return true if entity can hold all its items, false otherwise
     *      | result == this.getTotalWeight() <= this.Capacity
     */
    public boolean canHoldItems() {
        return this.getTotalWeight() <= this.Capacity;
    }

    /**
     * a checker that checks if a hero has all the needed anchorpoints exactly once
     *
     * @return true if a hero has all the needed anchorpoints exactly once, false otherwise
     */
    public abstract boolean hasProperAnchorpoints();

    /**
     * checks if an entity can equip a certain item
     *
     * @param item
     *      item we want to check for
     *
     * @return true if we can equip Item, false otherwise
     *      | if (item == null) result == true
     *      | result == !(this.getTotalWeight() + item.getTotalWeight() > this.Capacity)
     */
    public boolean canEquip(Item item) {
        if (item == null) return true;
        return !(this.getTotalWeight() + item.getTotalWeight() > this.Capacity);
    }
}
