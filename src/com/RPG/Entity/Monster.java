package com.RPG.Entity;

import com.RPG.Exception.InvalidHPException;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Monster extends Entity {
    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A regex that the name of an entity needs to follow
     */
    private static final String nameRegex = "^[A-Z][a-zA-Z ’:]*$";

    /**
     * A variable representing the DamageType of a monster
     */
    private ArrayList<DamageType> DamageTypes = new ArrayList<>();;

    /**********************************************************
     * Constructors
     *********************************************************/

    //TODO
    public Monster(String name) throws InvalidNameException, InvalidHPException {
        super(name, 997L, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND //TODO: give each part a chance to get an item
        )));
        this.DamageTypes = new ArrayList<>(List.of(DamageType.CLAWS));
        this.setSkinType(SkinType.THICK);
        this.setCapacity(); //TODO
    }

    //TODO
    public Monster(String name, ArrayList<AnchorPoint> anchorPoints, ArrayList<DamageType> damageTypes, SkinType skinType) throws InvalidNameException, InvalidHPException { //TODO: add damageType
        super(name, 1000L, anchorPoints);
        this.DamageTypes = damageTypes;
        this.setSkinType(skinType); //check for exceptions
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * a method to calculate the capacity of a Hero
     *
     * @return capacity of that hero
     */
    @Override
    protected long calculateCapacity() {
        return 0L; //TODO implement this when item works
    }

    /**
     * Checks whether the given name is a valid name
     *
     * @param name
     *      The name to be checked
     *
     * @return True if the name is valid, not empty and follows the nameRegex, false otherwise //TODO: ask idk mentioning nameRegex is allowed
     *      | result == (name != null) && name.matches(nameRegex)
     */
    @Override
    public Boolean isValidName(String name) {
        return super.isValidName(name);
    }
}
