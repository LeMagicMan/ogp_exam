package com.RPG.Entity;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import javax.naming.InvalidNameException;
import java.util.ArrayList;

/**
 * A class representing all Entity-types
 *
 * @invar Every entity must have a valid name
 *      | isValidName(getName())
 *
 * @invar Every entity must have a valid amount of hitpoints
 *      | isValidHP(getHP)
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
        MaxHP = maxHP;
        HP = maxHP;
        AnchorPoints = Anchorpoints;
    }


    /**********************************************************
     * Getters and Setters
     **********************************************************/

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

    /**********************************************************
     * Methods
     **********************************************************/

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
     *      //TODO: ask prof for formal
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

}
