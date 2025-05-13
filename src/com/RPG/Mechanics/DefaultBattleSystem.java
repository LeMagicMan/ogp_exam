package com.RPG.Mechanics;

import com.RPG.Core.Entity;
import com.RPG.Core.Hero;
import com.RPG.Core.Item;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class representing a normal battle between entity's
 */
public class DefaultBattleSystem {

    /**
     * Executes a hit action from an attacking entity to a target entity.
     *
     * @effect This method performs a hit roll using a random value between 0 and 100,
     * then adjusts the roll .
     * If the adjusted roll is greater than or equal to the target's defense, the attack hits.
     *      | if adjustedRoll >= target.getDefense()
     *      |   target.reduceHP(Math.max(0, attacker.getBaseDamage()))
     *
     * @effect On a successful hit, damage is calculated from the attacker's base damage and applied
     * to the target. If the damage is equal to or greater than the target's current HP,
     * the target is considered slain. In that case:
     *      1. Loot is transferred from the target to the attacker using TreasureManager
     *              | target.Loot()
     *      2. The target is marked as killed via
     *              | target.kill()
     *      3. The attacker is healed using a HealingSystem
     *              | attacker.heal()
     *      4. The attacker's HP is normalized
     *              | attacker.normaliseHP()
     *
     * @effect The chosenItems list determines what items are looted from the target on a killing blow.
     *      | loot(target, attacker, chosenItems)
     *
     * @param attacker
     *      the entity initiating the hit
     *
     * @param target
     *      the entity being attacked
     *
     * @param chosenItems
     *      a list of items to be looted if the target is killed
     */
    private void executeHit(Entity attacker, Entity target, ArrayList<Item> chosenItems) {
        if (attacker == null || target == null || attacker.isTerminated() || target.isTerminated()) return;
        Random random = new Random();

        int roll = random.nextInt(101);

        int adjustedRoll = attacker.getAdjustedRoll(roll);

        if (adjustedRoll >= target.getDefense()) {
            long damage = Math.max(0, attacker.getBaseDamage());

            boolean killingBlow = damage >= target.getHP();

            target.reduceHP(damage);

            if (killingBlow) {
                TreasureManager.loot(target, attacker, chosenItems);
                target.kill();
                HealingSystem healingSystem = new HealingSystem();
                healingSystem.heal(attacker);
                attacker.normaliseHP();
            }
        }
    }

    /**
     * Simulates a battle between two entities, alternating turns until one is defeated.
     *
     * @effect Each entity takes turns executing a hit on the other. The battle continues
     * until one of the entities has 0 or less HP, at which point the loop ends.
     *      | while (!hero.isTerminated() && !monster.isTerminated())
     *      |       executeHit()
     *
     * @param hero
     *      the first combatant
     *
     * @param monster
     *      the second combatant
     *
     * @param chosenItems
     *      the list of items that may be looted if a killing blow occurs
     */
    public void battle(Hero hero, Entity monster, ArrayList<Item> chosenItems) {
        boolean heroTurn = true; //TODO: add initiator

        while (!hero.isTerminated() && !monster.isTerminated()) {
            if (heroTurn) {
                executeHit(hero, monster, chosenItems);
            } else {
                executeHit(monster, hero, chosenItems);
            }

            heroTurn = !heroTurn;
        }
    }


}
