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

    /**
     * A map containing all not null ItemFactories needed for a monster
     */
    private final Map<ItemType, MonsterLootFactory> factories = Map.of(
            ItemType.WEAPON, new WeaponFactory(),
            ItemType.BACKPACK, new BackpackFactory()
    );

    /**
     * A variable representing whether an entity can heal
     */
    private static final boolean Healable = false;

    /**
     * A variable representing whether an entity is intelligent
     */
    private static final boolean Intelligent = false;

    /**
     * A variable representing the capacity a monster gets per anchorpoint
     */
    private static final int capacityPerAnchorPoint = 31;

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
        this.setCapacity();
        createLoot();
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
     * getter for the nameRegex of an entity
     *
     * @return the nameRegex
     * | this.nameRegex
     */
    @Override
    public String getNameRegex() {
        return nameRegex;
    }

    /**
     * getter for the adjusted roll of a monster, If the roll is less than the current HP, the roll is returned unchanged.
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
        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++) {
            capacity += capacityPerAnchorPoint;
        }
        return capacity;
    }

    /**
     * Checks whether the given name is a valid name
     *
     * @param name
     *      The name to be checked
     *
     * @return True if the name is valid, not empty and follows the nameRegex, false otherwise
     *      | result == (name != null) && name.matches(nameRegex)
     */
    @Override
    public boolean isValidName(String name) {
        return super.isValidName(name);
    }

    /**
     * checks whether an entity can heal
     *
     * @return true if an entity can heal, otherwise false
     *      | Entity.Healable
     */
    @Override
    public boolean isHealable() {
        return Healable;
    }

    /**
     * getter for the intelligent of an entity
     *
     * @return true if is Intelligent, false otherwise
     *      | this.Intelligent
     */
    @Override
    public boolean isIntelligent() {
        return Intelligent;
    }

    /**
     * Attempts to create loot items for each anchor point based on a random spawn chance.
     *
     * @effect  For each anchor point on the object, this method checks if a random number is less than
     * the itemSpawnChance. If so, it attempts to create an item of the allowed type
     * for that anchor point using the appropriate MonsterLootFactory.
     *      | if (Math.random() < itemSpawnChance)
     *      | then MonsterLootFactory factory = factories.get(anchorpoint.getAllowedItemType());
     *
     * @effect If the anchor point allows any item type ItemType.ANY, a specific type is
     * chosen randomly from a fixed set: WEAPON, ARMOR, MONEY_POUCH,
     * and BACKPACK.
     *      | if(anchorpoint.getAllowedItemType() == ItemType.ANY)
     *      |       ItemType[] types = {ItemType.WEAPON, ItemType.ARMOR, ItemType.MONEY_POUCH, ItemType.BACKPACK}
     *      |       type = types[(int) (Math.random() * types.length)]
     *
     * @effect If a matching loot factory exists for the chosen item type, it is used to create the item
     * and associate it with the current object and anchor point. If the creation throws
     * InvalidValueException or InvalidHolderException, the method fails an assertion,
     * as those exceptions are considered unexpected.
     *      | Item item = factory.createItem(this, anchorpoint);
     *
     */
    private void createLoot(){
        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++) {
            if (Math.random() < itemSpawnChance) {
                AnchorPoint anchorpoint = this.getAnchorPointAt(index);
                ItemType type = anchorpoint.getAllowedItemType();

                switch (type) {
                    case ANY -> {
                        ItemType[] types = {ItemType.WEAPON, ItemType.ARMOR, ItemType.MONEY_POUCH, ItemType.BACKPACK};
                        type = types[(int) (Math.random() * types.length)];
                    }
                }

                MonsterLootFactory factory = factories.get(type);

                if (factory != null) {
                    try {
                        Item item = factory.createItem(this, anchorpoint);
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
    public boolean areValidDamageTypes(HashSet<DamageType> damageTypes){
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

    /**
     * a checker that checks if a monster has proper anchorpoints
     *
     * @return true
     */
    @Override
    public boolean hasProperAnchorpoints() {
        return true;
    }
}

