package com.RPG.Core;

import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

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
    public Monster(String name) throws InvalidNameException, InvalidValueException, InvalidHolderException { //TODO: use less specific one to make this
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

    private void createLoot() throws InvalidValueException, InvalidHolderException {
        for (int index = 0; index < this.getAmountOfAnchorPoints(); index++) {
            if (Math.random() < itemSpawnChance) {
                switch (this.getAnchorPointAt(index).getAllowedItemType()){
                    case ARMOR, MONEY_POUCH -> {
                        return;
                    }
                    case ANY -> {
                        ItemType[] types = {ItemType.WEAPON, ItemType.ARMOR, ItemType.MONEY_POUCH, ItemType.BACKPACK};
                        ItemType randomType = types[(int) (Math.random() * types.length)];
                        switch (randomType) {
                            case WEAPON -> {
                                Weapon weapon = new Weapon(this, this.getAnchorPointAt(index));
                            }
                            case BACKPACK -> {
                                Backpack backpack = new Backpack(this, this.getAnchorPointAt(index));
                            }
                            case ARMOR, MONEY_POUCH -> {
                                return;
                            }
                        }
                    }
                    case BACKPACK -> {
                        Backpack backpack = new Backpack(this, this.getAnchorPointAt(index));
                    }
                    case WEAPON -> {
                        Weapon weapon = new Weapon(this, this.getAnchorPointAt(index));
                    }
                }
            }
        }
    }
}
