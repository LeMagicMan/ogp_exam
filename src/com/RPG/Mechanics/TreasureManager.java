package com.RPG.Mechanics;

import be.kuleuven.cs.som.annotate.Model;
import com.RPG.Core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the looting an entity can do
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class TreasureManager {

    /**
     * a method to loot an entity
     *
     * @param defeated
     *      the defeated entity
     *
     * @param looter
     *      the looting entity
     *
     * @param desired
     *      the desired items
     *
     * @effect If either entity is null or has been terminated, the method exits immediately.
     * Otherwise, the looter will attempt to loot the defeated entity's items.
     *      | if (defeated == null || looter == null || defeated.isTerminated() || looter.isTerminated())
     *      |   then return
     *      | else continue; //TODO
     *
     * @effect Looting behavior is based on the looter's intelligence:
     *      1. If the looter is intelligent
     *         it will loot selectively using the provided desired items.
     *              | lootIntelligently(Entity, Entity, ArrayList)
     *      2. If the looter is not intelligent, it will perform a simpler looting behavior
     *         taking items that appear shiny without consideration.
     *              | lootShiny(Entity, ArrayList)
     *
     */
    public static void loot(Entity defeated, Entity looter, ArrayList<Item> desired) {
        if (defeated == null || looter == null || defeated.isTerminated() || looter.isTerminated()) return;

        ArrayList<Item> defeatedItems = defeated.getAllItems();

        if (looter.isIntelligent()){
            lootIntelligently(looter, defeated, desired);
        } else if (!looter.isIntelligent()) {
            lootShiny(looter,defeated, defeatedItems);
        }
    }

    /**
     * Allows an intelligent looter entity to selectively loot desired items from a defeated entity.
     *
     * @effect The method iterates over a list of desired items and attempts to transfer each valid item
     * from the defeated entity to the looter. For each item:
     *   1. If the item is null, terminated, or not present in the defeated entity's inventory, it is skipped.
     *   2. If the item is currently equipped by the defeated entity , it is unequipped before transfer.
     *   3.The looter then attempts to equip the item or store it in a backpack
     *          | tryEquipOrBackpack()
     *
     *
     * @effect If an item cannot be equipped or stored, unequip item from entity
     *      | defeated.unequip(defeated.getAnchorPointWithItem(item), item);
     *
     * @param looter
     *      the intelligent entity attempting to loot
     *
     * @param defeated
     *      the defeated entity from whom items are being looted
     *
     * @param desiredItems
     *      a list of items that the looter wishes to acquire
     */
    @Model
    private static void lootIntelligently(Entity looter, Entity defeated, ArrayList<Item> desiredItems) {
        if (desiredItems == null) return;

        for (Item item : desiredItems) {
            if (item == null || item.isTerminated()) continue;
            if (!defeated.getAllItems().contains(item)) continue;

            AnchorPoint anchor = defeated.getAnchorPointWithItem(item);
            if (anchor != null) {
                defeated.unequip(anchor, item);
            }

            boolean equipped = tryEquipOrBackpack(looter, item);
            if (!equipped) {
                defeated.unequip(defeated.getAnchorPointWithItem(item), item);
            }
        }
    }

    /**
     * Enables a non-intelligent looter entity to loot items from a defeated entity
     * based on how "shiny" or valuable the items appear.
     *
     * @effect This method filters out any null or terminated items from the defeated entity's inventory,
     * then sorts the remaining items in descending order of their ShineLevel's valueMultiplier.
     * The looter then attempts to equip or store the shiniest items first.
     *      | for index > defeatedItems.size(); index++
     *      |   if defeatedItems.get(index).isTerminated || defeatedItems.get(index) == null
     *      |       defeatedItems.remove(item)
     *      |   if(defeatedItems.get(index).getShineLevel().getValueMultiplier() < defeatedItems.get(index + 1).getShineLevel().getValueMultiplier())
     *      |       Item item1 = defeatedItems.get(index ).getShineLevel().getValueMultiplier()
     *      |       defeatedItems.get(index) = defeatedItems.get(index + 1)
     *      |       defeatedItems.get(index + 1) = item1
     *      |       index == 0
     *
     *  @effect For each valid item:
     *      1. If the item is currently equipped by the defeated entity, it is first unequipped from its anchor point.
     *          | if item.getHolder != null
     *          |       item.getHolder().unequip(item.getHolder().getAnchorPointWithItem(item), item)
     *
     *      2.The looter then tries to equip or store the item
     *          | looter.tryEquipOrBackpack().
     *
     *      3.If this fails, the defeated entity is asked again to unequip the item.
     *          | for each item in defeatedItems
     *          |   defeated.unequip(item.getAnchorpoint(), item)
     *
     * @note  This behavior models simple creatures or AI looters attracted to flashy, high-value items
     * without regard for utility or fit.
     *
     * @param looter
     *      the entity attempting to loot items
     *
     * @param defeated
     *      the entity that has been defeated
     *
     * @param defeatedItems
     *      the list of items available from the defeated entity (may include nulls or terminated items)
     */
    @Model
    private static void lootShiny(Entity looter, Entity defeated, List<Item> defeatedItems) {
        defeatedItems.stream()
                .filter(item -> item != null && !item.isTerminated())
                .sorted((item1, item2) ->
                        Float.compare(item2.getShineLevel().getValueMultiplier(), item1.getShineLevel().getValueMultiplier()))
                .forEach(item -> {
                    AnchorPoint anchor = item.getHolder().getAnchorPointWithItem(item);
                    if (anchor != null) {
                        defeated.unequip(anchor, item);
                    }

                    boolean equipped = tryEquipOrBackpack(looter, item);
                    if (!equipped) {
                        defeated.unequip(defeated.getAnchorPointWithItem(item), item);
                    }
                });
    }

    /**
     * Attempts to equip the given item to an available anchor point on the entity, or store it in a backpack if no
     * appropriate equipment slot is available.
     *
     * @effect The method first checks for a compatible, free anchor point where the item can be equipped.
     * If such a point is found, it attempts to equip the item and returns true on success.
     *      | if findFreeAnchorPoint(entity, item) != null
     *      |   entity.equip(findFreeAnchorPoint(entity, item), item)
     *      | result == true
     *
     *
     * @effect If no appropriate equipment slot is available or equipping fails, the method then searches for a backpack
     * on the entity. If one exists and can accept the item, it attempts to equip the item into the backpack.
     *      | if findBackpack(entity) != null && backpack.canAttach(item)
     *      |   entity.equip(findBackpack(entity), item)
     *      | result == true
     *
     *
     *
     * @return If both equipping and storing in a backpack fail, the method returns false.
     *      | result == false
     *
     * @param entity
     *      the entity that will attempt to equip or store the item
     *
     * @param item
     *      the item to equip or store
     */
    @Model
    private static boolean tryEquipOrBackpack(Entity entity, Item item) {
        AnchorPoint freeEquipSlot = findFreeAnchorPoint(entity, item);
        if (freeEquipSlot != null) {
                entity.equip(freeEquipSlot, item);
                return true;
        }

        Backpack back = findBackpack(entity);
        if (back != null) {
            back.storeItem(item);
            return true;
        }

        return false;
    }

    /**
     * Searches for a free anchor point on the given entity that can accept the specified item,
     * excluding anchor points that represent backpacks.
     *
     * @effect The method iterates through all anchor points on the entity and returns the first one that
     *      1. Is not currently occupied by an item
     *          | !ap.hasItem()
     *
     *      2. Can accept the given item based on type or compatibility
     *          | ap.canAttach(item)
     *
     *      3.Is not considered a backpack anchor point
     *          | !isBackpack(ap)
     *
     * @param entity
     *      the entity to search for a valid anchor point
     *
     * @param item
     *      the item that needs to be equipped
     *
     * @return the first suitable AnchorPoint that can hold the item, or null if none are found
     *
     */
    @Model
    private static AnchorPoint findFreeAnchorPoint(Entity entity, Item item) {
        for (int i = 0; i < entity.getAmountOfAnchorPoints(); i++) {
            AnchorPoint anchorPoint = entity.getAnchorPointAt(i);
            if (!entity.hasItemAt(anchorPoint) && anchorPoint.canAttach(item)) {
                return anchorPoint;
            }
        }
        return null;
    }

    /**
     * Searches for and returns the first anchor point on the given entity that is classified as a backpack.
     *
     * @effect The method iterates through all of the entity's anchor points and checks each one.
     * It returns the first anchor point that qualifies as a backpack.
     *      | for i = 0; i < entity.getAmountOfAnchorPoints(); i++
     *      |   if (isBackpack(entity.getAnchorPointAt(i)))
     *      |       result == entity.getAnchorPointAt(i)
     *
     *
     * @param entity
     *      the entity whose anchor points are being searched
     *
     * @return the first AnchorPoint identified as a backpack, or null if none are found
     */
    @Model
    private static Backpack findBackpack(Entity entity) {
        for (int i = 0; i < entity.getAmountOfAnchorPoints(); i++) {
            AnchorPoint anchorPoint = entity.getAnchorPointAt(i);
            if (hasBackpack(anchorPoint, entity)) {
                return (Backpack) entity.getItemAt(anchorPoint);
            }
        }
        return null;
    }

    /**
     * Determines whether the given anchor point is designated for a backpack.
     *
     * @param anchorPoint
     *      the anchor point to evaluate
     *
     * @return true if the anchor point is intended for backpacks; false otherwise
     *      | result == anchorPoint.getAllowedItemType() == ItemType.BACKPACK
     *
     */
    @Model
    private static boolean hasBackpack(AnchorPoint anchorPoint, Entity entity) {
        if (anchorPoint == null) return false;
        if (entity.getItemAt(anchorPoint) == null) return false;
        return entity.getItemAt(anchorPoint).getItemType() == ItemType.BACKPACK;
    }
}
