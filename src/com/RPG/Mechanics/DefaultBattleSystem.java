package com.RPG.Mechanics;

import com.RPG.Core.Entity;
import com.RPG.Core.Item;

import java.util.ArrayList;
import java.util.Random;

public class DefaultBattleSystem implements BattleSystem {

    @Override
    public void executeHit(Entity attacker, Entity target, ArrayList<Item> chosenItems) { //TODO: ask about overloading
        Random random = new Random();

        int roll = random.nextInt(101);

        int adjustedRoll = attacker.getAdjustedRoll(roll);

        if (adjustedRoll >= target.getDefense()) {
            long damage = Math.max(0, attacker.getBaseDamage());

            boolean killingBlow = damage >= target.getHP();

            target.reduceHP(damage);

            if (killingBlow) {
                if (attacker.isIntelligent()){
                    TreasureManager.loot(target, attacker, LootStrategy.INTELLIGENT, chosenItems);
                } else if (!attacker.isIntelligent()) {
                    TreasureManager.loot(target, attacker, LootStrategy.SHINE_BASED, chosenItems);
                }
                target.kill();
                HealingSystem healingSystem = new HealingSystem();
                healingSystem.heal(attacker);
                attacker.normaliseHP();
            }
        }
    }
}
