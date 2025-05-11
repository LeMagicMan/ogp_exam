package com.RPG.Core;

import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidItemsException;
import com.RPG.Exception.InvalidValueException;

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

    private static final int defaultStrength = 50;

    private static final int defaultProtection = 10;

    /**********************************************************
     * Constructors
     *********************************************************/

    public Hero(String name, long maxHP, BigDecimal strength, ArrayList<Item> items) throws InvalidNameException, InvalidItemsException {
        super(name, maxHP, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND
        )));
        this.setStrength(strength.setScale(getStrengthScale(), getRoundingMode()));
        this.setProtection(defaultProtection);
        this.setSkinType(SkinType.NORMAL);
        this.setDamageTypes(new HashSet<>(List.of(DamageType.CLAWS)));
        this.setCapacity();
        this.equipStarterItems(items);
    }

    public Hero(String name) throws InvalidNameException, InvalidItemsException, InvalidValueException, InvalidHolderException {
        // Use a fixed value or another way to pass strength
        this(name, 997L, BigDecimal.valueOf(defaultStrength), createDefaultItems());
    }

    private static ArrayList<Item> createDefaultItems() throws InvalidValueException, InvalidHolderException {
        // Create and return the default items for the Hero
        ArrayList<Item> defaultItems = new ArrayList<>();
        defaultItems.add(new Weapon(null, AnchorPoint.RIGHTHAND));
        return defaultItems;
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/

    public boolean hasItemAt(AnchorPoint anchorPoint){
        return anchorPoint.hasItem();
    }

    private void equipStarterItems(ArrayList<Item> items) throws InvalidItemsException {
        if (items == null || items.isEmpty()) return;

        Backpack backpack = findBackpack(items);
        ArrayList<Item> sortedItems = sortItemsByWeight(items, backpack);

        ArrayList<AnchorPoint> freePoints = getFreeAnchorPoints();
        ArrayList<Item> toEquip = new ArrayList<>();
        ArrayList<Item> toStore = new ArrayList<>();

        divideItemsBetweenEquipAndStore(sortedItems, freePoints, toEquip, toStore);

        validateTotalWeight(items);
        validateBackpackStorage(backpack, toStore);

        if (backpack != null) equipBackpackFirst(backpack);
        equipItems(toEquip);
        storeRemainingItemsInBackpack(backpack, toStore);
    }

    private Backpack findBackpack(List<Item> items) {
        for (Item item : items) {
            if (item.getItemType() == ItemType.BACKPACK) {
                return (Backpack) item;
            }
        }
        return null;
    }

    private ArrayList<Item> sortItemsByWeight(List<Item> items, Backpack backpack) {
        ArrayList<Item> sorted = new ArrayList<>(items);
        if (backpack != null) sorted.remove(backpack);
        sorted.sort((a, b) -> Float.compare((float) b.getWeight(), (float) a.getWeight()));
        return sorted;
    }

    private ArrayList<AnchorPoint> getFreeAnchorPoints() {
        ArrayList<AnchorPoint> free = new ArrayList<>();
        for (int i = 0; i < getAmountOfAnchorPoints(); i++) {
            AnchorPoint ap = getAnchorPointAt(i);
            if (ap.getItem() == null) free.add(ap);
        }
        return free;
    }

    private void divideItemsBetweenEquipAndStore(List<Item> sortedItems, List<AnchorPoint> freePoints,
                                                 List<Item> toEquip, List<Item> toStore) {
        for (Item item : sortedItems) {
            boolean matched = false;
            for (AnchorPoint ap : new ArrayList<>(freePoints)) {
                if (ap.getAllowedItemType() == item.getItemType()) {
                    toEquip.add(item);
                    freePoints.remove(ap);
                    matched = true;
                    break;
                }
            }
            if (!matched) toStore.add(item);
        }
    }

    private void validateTotalWeight(List<Item> items) throws InvalidItemsException {
        long totalWeight = (long) items.stream().mapToDouble(Item::getWeight).sum();
        if (totalWeight > this.getCapacity()) {
            throw new InvalidItemsException("Total weight exceeds hero capacity.");
        }
    }

    private void validateBackpackStorage(Backpack backpack, List<Item> toStore) throws InvalidItemsException {
        if (!toStore.isEmpty()) {
            if (backpack == null) {
                throw new InvalidItemsException("No backpack to store remaining items.");
            }
            long storeWeight = (long) toStore.stream().mapToDouble(Item::getWeight).sum();
            if (!backpack.canStoreAll(toStore, storeWeight)) {
                throw new InvalidItemsException("Backpack cannot store all remaining items.");
            }
        }
    }

    private void equipBackpackFirst(Backpack backpack) {
        this.equip(AnchorPoint.BACK, backpack);
    }

    private void equipItems(List<Item> toEquip) {
        for (Item item : toEquip) {
            for (int i = 0; i < getAmountOfAnchorPoints(); i++) {
                AnchorPoint ap = getAnchorPointAt(i);
                if (ap.getItem() == null && ap.getAllowedItemType() == item.getItemType()) {
                    this.equip(ap, item);
                    break;
                }
            }
        }
    }

    private void storeRemainingItemsInBackpack(Backpack backpack, List<Item> toStore) {
        if (backpack != null) {
            for (Item item : toStore) {
                backpack.storeItem(item);
            }
        }
    }



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
