package com.RPG.Mechanics;

import be.kuleuven.cs.som.annotate.Model;
import com.RPG.Core.Entity;

import java.util.Random;

/**
 * A class representing how entity's heal
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class HealingSystem {

    /**
     * Heals an entity
     *
     * @param entity
     *      entity we want to heal
     *
     * @post Entity can only heal if it is healable
     *      | entity.canHeal
     */
    public void heal(Entity entity) {
        if (entity.isHealable()) {
            long healingAmount = calculateHealingAmount(entity);
            entity.increaseHP(healingAmount);
        }
    }

    /**
     * calculate how much to heal an entity
     *
     * @param entity
     *      entity we want to heal
     *
     * @return a percentage healing amount between 0 and the full difference between max HP and current HP
     *      | 0 <= healingAmount <= maxHP - currentHP
     */
    @Model
    private long calculateHealingAmount(Entity entity) {
        Random random = new Random();
        int percentage = random.nextInt(101);

        long healingDifference = entity.getMaxHP() - entity.getHP();

        long healingAmount = (healingDifference * percentage) / 100;

        return Math.max(0, healingAmount);
    }
}

