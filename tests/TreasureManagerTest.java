import com.RPG.Core.*;
import com.RPG.Exception.*;
import com.RPG.Mechanics.TreasureManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreasureManagerTest {

    private Hero hero;
    private Monster monster;
    private ArrayList<Item> desiredItems;
    private Item shinyItem;
    private Item basicItem;
    private Item nullItem;

    Weapon weapon3;
    Weapon weapon2;
    Backpack backpack;

    @BeforeEach
    void setUp() throws InvalidValueException, InvalidHolderException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException {
        hero = new Hero("Gandalf");
        monster = new Monster("Sauron");

        backpack = new Backpack(50, 50, 50, hero, AnchorPoint.BACK, ShineLevel.MEDIUM);
        Weapon weapon = new Weapon(hero, AnchorPoint.RIGHTHAND);
        Weapon weapon1 = new Weapon(hero, AnchorPoint.LEFTHAND);
        Weapon shinyWeapon = new Weapon(10,  null, null, ShineLevel.HIGH, 20);
        Weapon shinyWeapon1 = new Weapon(20,  null, null, ShineLevel.LEGENDARY, 20);
        backpack.storeItem(shinyWeapon);
        backpack.storeItem(shinyWeapon1);


        // Create a list of desired items for looting
        desiredItems = new ArrayList<>();
        desiredItems.add(weapon3);
        desiredItems.add(weapon2);
    }

    @Test
    void testLootWhenEntitiesAreNull() {
        // Test when either the defeated or looter is null
        TreasureManager.loot(null, hero, desiredItems);
        TreasureManager.loot(monster, null, desiredItems);

        // No actual changes expected
        assertTrue(hero.getAllItems().isEmpty());
    }

    @Test
    void testLootWhenEntitiesAreTerminated() {

        hero.kill();
        TreasureManager.loot(monster, hero, desiredItems);  // Should not loot
        assertTrue(hero.getAllItems().isEmpty());

        monster.kill();  // Mark defeated entity as terminated
        TreasureManager.loot(hero, monster, desiredItems);  // Should not loot
        assertTrue(hero.getAllItems().isEmpty());
    }

    @Test
    void testLootIntelligentlyWithDesiredItems() {
        TreasureManager.loot(monster, hero, desiredItems);

        // Assert that looter has acquired both items
        assertTrue(hero.getAllItems().contains(shinyItem), "Looter should have looted the shiny item.");
        assertTrue(hero.getAllItems().contains(basicItem), "Looter should have looted the basic item.");
    }

    @Test
    void testLootNonIntelligentlyWithShinyItems() {
        TreasureManager.loot(hero, monster, desiredItems);

        // Assert that looter should have looted the shiny item first
        assertTrue(monster.getAllItems().contains(shinyItem), "Looter should loot shiny items first.");
        assertTrue(monster.getAllItems().contains(basicItem), "Looter should also loot the basic item.");
    }

    @Test
    void testLootWhenItemsAreNull() {
        // Test when some of the items in the list are null
        desiredItems.add(nullItem);  // Adding a null item to the desired list
        TreasureManager.loot(monster, hero, desiredItems);

        // Assert that the null item should not be looted
        assertFalse(hero.getAllItems().contains(nullItem), "Null items should not be looted.");
    }


    @Test
    void testLootWithEmptyDesiredItems() {
        // Test when the desired items list is empty
        ArrayList<Item> emptyDesiredItems = new ArrayList<>();
        TreasureManager.loot(monster, hero, emptyDesiredItems);

        // Assert that looter should not have any items after looting
        assertTrue(hero.getAllItems().contains(weapon2) || hero.getAllItems().contains(weapon3)) ;
    }

    @Test
    void testLootWithoutDesiredItems() {
        ArrayList<Item> nonDesiredItems = new ArrayList<>();  // Looter will just take any items
        TreasureManager.loot(hero, monster, nonDesiredItems);

        // Assert that looter should have at least the shiny item
        assertTrue(monster.getAllItems().contains(shinyItem));
    }

    @Test
    void testEquipItem() {

        TreasureManager.loot(monster, hero, desiredItems);

        // Assert that looter equips the shiny item first
        assertTrue(hero.getAllItems().contains(weapon2));
    }


    @Test
    void testEquipItemWithFullBackpack() throws InvalidValueException, InvalidHolderException {
        Weapon weapon4 = new Weapon(null, null);
        Weapon weapon5 = new Weapon(null, null);

        backpack.storeItem(weapon4);
        backpack.storeItem(weapon5);

        TreasureManager.loot(monster, hero, desiredItems);

        // Assert that the looter should not equip any new items if the backpack is full
        assertFalse(hero.hasAsItem(weapon2));
    }
}
