package com.RPG.Core;

import com.RPG.Exception.InvalidDamageTypesException;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidSkinTypeException;
import com.RPG.Exception.InvalidValueException;

import javax.naming.InvalidNameException;
import java.util.*;

/**
 * A class representing a Monster Entity
 *
 * @invar monster must have valid DamageTypes
 *      | hasValidDamageTypes()
 *
 * @invar monster must have valid SkinType
 *      | isValidSkinType()
 */
public class Monster extends Entity {
    /**********************************************************
     * Variables
     **********************************************************/

    /**
     * A variable representing the spawn chance of an item on an anchorpoint
     */
    private static final float itemSpawnChance = 0.20F;

    /**
     * A regex that the name of an entity needs to follow
     */
    private static final String nameRegex = "^[A-Z][a-zA-Z ’:]*$";

    //TODO
    private final Map<ItemType, MonsterLootFactory> factories = Map.of(
            ItemType.WEAPON, new WeaponFactory(),
            ItemType.BACKPACK, new BackpackFactory()
    );

    /**********************************************************
     * Constructors
     *********************************************************/

    /**
     * A constructor for a monster with a given name, maxHP, anchorpoint, damageTypes, SkinType
     *
     * @param name
     *      name of the monster
     *
     * @param maxHP
     *      maxHP of the monster
     *
     * @param anchorPoints
     *      anchorpoints of the monster
     *
     * @param damageTypes
     *      damagetypes of the monster
     *
     * @param skintype
     *      skintype of a monster
     */
    public Monster(String name, Long maxHP, ArrayList<AnchorPoint> anchorPoints, HashSet<DamageType> damageTypes, SkinType skintype) throws InvalidNameException, InvalidDamageTypesException, InvalidSkinTypeException {
        super(name, maxHP, anchorPoints, skintype, damageTypes);
        this.setDamageTypes(damageTypes);
        this.setSkinType(skintype);
        createLoot();
        this.setCapacity();
    }

    /**
     * A constructor for a monster with a given name
     *
     * @param name
     *      name of the monster
     *
     */
    public Monster(String name) throws InvalidNameException, InvalidDamageTypesException, InvalidSkinTypeException {
        this(name, 997L, new ArrayList<>(Arrays.asList(
                AnchorPoint.BELT,
                AnchorPoint.BACK,
                AnchorPoint.BODY,
                AnchorPoint.LEFTHAND,
                AnchorPoint.RIGHTHAND
        )), new HashSet<>(List.of(DamageType.CLAWS)), SkinType.THICK );
    }

    /**********************************************************
     * Getters and Setters
     **********************************************************/

    /**
     * getter for the adjusted roll of a monster
     *
     * @effect If the roll is less than the current HP, the roll is returned unchanged.
     * Otherwise, the method returns the HP value, effectively capping the roll at the current HP.
     *
     * @param roll
     *      roll we need to adjust
     *
     * @return the adjusted roll, which is the smaller of roll or the object's current HP
     *      | result = (roll < this.getHP()) ? roll : (int) this.getHP()
     */
    @Override
    public int getAdjustedRoll(int roll) {
        return (roll < this.getHP()) ? roll : (int) this.getHP();
    }

    /**********************************************************
     * Methods
     **********************************************************/

    /**
     * a method to calculate the capacity of a Hero
     *
     * @return capacity of that hero
     *      | for (index < this.getAmountOfAnchorPoints(); index++)
     *      |   if (this.getAnchorPointAt(index).hasItem())
     *      |       capacity += this.getAnchorPointAt(index).getItem().getWeight()
     *      | result = capacity
     */
    @Override
    protected long calculateCapacity() {
        long capacity = 0L;
        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++){
            if (this.getAnchorPointAt(index).hasItem()){
                capacity += (long) this.getAnchorPointAt(index).getItem().getWeight();
            }
        }
        return capacity;
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



    /**
     * Attempts to create loot items for each anchor point based on a random spawn chance.
     *
     * @effect  For each anchor point on the object, this method checks if a random number is less than
     * the itemSpawnChance. If so, it attempts to create an item of the allowed type
     * for that anchor point using the appropriate MonsterLootFactory.
     *
     * @effect If the anchor point allows any item type ItemType.ANY, a specific type is
     * chosen randomly from a fixed set: WEAPON, ARMOR, MONEY_POUCH,
     * and BACKPACK.
     *
     * @effect If a matching loot factory exists for the chosen item type, it is used to create the item
     * and associate it with the current object and anchor point. If the creation throws
     * InvalidValueException or InvalidHolderException, the method fails an assertion,
     * as those exceptions are considered unexpected.
     */
    private void createLoot(){ //TODO: desperately needs testing, and ask professor
        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++) {
            if (Math.random() < itemSpawnChance) {
                AnchorPoint anchor = this.getAnchorPointAt(index);
                ItemType type = anchor.getAllowedItemType();

                switch (type) {
                    case ANY -> {
                        ItemType[] types = {ItemType.WEAPON, ItemType.ARMOR, ItemType.MONEY_POUCH, ItemType.BACKPACK};
                        type = types[(int) (Math.random() * types.length)];
                    }
                }

                MonsterLootFactory factory = factories.get(type);

                if (factory != null) {
                    try {
                        Item item = factory.createItem(this, anchor);
                    } catch (InvalidValueException | InvalidHolderException e) {
                        assert false; //shouldn't happen
                    }
                }
            }
        }
    }

    /**
     * checks if a monster has valid DamageTypes
     *
     * @param damageTypes
     *      the damagetypes that need to be checked
     *
     * @return true if Damagetypes are all different from Normal, false otherwise
     *      | for each damagetype in damagetypes :
     *      |   if damagetype != NORMAL
     *      |       then result == true
     *      | result == false
     */
    @Override
    public boolean hasValidDamageTypes(HashSet<DamageType> damageTypes){
        if (damageTypes.size() != 1) return false;
        for (DamageType damageType : damageTypes){
            if (damageType == DamageType.NORMAL){
                return false;
            }
        }
        return true;
    }

    /**
     * checks if skintype of a monster is valid
     *
     * @param skinType
     *      SkinType we need to check
     *
     * @return true if different from Normal, false otherwise
     *      | result == (skinType != SkinType.NORMAL)
     */
    @Override
    public boolean isValidSkinType(SkinType skinType){
        return skinType != SkinType.NORMAL;
    }
}

