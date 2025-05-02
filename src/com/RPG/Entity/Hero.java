package com.RPG.Entity;

import com.RPG.Exception.InvalidHPException;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing A Hero entity
 *
 * @invar A Hero must only have the anchorpoints Belt, Back, Body, LeftHand, RightHand
 *      | hasProperAnchorpoints() //TODO
 *
 * @invar A hero must have a correct StrengthScale
 *      | hasProperStrengthScale()
 *
 * @invar A hero must have a valid Protection
 *      | isValidProtection()
 */
public class Hero extends Entity {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A Variable representing the protection of a Hero
     */
    private int Protection = 0;

    /**
     * A Variable representing the strength of a Hero
     */
    private BigDecimal Strength = BigDecimal.valueOf(0);

    /**
     * A variable representing the precision of the strength variable
     */
    private static final int strengthScale = 2;

    /**
     * A variable representing the way to round the strength to the set strengthScale
     */
    private static final RoundingMode roundingMode = RoundingMode.HALF_UP;

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
        this.Strength = BigDecimal.valueOf(50).setScale(strengthScale, roundingMode);
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

    /**
     * A checker to see if the protection is valid
     *
     * @param Protection
     *      the protection that needs to be checked
     *
     * @return true if protection is valid, otherwise false
     *      | result == (Protection >= 0)
     */
    public boolean isValidProtection(int Protection){
        return Protection >= 0;
    }
}
