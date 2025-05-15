package com.RPG.Core;

import com.RPG.Exception.*;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;
import java.util.*;

/**
 * A class representing A Hero entity
 *
 * @invar A Hero must only have the anchorpoints Belt, Back, Body, LeftHand, RightHand
 *      | hasProperAnchorpoints()
 *
 * @invar A hero's skintype must be Normal
 *      | isValidSkinType()
 *
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

    /**
     * A variable representing the Intelligence of a hero
     */
    private final Boolean Intelligent = true;

    /**
     * A variable representing a hero's capacity multiplier
     */
    private static final float capacityMultiplier = 5;

    /**
     * defaultStrength of a hero
     */
    private static final int defaultStrength = 50;

    /**
     * defaultProtection of a hero
     */
    private static final int defaultProtection = 10;

    /**********************************************************
     * Constructors
     *********************************************************/

    /**
     * A constructor for a hero with given name, maxHP, strength, items
     *
     * @param name
     *      name of the hero
     *
     * @param maxHP
     *      maxHP of the hero
     *
     * @param strength
     *      strength of the hero
     *
     * @param items
     *      items of the hero
     *
     * @pre Hero must be able to hold all items
     *      | validateTotalWeight()
     *
     * @pre Hero must have Valid Strength
     *      | isValidStrength()
     *
     * @throws InvalidItemsException
     *      gets thrown when hero cant hold all starterItems
     */
    public Hero(String name, long maxHP, BigDecimal strength, ArrayList<Item> items) throws InvalidNameException, InvalidItemsException, InvalidSkinTypeException, InvalidDamageTypesException {
        super(name, maxHP, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND
        )), SkinType.NORMAL, new HashSet<>(List.of(DamageType.NORMAL)));
        this.setStrength(strength.setScale(getStrengthScale(), getRoundingMode()));
        this.setProtection(defaultProtection);
        this.setCapacity();
        if (items == null || items.isEmpty()) {
            this.equipStarterItems(createDefaultItems());
        } else this.equipStarterItems(items);

    }

    /**
     * A constructor for a Hero with given Name
     *
     * @param name
     *      name of the hero
     */
    public Hero(String name) throws InvalidNameException, InvalidItemsException, InvalidValueException, InvalidHolderException, InvalidSkinTypeException, InvalidDamageTypesException {
        this(name, 997L, BigDecimal.valueOf(defaultStrength), null);
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * creates items for the most basic constructor of a hero
     *
     * @return the created items
     *    | result == new Weapon(null, AnchorPoint.RIGHTHAND)
     */
    private ArrayList<Item> createDefaultItems() {
        ArrayList<Item> defaultItems = new ArrayList<>();
        try {
            defaultItems.add(new Weapon(null, null));
        } catch (InvalidValueException | InvalidHolderException e) {
            assert false;
        }
        return defaultItems;
    }

    /**
     * a method to equip the heaviest starter item sto anchorpoints first and the rest to backpack if there is one
     *
     * @param items
     *      items we want to equip
     *
     * @throws InvalidItemsException
     *      If there is no backpack to store remaining items
     */
    private void equipStarterItems(ArrayList<Item> items) throws InvalidItemsException {
        if (items == null || items.isEmpty()) return;

        Backpack backpack = (Backpack) findBackpack(items);
        ArrayList<Item> sortedItems = sortItemsByWeight(items, backpack);

        ArrayList<AnchorPoint> freePoints = getFreeAnchorPoints();
        ArrayList<Item> toEquip = new ArrayList<>();
        ArrayList<Item> toStore = new ArrayList<>();

        tryToEquipItems(sortedItems, freePoints, toEquip, toStore);

        validateTotalWeight(items);

        if (backpack != null) {
            if (backpack.canStoreAll(toStore)) {
                equipBackpack(backpack);
            }
        } else if(!(toStore.isEmpty())){
            throw new InvalidItemsException("Too many items");
        }

        equipItems(toEquip);

        if (backpack != null) {
            storeRemainingItemsInBackpack(backpack, toStore);
        }
        items.clear();
    }

    /**
     * checks if there is a backpack in a list of items
     *
     * @param items
     *      items we want to check for a backpack
     *
     * @return the backpack itemn if found, null otherwise
     *      | for each item in item
     *      |   if (item.getItemType() == ItemType.BACKPACK)
     *      |       return Item
     *      | return null
     */
    private Item findBackpack(List<Item> items) {
        for (Item item : items) {
            if (item.getItemType() == ItemType.BACKPACK) {
                return item;
            }
        }
        return null;
    }

    /**
     * sorts items in a list by weight from heaviest to lightest, if backpack is not null, we remove the backpack from the sorted list
     *
     * @param items
     *      items we want to sort
     *
     * @param backpack
     *      backpack we need to remove from list
     *
     * @return the sorted list
     */
    private ArrayList<Item> sortItemsByWeight(List<Item> items, Backpack backpack) {
        ArrayList<Item> sorted = new ArrayList<>(items);
        if (backpack != null) sorted.remove(backpack);
        sorted.sort((a, b) -> Float.compare((float) b.getTotalWeight(), (float) a.getTotalWeight()));
        return sorted;
    }

    /**
     * checks if a hero has any free anchorpoints
     *
     * @return the free anchorpoints
     *      | for each anchorpoint in anchorpoints
     *      |   if !anchorpoint.hasItem
     *      |       free.add(anchorpoint)
     */
    private ArrayList<AnchorPoint> getFreeAnchorPoints() {
        ArrayList<AnchorPoint> free = new ArrayList<>();
        for (int i = 0; i < getAmountOfAnchorPoints(); i++) {
            AnchorPoint anchorpoint = getAnchorPointAt(i);
            if (!anchorpoint.hasItem()) free.add(anchorpoint);
        }
        return free;
    }

    /**
     * Attempts to equip a list of sorted items to available anchor points.
     *
     * @effect Each item is matched to the first suitable anchor point based on item type.
     * If a matching anchor point is found, the item is added to the toEquip list,
     * and the anchor point is removed from freePoints to prevent reuse.
     * Items that cannot be equipped due to lack of a suitable anchor point are added to toStore.
     *      | for each item in sortedItems
     *      |   for each anchorpoint in freePoints
     *      |       if (anchorpoint.getAllowedItemType() == ItemType.ANY || anchorpoint.getAllowedItemType() == item.getItemType())
     *      |           toEquip.add(item)
     *      |           freePoints.remove(anchorpoint)
     *      |           matched = true
     *      |   if !matched
     *      |       toStore.add(item)
     *
     * @param sortedItems
     *      the list of items to equip, sorted by priority or preference
     *
     * @param freePoints
     *      the list of available anchor points where items can be equipped
     *
     * @param toEquip
     *      an output list that will be populated with successfully equipped items
     *
     * @param toStore
     *      an output list that will be populated with items that could not be equipped
     */
    private void tryToEquipItems(ArrayList<Item> sortedItems, ArrayList<AnchorPoint> freePoints, ArrayList<Item> toEquip, ArrayList<Item> toStore) {
        for (Item item : sortedItems) {
            boolean matched = false;
            for (AnchorPoint anchorpoint : freePoints) {
                if (anchorpoint.getAllowedItemType() == ItemType.ANY || anchorpoint.getAllowedItemType() == item.getItemType()) {
                    toEquip.add(item);
                    freePoints.remove(anchorpoint);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                toStore.add(item);
            }
        }
    }

    /**
     * checks if all items fom a list can be added to a hero
     *
     * @param items
     *      items we want to add
     *
     * @throws InvalidItemsException
     *      if weight exceeds hero's capacity
     */
    private void validateTotalWeight(List<Item> items) throws InvalidItemsException {
        double totalWeight = items.stream().mapToDouble(Item::getWeight).sum();
        if (totalWeight > this.getCapacity()) {
            throw new InvalidItemsException("Total weight exceeds hero capacity.");
        }
    }

    /**
     * equips a backpack
     *
     * @param backpack
     *      backpack we want to equip
     */
    private void equipBackpack(Backpack backpack) {
        this.equip(AnchorPoint.BACK, backpack);
    }

    /**
     * equips a list of Items
     *
     * @pre hero must be able to hold Items
     *
     * @param toEquip
     *      items we want to equip
     *
     * @effect equips all items from the list
     *      | for each item in toEquip
     *      |   for each AnchorPoint in anchorpoints
     *      |       if (anchorpoint.getItem() == null && anchorpoint.getAllowedItemType() == item.getItemType())
     *      |           this.equip(anchorpoint, item)
     */
    private void equipItems(List<Item> toEquip) {
        for (Item item : toEquip) {
            for (int i = 0; i < getAmountOfAnchorPoints(); i++) {
                AnchorPoint anchorpoint = getAnchorPointAt(i);
                if(anchorpoint.getAllowedItemType() == ItemType.ANY || anchorpoint.getAllowedItemType() == item.getItemType()) {
                    if (anchorpoint.getItem() == null && anchorpoint.getAllowedItemType() == item.getItemType()) {
                        this.equip(anchorpoint, item);
                        break;
                    }
                }
            }
        }
    }

    /**
     * stores Items in backpack
     *
     * @pre backpack must be able to store Items
     *      | for each item in toStore
     *      |  totalWeight += item.getTotalWeight
     *      | result totalweight <= backpack.getCapacity()
     *
     * @param backpack
     *      backpack we want to store items in
     *
     * @param toStore
     *      items we want to store in backpack
     *
     * @post all items are stored in the given backpack
     *      | for each item in toStore
     *      |   backpack.storeItem(item)
     */
    private void storeRemainingItemsInBackpack(Backpack backpack, List<Item> toStore) {
        if (backpack != null) {
            for (Item item : toStore) {
                backpack.storeItem(item);
            }
        }
    }

    /**
     * getter for the nameRegex of a hero
     *
     * @return the nameRegex
     *      | this.nameRegex
     */
    @Override
    public String getNameRegex() {
        return nameRegex;
    }

    /**
     * getter for the intelligent of an entity
     *
     * @return true if is Intelligent, false otherwise
     * | this.Intelligent
     */
    @Override
    public boolean isIntelligent() {
        return Intelligent;
    }

    /**
     * a method to calculate the capacity of a Hero
     *
     * @return capacity of that hero
     *      | result == this.getStrength().multiply(BigDecimal.valueOf(capacityMultiplier)).longValue();
     */
    @Override
    protected long calculateCapacity() {
        return this.getStrength().multiply(BigDecimal.valueOf(capacityMultiplier)).longValue();
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
     *      | && !(name.charAt(index) == ':' && name.charAt(index + 1) != ' ')
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
     * checks whether an entity can heal
     *
     * @return true if an entity can heal, otherwise false
     * | Entity.Healable
     */
    @Override
    public boolean isHealable() {
        return Healable;
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
    @Override
    public boolean hasProperAnchorpoints() {
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

        return found.equals(expected);
    }

    /**
     * checks whether the given damagetypes are valid
     *
     * @param damageTypes
     *      the damagetypes that need to be checked
     *
     * @return true if all damagetypes are different than Normal, if there is a Normal type it must be the only type
     *      | for each damagetype in damagetypes :
     *      |       if damagetype == NORMAL
     *      |           then result == true
     *      | result == false
     *
     */
    public boolean hasValidDamageTypes(HashSet<DamageType> damageTypes) {
        if (damageTypes.size() != 1) return false;
        for (DamageType damageType : damageTypes) {
            if (Objects.requireNonNull(damageType) == DamageType.NORMAL) {
                return true;
            }
        }
        return false;
    }
}
