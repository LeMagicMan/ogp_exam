package com.RPG.Mechanics;

import com.RPG.Core.Entity;


import java.util.Random;

public class DefaultBattleSystem implements BattleSystem {

    private HealingSystem healingSystem;


    public DefaultBattleSystem() {
        this.healingSystem = new HealingSystem();
    }

    @Override
    public void executeHit(Entity attacker, Entity target) {
        Random random = new Random();

        int roll = random.nextInt(101);

        int adjustedRoll = attacker.getAdjustedRoll(roll);

        if (adjustedRoll >= target.getDefense()) {
            long damage = Math.max(0, attacker.getBaseDamage());

            boolean killingBlow = damage >= target.getHP();

            target.reduceHP(damage);

            if (killingBlow) {
                attacker.loot();
                target.kill();
                healingSystem.heal(attacker);
                attacker.normaliseHP();
            }
        }
    }
}
