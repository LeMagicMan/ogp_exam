import com.RPG.Core.*;
import com.RPG.Exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private Hero hero;
    private Monster monster;

    @BeforeEach
    public void setUp() throws Exception {
        hero = new Hero("Artemis");
        monster = new Monster("Gorgon");
    }

    @Test
    public void testHeroInitialization() {
        assertEquals("Artemis", hero.getName());
        assertEquals(997L, hero.getMaxHP());
        assertEquals(997L, hero.getHP());
        assertNotNull(hero.getSkinType());
        assertTrue(hero.getAmountOfDamageTypes() > 0);
        assertTrue(hero.getBaseDamage() > 0);
    }

    @Test
    public void testMonsterInitialization() {
        assertEquals("Gorgon", monster.getName());
        assertEquals(997L, monster.getMaxHP());
        assertEquals(997L, monster.getHP());
        assertEquals(5, monster.getAmountOfAnchorPoints());
        assertEquals(SkinType.THICK, monster.getSkinType());
        assertEquals(1, monster.getAmountOfDamageTypes());
        assertTrue(monster.hasDamageType(DamageType.CLAWS));
    }

    @Test
    public void testReduceHPWithinBounds(){
        hero.reduceHP(300L);
        assertEquals(697L, hero.getHP());

        monster.reduceHP(997L);
        assertEquals(0L, monster.getHP());
    }

    @Test
    public void testReduceHPWithNegativeValueDoesNothing() {
        assertDoesNotThrow(() -> hero.reduceHP(-10L));
        assertEquals(hero.getMaxHP(), hero.getHP());
    }

    @Test
    public void testIncreaseHPWithinBounds(){
        hero.reduceHP(200L);
        assertEquals(797L, hero.getHP());

        hero.increaseHP(100L);
        assertEquals(897L, hero.getHP());

        hero.increaseHP(500L);
        assertEquals(hero.getMaxHP(), hero.getHP());
    }

    @Test
    public void testMonsterHPDoesNotIncrease(){
        monster.reduceHP(200L);
        assertEquals(797L, monster.getHP());

        monster.increaseHP(100L);
        assertEquals(797L, monster.getHP());
    }

    @Test
    public void testInvalidNameThrowsForHero() {
        assertThrows(InvalidNameException.class, () -> new Hero(""));
        assertThrows(InvalidNameException.class, () -> new Hero(" "));
    }

    @Test
    public void testInvalidNameThrowsForMonster() {
        assertThrows(InvalidNameException.class, () -> new Monster(""));
        assertThrows(InvalidNameException.class, () -> new Monster(" "));
    }

    @Test
    public void testAnchorPointsAccess() {
        assertNotNull(monster.getAnchorPointAt(0));
        assertNull(monster.getAnchorPointAt(10));
    }

    @Test
    public void testDamageTypePresence() {
        assertTrue(monster.hasDamageType(DamageType.CLAWS));
        assertTrue(hero.getAmountOfDamageTypes() > 0);
    }

    @Test
    public void testGetDefenseReflectsProtectionAndSkin() {
        int expected = hero.getProtection() + hero.getSkinType().getProtection();
        assertEquals(expected, hero.getDefense());
    }

    @Test
    public void testGetBaseDamageNonZero() {
        assertTrue(hero.getBaseDamage() > 0);
    }

    @Test
    public void testValidNameInvariant() {
        assertTrue(hero.isValidName(hero.getName()));
        assertTrue(monster.isValidName(monster.getName()));

        assertFalse(hero.isValidName(""));
        assertFalse(hero.isValidName(" "));
        assertFalse(monster.isValidName(""));
        assertFalse(monster.isValidName(" "));
    }

    @Test
    public void testHasValidItemsInvariant() throws InvalidHolderException, InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException {
        assertTrue(hero.hasValidItems());
        assertTrue(monster.hasValidItems());

        Weapon sword = new Weapon(5.0, hero, AnchorPoint.LEFTHAND, ShineLevel.MEDIUM, 15);
        hero.equip(AnchorPoint.LEFTHAND, sword);

        assertTrue(hero.hasValidItems());

        Hero otherHero = new Hero("Apollo");
        Weapon foreignWeapon = new Weapon(4.0, otherHero, AnchorPoint.RIGHTHAND, ShineLevel.MEDIUM, 10);
        hero.equip(AnchorPoint.LEFTHAND, foreignWeapon);

        assertTrue(otherHero.hasValidItems());
    }

    @Test
    public void testIsValidCapacity() throws InvalidValueException, InvalidHolderException {
        assertTrue(hero.isValidCapacity(0));
        assertTrue(hero.isValidCapacity(10));

        assertFalse(hero.isValidCapacity(-1));

        AnchorPoint heroAnchor = hero.getAnchorPointAt(0);
        Backpack validBackpack = new Backpack(3.0, 100, 15, hero, heroAnchor, ShineLevel.MEDIUM);
        assertTrue(validBackpack.isValidCapacity(validBackpack.getCapacity()));

        Backpack invalidBackpack = new Backpack(3.0, 100, -5, hero, heroAnchor, ShineLevel.MEDIUM);
        assertTrue(invalidBackpack.isValidCapacity(invalidBackpack.getCapacity()));
        assertTrue(invalidBackpack.getCapacity() >= 0);
    }

    @Test
    public void testCanHoldItemsInvariant() throws InvalidHolderException, InvalidValueException {
        assertTrue(hero.canHoldItems());
        assertTrue(monster.canHoldItems());

        Weapon weapon = new Weapon(150, hero, AnchorPoint.BACK, ShineLevel.MEDIUM, 10);
        Weapon weapon1 = new Weapon(150, hero, AnchorPoint.LEFTHAND, ShineLevel.MEDIUM, 10);

        assertTrue(weapon.getTotalWeight() + weapon1.getTotalWeight() > hero.getCapacity());
        assertFalse(hero.hasAsItem(weapon1));
    }

    @Test
    public void testHasValidDamageTypes() {
        assertTrue(hero.hasValidDamageTypes());

        assertTrue(monster.hasValidDamageTypes());

        HashSet<DamageType> invalidDamageTypes = new HashSet<>();
        invalidDamageTypes.add(DamageType.NORMAL);
        invalidDamageTypes.add(DamageType.CLAWS);

        assertFalse(hero.areValidDamageTypes(invalidDamageTypes));
    }

}
