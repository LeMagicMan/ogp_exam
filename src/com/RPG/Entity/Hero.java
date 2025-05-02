package com.RPG.Entity;

import com.RPG.Exception.InvalidHPException;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Arrays;

public class Hero extends Entity {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A regex that the name of an entity needs to follow
     */
    private static final String nameRegex = "^[A-Z][a-zA-Z ’:]*$";

    /**
     * A variable representing whether an entity can heal
     */
    private Boolean Healable = true;



    /**********************************************************
     * Constructors
     *********************************************************/

    public Hero(String name) throws InvalidNameException, InvalidHPException {
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
     * @return True if the name is valid, not empty, follows the nameRegex, has a maximum of 2 apostrophes and after a ':' there is always a ' ', false otherwise
     *      | result == (name != null) && name.matches(nameRegex)
     *      | && (name.chars().filter(c -> c == '’').count() > 2)
     *      | && !(name.charAt(index) == ':' && name.charAt(index + 1) != ' ') //TODO: ask about doc
     */
    @Override
    public Boolean isValidName(String name) {

        long Apostrof = name.chars().filter(c -> c == '’').count();
        if (Apostrof > 2) return false;

        for (int index = 0; index < name.length() - 1; index++) {
            if (name.charAt(index) == ':' && name.charAt(index + 1) != ' ') return false;
        }

        return super.isValidName(name);
    }
}
