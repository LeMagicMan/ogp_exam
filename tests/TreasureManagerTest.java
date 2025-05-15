import com.RPG.Core.*;
import com.RPG.Exception.*;
import com.RPG.Mechanics.TreasureManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TreasureManagerTest {

    private Hero intelligentHero;
    private Monster nonIntelligentMonster;
    private Weapon dullWeapon;
    private Weapon shinyWeapon;
    private Backpack monsterBackpack;
    private Backpack heroBackpack;
    private Weapon heroWeapon;

    @BeforeEach
    void setUp() throws Exception {
        intelligentHero = new Hero("Hero");
        nonIntelligentMonster = new Monster("Monster");

        dullWeapon = new Weapon(2.0, nonIntelligentMonster, AnchorPoint.LEFTHAND, ShineLevel.LOW, 12);
        shinyWeapon = new Weapon(1.0, nonIntelligentMonster, AnchorPoint.RIGHTHAND, ShineLevel.HIGH, 8);
        monsterBackpack = new Backpack(3.0, 40, 10, nonIntelligentMonster, AnchorPoint.BACK, ShineLevel.MEDIUM);

        heroWeapon = new Weapon(2.0, intelligentHero, AnchorPoint.LEFTHAND, ShineLevel.LEGENDARY, 12);
        heroBackpack = new Backpack(2.5, 30, 10, intelligentHero, AnchorPoint.BACK, ShineLevel.LOW);
    }

    @Test
    void testLootWithNullEntitiesDoesNothing() {
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(dullWeapon);

        TreasureManager.loot(null, intelligentHero, desired);
        TreasureManager.loot(nonIntelligentMonster, null, desired);

        assertTrue(nonIntelligentMonster.hasAsItem(dullWeapon));
        assertTrue(intelligentHero.hasAsItem(heroWeapon));
    }

    @Test
    void testLootWithTerminatedEntitiesDoesNothing() throws InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidHolderException {
        Hero deadHero = new Hero("Peter");
        deadHero.kill();
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(dullWeapon);

        TreasureManager.loot(nonIntelligentMonster, deadHero, desired);
        assertTrue(nonIntelligentMonster.hasAsItem(dullWeapon));

        TreasureManager.loot(deadHero, intelligentHero, desired);
        assertTrue(nonIntelligentMonster.hasAsItem(dullWeapon));
    }

    @Test
    void testIntelligentHeroLootsDesiredItemsOnly() {
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(dullWeapon);

        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desired);

        assertTrue(intelligentHero.hasAsItem(dullWeapon));
        assertFalse(intelligentHero.hasAsItem(shinyWeapon));
        assertTrue(nonIntelligentMonster.hasAsItem(shinyWeapon));
    }

    @Test
    void testLootSkipsInvalidDesiredItems() throws InvalidValueException, InvalidHolderException {
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(null);
        Item fakeItem = new Weapon(1.0, null, null, ShineLevel.LOW, 3);
        desired.add(fakeItem);

        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desired);

        assertFalse(intelligentHero.hasAsItem(fakeItem));
        assertTrue(nonIntelligentMonster.hasAsItem(dullWeapon));
    }

    @Test
    void testNonIntelligentMonsterLootsShiniestItem() {
        ArrayList<Item> dummyList = nonIntelligentMonster.getAllItems();
        TreasureManager.loot(intelligentHero, nonIntelligentMonster, dummyList);

        assertTrue(nonIntelligentMonster.hasAsItem(heroWeapon));
    }

    @Test
    void testLootFallsBackToBackpack() throws Exception {
        Weapon blockingWeapon = new Weapon(1.0, intelligentHero, AnchorPoint.RIGHTHAND, ShineLevel.LOW, 5);
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(shinyWeapon);

        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desired);

        assertTrue(heroBackpack.hasAsItem(shinyWeapon));
    }

    @Test
    void testLootFailsIfNoSlotAndNoBackpack() throws InvalidValueException, InvalidHolderException {
        intelligentHero.unequip(AnchorPoint.BACK, heroBackpack);

        Weapon blockingWeapon = new Weapon(1.0, intelligentHero, AnchorPoint.BACK, ShineLevel.LOW, 5);
        Weapon blockingWeapon1 = new Weapon(1.0, intelligentHero, AnchorPoint.RIGHTHAND, ShineLevel.LOW, 5);
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(shinyWeapon);

        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desired);

        assertNull(shinyWeapon.getHolder());
    }

    @Test
    void testLootMultipleItemsWithMixedOutcomes() throws Exception {
        // Hero gets shiny weapon in slot, dull weapon into backpack
        ArrayList<Item> desired = new ArrayList<>();
        desired.add(shinyWeapon);
        desired.add(dullWeapon);

        TreasureManager.loot(nonIntelligentMonster, intelligentHero, desired);

        assertTrue(intelligentHero.hasAsItem(shinyWeapon));
        assertTrue(heroBackpack.hasAsItem(dullWeapon));
    }
}
