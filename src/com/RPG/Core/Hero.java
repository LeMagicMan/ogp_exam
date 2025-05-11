package com.RPG.Core;

import com.RPG.Exception.InvalidHPException;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;
import java.util.*;

/**
 * A class representing A Hero entity
 *
 * @invar A Hero must only have the anchorpoints Belt, Back, Body, LeftHand, RightHand
 *      | hasProperAnchorpoints()
 *
 * @invar A hero must have a valid Strength
 *      | isValidStrength()
 *
 * @invar A hero must have a correct StrengthScale
 *      | hasProperStrengthScale()
 *
 * @invar A hero must have a valid Protection
 *      | isValidProtection()
 *
 * @invar A hero's skintype must be Normal
 *      | isValidSkinType()
 */
public class Hero extends Entity {

    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A regex that the name of a hero needs to follow
     */
    private static final String nameRegex = "^[A-Z][a-zA-Z ’:]*$";

    /**
     * A variable representing whether a hero can heal
     */
    private Boolean Healable = true;

    private final Boolean Intelligent = true;

    /**
     * A variable representing a hero's capacity multiplier
     */
    private static final float capacityMultiplier = 20;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Hero(String name) throws InvalidNameException, InvalidHPException {
        super(name, 997L, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND
        )));
        this.setStrength(BigDecimal.valueOf(50).setScale(getStrengthScale(), getRoundingMode()));
        this.setProtection(10);
        this.setSkinType(SkinType.NORMAL);
        this.setDamageTypes(new HashSet<>(List.of(DamageType.CLAWS)));
        this.setCapacity();
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
        return this.getStrength().multiply(BigDecimal.valueOf(capacityMultiplier)).longValue(); //TODO: ask if this is what they meant
    }

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
     * A checker to see if the strength is valid
     *
     * @param strength
     *      the strength that needs to be checked
     *
     * @return true if strength is valid, otherwise false
     *      | result == (strength >= 0)
     */
    public boolean isValidStrength(BigDecimal strength) {
        return strength.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * checks if the skin of a Hero is Valid
     *
     * @param skinType
     *      The Skintype that needs to be checked
     *
     * @return true if skintype ios normal, otherwise false
     *      | result == skintype == NORMAL
     */
    public boolean isValidSkinType(SkinType skinType){
        return skinType == SkinType.NORMAL;
    }

    /**
     * a checker that checks if a hero has all the needed anchorpoints exactly once
     *
     * @return true if a hero has all the needed anchorpoints exactly once, false otherwise
     *      | TODO: ask about formal
     */
    public boolean hasProperAnchorpoints() {
        // Use a set to track seen anchorpoints
        Set<AnchorPoint> expected = EnumSet.of(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND
        );

        Set<AnchorPoint> found = EnumSet.noneOf(AnchorPoint.class);

        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++) {
            AnchorPoint ap = getAnchorPointAt(index);
            if (!expected.contains(ap) || !found.add(ap)) {
                return false;
            }
        }

        // Valid only if all expected anchorpoints were found
        return found.equals(expected);
    }
}
