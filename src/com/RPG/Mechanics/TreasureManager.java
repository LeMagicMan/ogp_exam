package com.RPG.Mechanics;

import be.kuleuven.cs.som.annotate.Model;
import com.RPG.Core.AnchorPoint;
import com.RPG.Core.Entity;
import com.RPG.Core.Item;
import com.RPG.Core.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the looting an entity can do
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
     * @effect If an item cannot be equipped or stored, a
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

    @Model
    private static void lootShiny(Entity looter, Entity defeated, List<Item> defeatedItems) {
        defeatedItems.stream()
                .filter(item -> item != null && !item.isTerminated())
                .sorted((item1, item2) ->
                        Float.compare(item2.getShineLevel().getValueMultiplier(), item1.getShineLevel().getValueMultiplier())) // Sorting based on valueMultiplier
                .forEach(item -> {
                    AnchorPoint anchor = item.getHolder().getAnchorPointWithItem(item);
                    if (anchor != null) {
                        item.getHolder().unequip(anchor, item);
                    }

                    boolean equipped = tryEquipOrBackpack(looter, item);
                    if (!equipped) {
                        defeated.unequip(defeated.getAnchorPointWithItem(item), item);
                    }
                });
    }

    @Model
    private static boolean tryEquipOrBackpack(Entity entity, Item item) {
        AnchorPoint freeEquipSlot = findFreeAnchorPoint(entity, item);
        if (freeEquipSlot != null) {
            try {
                entity.equip(freeEquipSlot, item);
                return true;
            } catch (Exception ignored) {}
        }

        AnchorPoint backpack = findBackpack(entity);
        if (backpack != null && backpack.canAttach(item)) {
            try {
                entity.equip(backpack, item);
                return true;
            } catch (Exception ignored) {}
        }

        return false;
    }

    @Model
    private static AnchorPoint findFreeAnchorPoint(Entity entity, Item item) {
        for (int i = 0; i < entity.getAmountOfAnchorPoints(); i++) {
            AnchorPoint ap = entity.getAnchorPointAt(i);
            if (!ap.hasItem() && ap.canAttach(item) && !isBackpack(ap)) {
                return ap;
            }
        }
        return null;
    }

    @Model
    private static AnchorPoint findBackpack(Entity entity) {
        for (int i = 0; i < entity.getAmountOfAnchorPoints(); i++) {
            AnchorPoint ap = entity.getAnchorPointAt(i);
            if (isBackpack(ap)) {
                return ap;
            }
        }
        return null;
    }

    @Model
    private static boolean isBackpack(AnchorPoint ap) {
        return ap.getAllowedItemType() == ItemType.BACKPACK;
    }
}
