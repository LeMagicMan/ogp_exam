package com.RPG.Core;

import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

import javax.naming.InvalidNameException;
import java.util.*;

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

    /**********************************************************
     * Constructors
     *********************************************************/

    public Monster(String name, Long maxHP, ArrayList<AnchorPoint> anchorPoints, HashSet<DamageType> damageTypes, SkinType skintype) throws InvalidNameException, InvalidValueException, InvalidHolderException {
        super(name, maxHP, anchorPoints);
        this.setDamageTypes(damageTypes);
        this.setSkinType(skintype);
        createLoot();
        this.setCapacity();
    }

    //TODO
    public Monster(String name) throws InvalidNameException, InvalidValueException, InvalidHolderException {
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
     */
    @Override
    protected long calculateCapacity() {
        long capacity = 0L;
        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++){
            if (this.getAnchorPointAt(index).getItem() != null){
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

    private final Map<ItemType, MonsterLootFactory> factories = Map.of(
            ItemType.WEAPON, new WeaponFactory(),
            ItemType.BACKPACK, new BackpackFactory()
    );

    private void createLoot() throws InvalidValueException, InvalidHolderException { //TODO: desperately needs testing, and ask professor
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
                    Item item = factory.createItem(this, anchor);
                }
            }
        }
    }

}
