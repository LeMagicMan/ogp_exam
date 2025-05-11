package com.RPG.Core;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import com.RPG.Exception.InvalidAnchorPointException;
import com.RPG.Exception.InvalidHolderException;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;


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
 *      | hasValidItems() //TODO: create checker
 */
public abstract class Entity {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A variable representing the name of an entity (defensive)
     */
    private String Name= "";

    /**
     * A variable representing the maximum amount of hitpoints of an entity (nominal)
     */
    private Long MaxHP = 0L;

    /**
     * A variable representing the current amount of hitpoints (nominal)
     */
    private Long HP = 0L;

    /**
     * A variable representing the AnchorPoints of an entity
     */
    private ArrayList<AnchorPoint> AnchorPoints = new ArrayList<>();

    /**
     * A variable representing whether an entity can heal
     */
    private final Boolean Healable = false;

    /**
     * A variable representing the terminated status of an entity
     */
    private Boolean Terminated = false;

    /**
     * A regex that the name of an entity needs to follow
     */
    private static final String nameRegex = "^[A-Z][a-zA-Z ’:]*$"; //"^.*+$"; "^[A-Z][a-zA-Z ':]*$" //TODO: ask prof

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

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Constructor for an entity
     *
     * @param name
     *      The name of the Entity
     *
     * @pre MaxHP must be a prime number bigger than 0
     *      | isValidHP(maxHP)
     *
     * @pre name must be a valid name
     *      | isValidName(name)
     *
     * @post HP must be Equal to maxHP
     *
     * @throws InvalidNameException
     *      gets thrown when nam is not valid
     *          | !(isValidName(name))
     */
    protected Entity(String name, Long maxHP, ArrayList<AnchorPoint> Anchorpoints) throws InvalidNameException { //TODO: ask about nominal aspect of HP
        if(!isValidName(name)){
            throw new InvalidNameException();
        }
        this.Name = name;
        this.MaxHP = maxHP;
        this.HP = maxHP;
        this.AnchorPoints = Anchorpoints;
    }


    /**********************************************************
     * Getters and Setters
     **********************************************************/

    public void setDamageTypes(HashSet<DamageType> damageTypes) {
        DamageTypes = damageTypes;
    }

    /**
     * a getter for an anchorpoint at a certain index
     *
     * @pre Index must be within range
     *
     * @param index the index of the anchorpoint
     *
     * @return the anchorpoint at the given Index
     *      | this.AnchorPoints.get(index)
     */
    public AnchorPoint getAnchorPointAt(int index){
        if(index >= this.getAmountOfAnchorPoints()){
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
    public int getAmountOfAnchorPoints(){
        return AnchorPoints.size();
    }

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
     *      | Entity.MaxHP
     */
    @Basic @Raw
    public long getMaxHP() {
        return MaxHP;
    }

    /**
     * getter for the HP of an entity
     *
     * @return the HP
     *      | Entity.HP
     */
    @Basic
    public long getHP() {
        return HP;
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
     * a getter for the skintype of an entity
     *
     * @return skintype of the entity
     *      | Entity.skinType
     */
    @Basic
    public SkinType getSkinType() {
        return skinType; //TODO: ask if this is correct or  return skintype.getProtection() is better
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
     * a getter for the capacity of an entity
     *
     * @return capacity of that entity
     *      | Entity.Capacity
     */
    public long getCapacity() {
        return Capacity;
    }

    /**
     * finds a given Anchorpoint in the hashset
     *
     * @pre given Anchorpoint must be in hashset
     *      | Anchorpoints.contains(anchorpoint)
     *
     * @effect if anchorpoint was not found return null //TODO is this redundant
     *      | if (!AnchorPoints.contains(anchorpoint))
     *      | then result == null
     *
     * @param anchorPoint
     *      AnchorPoint that needs to be found
     *
     * @return The AnchorPoint of the entity if found, null otherwise
     *      | for each ap in AnchorPoints
     *      |   if ap.equals(anchorpoint)
     *      |   then result == ap
     *      | result null
     */
    @Model
    private AnchorPoint getAnchorPoint(AnchorPoint anchorPoint){
        for (AnchorPoint ap : this.AnchorPoints) {
            if (ap.equals(anchorPoint)) {
                return ap;
            }
        }
        return null;
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * equips a given item to a given anchorpoint
     *
     * @pre Entity must have anchorpoint
     *      | hasAnchorPoint()
     *
     * @param anchorPoint
     *      the anchorpoint we want to equip the item to
     *
     * @param item
     *      the item we want to equip to the anchorpoint
     */
    public void equip(AnchorPoint anchorPoint, Item item){
        if (!hasAnchorpoint(anchorPoint) && !item.isValidItem()){ //TODO: make checker
            return;
        }
        if (this.isTerminated() || item.isTerminated()){
            return;
        }
        if (!anchorPoint.canAttach(item)){
            return;
        }
        if (item.getBackpack() != null){
            item.getBackpack().unpackItem(item);
        }
        if(getAnchorPoint(anchorPoint).getItem() != null){
            unequip(getAnchorPoint(anchorPoint), getAnchorPoint(anchorPoint).getItem()); //TODO: ask about second param
        }
        try {
            getAnchorPoint(anchorPoint).setItem(item);
        } catch (InvalidAnchorPointException e) {
            assert false;
        }
        try {
            item.setHolder(this);
        } catch (InvalidHolderException e) {
            assert false;
        }
    }

    public void unequip(AnchorPoint anchorPoint, Item item){
        if (!hasAnchorpoint(anchorPoint)){
            return;
        }
        if (!getAnchorPoint(anchorPoint).hasAsItem(item)){
            return;
        }
        if (this.isTerminated() || item.isTerminated()){
            return;
        }
        if (!anchorPoint.canAttach(item)){
            return;
        }
        try {
            getAnchorPoint(anchorPoint).setItem(null);
        } catch (InvalidAnchorPointException e) {
            assert false;
        }
        try {
            item.setHolder(this);
        } catch (InvalidHolderException e) {
            assert false;
        }
    }

    public AnchorPoint getAnchorPointWithItem(Item item){
        for (AnchorPoint ap : this.AnchorPoints) {
            if (ap.getItem().equals(item)){
                return ap;
            }
        }
        return null;
    }

    /**
     * a method to calculate the capacity of an entity
     *
     * @return capacity of that entity
     */
    @Model @Raw
    protected long calculateCapacity() {
        return 0L;
    }

    /**
     * Checks whether the given name is a valid name
     *
     * @param name
     *      The name to be checked
     *
     * @return True if the name is valid, not empty and follows the nameRegex, false otherwise //TODO: ask idf mentioning nameRegex is allowed
     *      | result == (name != null) && name.matches(nameRegex)
     */
    @Raw
    public Boolean isValidName(String name) {
        return name.matches(nameRegex);
    }

    /**
     * Checks whether the Hp of an entity is valid
     *
     * @param HP
     *      The Hp that needs to be checked
     *
     * @effect
     *      If the MaxHP is zero it means the maxHp has not been initialised yet, and thus is the HP allowed to be bigger than it to set it
     *              | if (this.getMaxHP() == 0){result == (this.getHP() >= 0 && isPrime(getHP()))}
     *
     * @return true if HP is bigger han or equal to zero, is lower than or Equal to its max HP and the Hp is a Prime number
     *      | result == (this.getHP() >= 0 && this.getHP <= this.getMaxHP() && isPrime(getHP()))
     */
    public Boolean isValidHp(Long HP) {
        if (this.getMaxHP() == 0L){
            return (HP >= 0 && isPrime(HP));
        }
        return (HP >= 0 && HP <= this.getMaxHP() && isPrime(HP));
    }

    /**
     * Checks whether the HP of an entity is prime
     *
     * @param HP
     *      The HP of an entity
     *          | Entity.getHP()
     *
     * @return true if HP is prime, otherwise false
     *      | if HP == 1
     *      | then result == false
     *      | for each index until HP-1
     *      |   HP % index == 0
     */
    @Model
    private Boolean isPrime(Long HP) {
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
    public boolean isHealable() {
        return Healable;
    }

    /**
     * checks whether the given damagetypes are valid
     *
     * @param damageTypes
     *      the damagetypes that need to be checked
     *
     * @note we are sure the elements are unique because of the used datatype
     *
     * @return true if all damagetypes are different than Normal, if there is a Normal type it must be the only type
     *      | for each damagetype in damagetypes :
     *      |       if damagetype == NORMAL
     *      |       then result == damagetypes.size() == 1
     *      | result == !DamageTypes.isEmpty()
     *
     */
    public boolean areValidDamageTypes(HashSet<DamageType> damageTypes) {
        for (DamageType damageType : damageTypes) {
            if (Objects.requireNonNull(damageType) == DamageType.NORMAL) {
                return damageTypes.size() == 1;
            }
        }
        return !DamageTypes.isEmpty();
    }

    public boolean hasAsItem(Item item){
       for (AnchorPoint ap : this.AnchorPoints) {
           if (ap.getItem() != null){
               if (ap.getItem().equals(item)){
                   return true;
               }
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
     *      | result == AnchorPoints.contains(anchorPoint) //TODO: ask about formal
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
    public boolean isTerminated() {
        return Terminated;
    }

}
