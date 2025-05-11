package com.RPG.Mechanics;

import com.RPG.Core.AnchorPoint;
import com.RPG.Core.Entity;
import com.RPG.Core.Item;
import com.RPG.Core.ItemType;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager {
    public static void loot(Entity defeated, Entity looter, LootStrategy mode, ArrayList<Item> desired) {
        if (defeated == null || looter == null || defeated.isTerminated() || looter.isTerminated()) return;

        ArrayList<Item> defeatedItems = defeated.getAllItems();

        switch (mode) {
            case INTELLIGENT -> lootIntelligently(looter, defeated, desired);
            case SHINE_BASED -> lootShiny(looter, defeatedItems);
        }
    }

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
                //TODO
            }
        }
    }

    private static void lootShiny(Entity looter, List<Item> defeatedItems) {
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
                       //TODO
                    }
                });
    }

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

    private static AnchorPoint findFreeAnchorPoint(Entity entity, Item item) {
        for (int i = 0; i < entity.getAmountOfAnchorPoints(); i++) {
            AnchorPoint ap = entity.getAnchorPointAt(i);
            if (!ap.hasItem() && ap.canAttach(item) && !isBackpack(ap)) {
                return ap;
            }
        }
        return null;
    }

    private static AnchorPoint findBackpack(Entity entity) {
        for (int i = 0; i < entity.getAmountOfAnchorPoints(); i++) {
            AnchorPoint ap = entity.getAnchorPointAt(i);
            if (isBackpack(ap)) {
                return ap;
            }
        }
        return null;
    }

    private static boolean isBackpack(AnchorPoint ap) {
        return ap.getAllowedItemType() == ItemType.BACKPACK;
    }
}
