package com.RPG.Core;

import com.RPG.Exception.InvalidHPException;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

    //TODO
    public Monster(String name) throws InvalidNameException, InvalidHPException { //TODO: use less specific one to make this
        super(name, 997L, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND //TODO: give each part a chance to get an item
        )));
        this.setDamageTypes(new HashSet<>(List.of(DamageType.CLAWS)));
        this.setSkinType(SkinType.THICK);
        this.setCapacity(); //TODO
    }

    //TODO
    public Monster(String name, ArrayList<AnchorPoint> anchorPoints, HashSet<DamageType> damageTypes, SkinType skinType) throws InvalidNameException, InvalidHPException { //TODO: add damageType
        super(name, 1000L, anchorPoints);
        this.setDamageTypes(damageTypes);
        this.setSkinType(skinType); //TODO: check for exceptions
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
