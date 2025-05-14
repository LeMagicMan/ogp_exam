import com.RPG.Core.*;
import com.RPG.Mechanics.TreasureManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TreasureManagerTest {

    private Entity intelligentHero;
    private Entity nonIntelligentMonster;
    private Weapon desiredWeapon;
    private Weapon shinyWeapon;
    private Weapon unwantedWeapon;
    private Backpack heroBackpack;

    @BeforeEach
    public void setUp() throws Exception {
        intelligentHero = new Hero("Hero");  // Hero is intelligent
        nonIntelligentMonster = new Monster("Monster");  // Monster is not intelligent
        heroBackpack = new Backpack(intelligentHero, AnchorPoint.BACK);

        // Setup items
        desiredWeapon = new Weapon(5, null, AnchorPoint.LEFTHAND, ShineLevel.LOW, 50);
        shinyWeapon = new Weapon(5, null, AnchorPoint.RIGHTHAND, ShineLevel.HIGH, 30);
        unwantedWeapon = new Weapon(5, null, AnchorPoint.LEFTHAND, ShineLevel.LOW, 40);

        // Set the hero's desired item
        ArrayList<Item> desiredItems = new ArrayList<>();
        desiredItems.add(desiredWeapon);

    }

    @Test
    public void intelligentEntityLootsDesiredItem() throws Exception {
        // Simulate the hero looting the items
        ArrayList<Item> desiredItems = new ArrayList<>();
        desiredItems.add(desiredWeapon);
        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desiredItems);

        // Assert that the intelligent hero looted the desired weapon
        assertEquals(intelligentHero, desiredWeapon.getHolder());
        assertTrue(heroBackpack.hasAsItem(desiredWeapon));
        assertFalse(heroBackpack.hasAsItem(shinyWeapon));
    }

    @Test
    public void nonIntelligentEntityLootsShinyItem() throws Exception {
        // Simulate the monster looting the shiny weapon (because it is not intelligent)
        ArrayList<Item> desiredItems = new ArrayList<>();
        desiredItems.add(desiredWeapon);  // Monster doesn't care about this
        TreasureManager.loot(intelligentHero, nonIntelligentMonster, desiredItems);

        // Assert that the non-intelligent monster looted the shiny weapon
        assertEquals(nonIntelligentMonster, shinyWeapon.getHolder());
        assertNull(desiredWeapon.getHolder());
    }

    @Test
    public void intelligentEntityIgnoresUnwantedItems() throws Exception {
        // Hero desires the desiredWeapon but there is also an unwantedWeapon
        ArrayList<Item> desiredItems = new ArrayList<>();
        desiredItems.add(desiredWeapon);
        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desiredItems);

        // Assert that the intelligent hero ignores the unwanted weapon
        assertNull(unwantedWeapon.getHolder());
        assertFalse(heroBackpack.hasAsItem(unwantedWeapon));
    }

    @Test
    public void intelligentEntityCannotLootIfBackpackFull() throws Exception {
        // Fill up the backpack with items
        for (int i = 0; i < 20; i++) {
            Weapon filler = new Weapon(1, intelligentHero, AnchorPoint.LEFTHAND, ShineLevel.LOW, 1);
            heroBackpack.storeItem(filler);
        }

        // Try to add the desired weapon when the backpack is full
        ArrayList<Item> desiredItems = new ArrayList<>();
        desiredItems.add(desiredWeapon);
        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desiredItems);

        // Assert that the desired weapon could not be looted since the backpack is full
        assertNull(desiredWeapon.getHolder());
        assertFalse(heroBackpack.hasAsItem(desiredWeapon));
    }

    @Test
    public void emptyLootListDoesNothing() throws Exception {
        // Simulate loot attempt with an empty desired list
        ArrayList<Item> emptyDesiredItems = new ArrayList<>();
        TreasureManager.loot(nonIntelligentMonster, intelligentHero, emptyDesiredItems);

        // Assert that the backpack has no items (nothing was looted)
        assertEquals(0, heroBackpack.getAmountOfItems());
    }
}
