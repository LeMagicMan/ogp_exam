package com.RPG.Mechanics;

import com.RPG.Core.Entity;
import com.RPG.Core.Item;

import java.util.ArrayList;

/**
 * //TODO
 */
public interface BattleSystem {
    void executeHit(Entity attacker, Entity target, ArrayList<Item> chosenItems);
}
