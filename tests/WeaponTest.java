import com.RPG.Core.*;
import com.RPG.Exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.util.HashSet;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

     Entity holder;
     AnchorPoint anchor;


    @BeforeEach
    void setUp() throws InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidValueException, InvalidItemsException, InvalidHolderException {
        holder = new Hero("Warrior");
        anchor = AnchorPoint.LEFTHAND;
    }

    @Test
    void testConstructorWithValidParameters() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(12.5, holder, anchor, ShineLevel.MEDIUM, 50);
        assertEquals(12.5, weapon.getWeight());
        assertEquals(50, weapon.getDamage());
        assertEquals(holder, weapon.getHolder());
        assertEquals(anchor, weapon.getHolder().getAnchorPointWithItem(weapon));
        assertEquals(ItemType.WEAPON, weapon.getItemType());
    }

    @Test
    void testConstructorInvalidDamageFallsBackToDefault() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(8.0, holder, anchor, ShineLevel.HIGH, -99);
        assertEquals(10, weapon.getDamage());
    }

    @Test
    void testSecondaryConstructorSetsDefaults() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(holder, anchor);
        assertEquals(10, weapon.getDamage());
        assertEquals(10.0, weapon.getWeight()); // defaultWeight
        assertEquals(ShineLevel.LOW, weapon.getShineLevel());
    }

    @Test
    void testIsValidDamage() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(holder, anchor);
        assertTrue(weapon.isValidDamage(1));
        assertTrue(weapon.isValidDamage(50));
        assertTrue(weapon.isValidDamage(100));
        assertFalse(weapon.isValidDamage(0));
        assertFalse(weapon.isValidDamage(100 + 1));
        assertFalse(weapon.isValidDamage(-1));
    }

    @Test
    void testCalculateValueWithValidDamage() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(12.5, holder, anchor, ShineLevel.MEDIUM, 50);
        assertEquals(100, weapon.getValue());
    }

    @Test
    void testCalculateValueWithZeroDamage() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(holder, anchor);
        assertEquals(20, weapon.getValue());
    }

    @Test
    void testUniqueIdIsMultipleOfSixAndPositive() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(holder, anchor);
        long id = weapon.getId();
        assertTrue(id > 0);
        assertEquals(0, id % 6);
    }

    @Test
    void testGeneratedIdsAreUnique() throws InvalidHolderException, InvalidValueException {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            Weapon weapon = new Weapon(holder, anchor);
            assertTrue(ids.add(weapon.getId()), "Duplicate ID found: " + weapon.getId());
        }
    }

    @Test
    void testWeaponHasNoContainedItems() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(holder, anchor);
        assertEquals(0, weapon.getAmountOfItems());
        assertNull(weapon.getItemAt(0));
        assertNull(weapon.getItemAt(-1));
        assertNull(weapon.getItemAt(999));
    }

    @Test
    void testMaxDamageAccepted() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(10.0, holder, anchor, ShineLevel.LOW, 100);
        assertEquals(100, weapon.getDamage());
        assertTrue(weapon.isValidDamage(100));
    }

    @Test
    void testNegativeWeight() throws InvalidHolderException, InvalidValueException {
        Weapon weapon = new Weapon(-5.0, holder, anchor, ShineLevel.HIGH, 10);
        assertEquals(10, weapon.getWeight());
    }

    @Test
    void testNullHolderThrowsException() throws InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidHolderException {
        Hero deadHero = new Hero("Dead");
        deadHero.kill();
        assertThrows(InvalidHolderException.class, () -> {
            new Weapon(10.0, deadHero, anchor, ShineLevel.LOW, 10);
        });
    }

    @Test
    void testTooHeavyThrowsValueException() throws InvalidValueException, InvalidHolderException {
        Weapon heavyWeapon = new Weapon(1000000, holder, anchor, ShineLevel.LOW, 20);
        assertEquals(10, heavyWeapon.getWeight());
    }

}
