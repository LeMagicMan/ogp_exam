package com.RPG.Mechanics;

import be.kuleuven.cs.som.annotate.Model;
import com.RPG.Core.Entity;

import java.util.Random;

/**
 * A class representing how entity's heal
 *
 * @invar entity can only heal if healable is true
 *      | isHealable()
 */
public class HealingSystem {

    /**
     * Heals an entity
     *
     * @param entity
     *      entity we want to heal
     */
    public void heal(Entity entity) {
        if (entity.canHeal()) {
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
     *      | //TODO
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

