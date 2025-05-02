package com.RPG.Entity;

import com.RPG.Exception.InvalidHPException;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Arrays;

public class Monster extends Entity {
    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A regex that the name of an entity needs to follow
     */
    private static final String nameRegex = "^[A-Z][a-zA-Z ’:]*$";

    /**********************************************************
     * Constructors
     *********************************************************/

    public Monster(String name) throws InvalidNameException, InvalidHPException {
        super(name, 1000L, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND
        )));
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
     * @return True if the name is valid, not empty and follows the nameRegex, false otherwise //TODO: ask idf mentioning nameRegex is allowed
     *      | result == (name != null) && name.matches(nameRegex)
     */
    @Override
    public Boolean isValidName(String name) {
        return super.isValidName(name);
    }
}
