package com.RPG.Mechanics;

import com.RPG.Core.Entity;
import com.RPG.Core.Item;
import com.RPG.Core.Monster;

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
    private void executeHit(Entity attacker, Entity target, ArrayList<Item> chosenItems, boolean response) {
        if (attacker == null || target == null || attacker.isTerminated() || target.isTerminated()) return;
        Random random = new Random();

        int roll = random.nextInt(101);
        if (response) {
            System.out.println(attacker.getName() + " rolled " + roll);
        }

        int adjustedRoll = attacker.getAdjustedRoll(roll);
        if (response) {
            System.out.println(attacker.getName() + "'s got adjusted to " + adjustedRoll);
        }

        if (adjustedRoll >= target.getDefense()) {
            if (response) {
                System.out.println(attacker.getName() + " hits " + target.getName());
            }

            long damage = Math.max(0, attacker.getBaseDamage());
            if (response) {
                System.out.println(attacker.getName() + " does " + damage + " damage to " + target.getName());
            }

            boolean killingBlow = damage >= target.getHP();

            target.reduceHP(damage);

            if (killingBlow) {
                if (response) {
                    System.out.println(attacker.getName() + " killed " + target.getName());
                    System.out.println(attacker.getName() + " starts looting, currently he has: " + attacker.getAllItems());
                }

                TreasureManager.loot(target, attacker, chosenItems);
                if (response) {
                    System.out.println(attacker.getName() + " stopped looting, currently he has: " + attacker.getAllItems());
                }

                target.kill();
                if (response && target.isTerminated()) {
                    System.out.println(target.getName() + " breathed his last ");
                }
                HealingSystem healingSystem = new HealingSystem();

                if (response && attacker.isHealable()) {
                    System.out.println(attacker.getName() + " started healing, he currently has " + attacker.getHP() + " HP" );
                }
                healingSystem.heal(attacker);
                if (response) {
                    System.out.println(attacker.getName() + " has recovered and ended the fight with " + attacker.getHP() + " HP" );
                }

                attacker.normaliseHP();
                if (response) {
                    System.out.println(attacker.getName() + " Hp got normalised to " + attacker.getHP());
                }
            }
        } else {
            if (response) {
                System.out.println(attacker.getName() + " missed " + target.getName());
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
     * @param entity1
     *      the first combatant
     *
     * @param entity2
     *      the second combatant
     *
     * @param chosenItems
     *      the list of items that may be looted if a killing blow occurs
     */
    private void battle(Entity entity1, Entity entity2, ArrayList<Item> chosenItems, Entity initiator, boolean response) {
        boolean heroTurn = (initiator == entity1);

        while (!entity1.isTerminated() && !entity2.isTerminated()) {
            if (heroTurn) {
                executeHit(entity1, entity2, chosenItems, response);
            } else {
                executeHit(entity2, entity1, chosenItems, response);
            }

            heroTurn = !heroTurn;
        }
    }

    /**
     *
     * @param attacker
     * @param monster
     * @param chosenItems
     * @param initiator
     */
    public void combat(Entity attacker, Monster monster, ArrayList<Item> chosenItems, Entity initiator, boolean response) {
        battle(attacker, monster, chosenItems, initiator, response);
    }
}
