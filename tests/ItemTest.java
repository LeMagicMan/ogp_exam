import com.RPG.Core.*;
import com.RPG.Exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Entity entity;
    private AnchorPoint anchorPoint;

    @BeforeEach
    public void setUp() throws InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidHolderException {
        entity = new Hero("Artemis");
        anchorPoint = AnchorPoint.RIGHTHAND;
    }

    @Test
    public void testWeaponBasicConstructor_SetsDefaultValues() throws Exception {
        Weapon weapon = new Weapon(entity, anchorPoint);

        assertEquals(10, weapon.getWeight());
        assertEquals(ShineLevel.LOW, weapon.getShineLevel());
        assertEquals(entity, weapon.getHolder());
        assertEquals(ItemType.WEAPON, weapon.getItemType());
    }

    @Test
    public void testWeapon_HasValidHolder() throws Exception {
        Weapon weapon = new Weapon(entity, anchorPoint);
        assertTrue(weapon.isValidHolder(entity));
    }

    @Test
    public void testWeapon_InvalidHolder_ThrowsException() {
        entity.kill();
        assertThrows(InvalidHolderException.class, () -> new Weapon(entity, anchorPoint));
    }

    @Test
    public void testBackpackConstructor_SetsFieldsCorrectly() throws Exception {
        Backpack backpack = new Backpack(5.0, 100, 10, entity, anchorPoint, ShineLevel.HIGH);

        assertEquals(5.0, backpack.getWeight());
        assertEquals(100, backpack.getValue());
        assertEquals(10, backpack.getCapacity());
        assertEquals(ShineLevel.HIGH, backpack.getShineLevel());
        assertEquals(entity, backpack.getHolder());
        assertEquals(ItemType.BACKPACK, backpack.getItemType());
        assertTrue(backpack.getAmountOfItems() == 0);
    }

    @Test
    public void testBackpack_InvalidValue_ThrowsException() {
        assertThrows(InvalidValueException.class, () ->
                new Backpack(3.0, -10, 10, entity, anchorPoint, ShineLevel.LOW));
    }

    @Test
    public void testBackpack_InvalidWeight_UsesDefaultWeight() throws Exception {
        Backpack backpack = new Backpack(-5.0, 100, 5, entity, anchorPoint, ShineLevel.MEDIUM);
        assertEquals(10.0, backpack.getWeight()); // defaultWeight applied
    }

    @Test
    public void testIsValidWeight() throws InvalidValueException, InvalidHolderException {
        Weapon weapon = new Weapon(null, null);
        assertTrue(weapon.isValidWeight(10));
        assertFalse(weapon.isValidWeight(-1));
    }

    @Test
    public void testIsValidValue() throws InvalidValueException, InvalidHolderException {
        Weapon weapon = new Weapon(null, null);
        assertTrue(weapon.isValidValue(100));
        assertFalse(weapon.isValidValue(-5));
        assertFalse(weapon.isValidValue(600));
    }

    @Test
    public void testBackpackBackpackGetterInitiallyNull() throws Exception {
        Weapon weapon = new Weapon(entity, anchorPoint);
        assertNull(weapon.getBackpack());
    }

    @Test
    public void testItemIsValidItem() throws Exception {
        Weapon weapon = new Weapon(entity, anchorPoint);
        assertTrue(weapon.isValidItem());
    }

    @Test
    public void testItemTerminatedFalseInitially() throws Exception {
        Weapon weapon = new Weapon(entity, anchorPoint);
        assertFalse(weapon.isTerminated());
    }
}
