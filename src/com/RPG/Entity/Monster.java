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
        super(name, 1000L, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND //TODO: give each part a chance to get an item
        )));
        DamageTypes = new ArrayList<>(List.of(DamageType.CLAWS));
    }

    //TODO
    public Monster(String name, ArrayList<AnchorPoint> anchorPoints) throws InvalidNameException, InvalidHPException { //TODO: add damageType
        super(name, 1000L, anchorPoints);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/

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
