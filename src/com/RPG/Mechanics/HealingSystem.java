package com.RPG.Mechanics;

import com.RPG.Core.Entity;

import java.util.Random;

public class HealingSystem {

    public void heal(Entity entity) {
        if (entity.canHeal()) {
            long healingAmount = calculateHealingAmount(entity);
            entity.increaseHP(healingAmount);
        }
    }

    private long calculateHealingAmount(Entity entity) {
        Random random = new Random();
        int percentage = random.nextInt(101);

        long healingDifference = entity.getMaxHP() - entity.getHP();

        long healingAmount = (healingDifference * percentage) / 100;

        return Math.max(0, healingAmount);
    }
}

