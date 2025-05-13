package com.RPG.Core;

import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;

//TODO
public interface MonsterLootFactory {
    Item createItem(Monster owner, AnchorPoint anchorPoint) throws InvalidValueException, InvalidHolderException;
}
