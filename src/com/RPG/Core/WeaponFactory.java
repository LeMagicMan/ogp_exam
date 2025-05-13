package com.RPG.Core;

import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

//TODO
public class WeaponFactory implements MonsterLootFactory{
    @Override
    public Item createItem(Monster owner, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        return new Weapon(owner, anchorPoint);
    }
}
