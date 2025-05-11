package com.RPG.Core;

import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

public class ArmorFactory implements MonsterLootFactory {
    @Override
    public Item createItem(Monster owner, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException {
        return null;
    }
}