import com.RPG.Core.*;
import com.RPG.Exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BackpackTest {

    private Entity entity;
    private AnchorPoint anchorPoint;
    private Backpack backpack;

    @BeforeEach
    public void setUp() throws InvalidValueException, InvalidHolderException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException {
        entity = new Hero("Hero");
        anchorPoint = AnchorPoint.LEFTHAND;
        backpack = new Backpack(5, 20, 20, entity, anchorPoint, ShineLevel.MEDIUM);
    }

    @Test
    public void testBasicConstructorDefaults() throws Exception {
        Backpack b = new Backpack(entity, AnchorPoint.RIGHTHAND);
        assertEquals(5, b.getWeight());
        assertEquals(20, b.getValue());
        assertEquals(20, b.getCapacity());
        assertEquals(0, b.getAmountOfItems());
    }

    @Test
    public void testConstructorWithContent_Valid() throws Exception {
        Backpack temp = new Backpack(5, 20, 50, entity, AnchorPoint.LEFTHAND, ShineLevel.LOW);

        Weapon weapon = new Weapon(entity, AnchorPoint.RIGHTHAND);
        Weapon weapon1 = new Weapon(entity, AnchorPoint.LEFTHAND);
        ArrayList<Item> content = new ArrayList<>();
        content.add(weapon);
        content.add(weapon1);

        Backpack b = new Backpack(5, 20, 50, entity, AnchorPoint.LEFTHAND, ShineLevel.LOW, content);
        assertEquals(2, b.getAmountOfItems());
        assertTrue(b.hasAsItem(weapon));
    }

    @Test
    public void testConstructorWithInvalidContent_Throws() throws Exception {
        Backpack temp = new Backpack(5, 20, 20, entity, anchorPoint, ShineLevel.MEDIUM);
        Weapon weapon = new Weapon(null, null);
        Weapon weapon1 = new Weapon(null, null);
        Weapon weapon2 = new Weapon(null, null);
        ArrayList<Item> content = new ArrayList<>();
        content.add(weapon);
        content.add(weapon1);
        content.add(weapon2);
        assertThrows(InvalidItemsException.class, () -> new Backpack(5, 20, 20, entity, anchorPoint, ShineLevel.MEDIUM, content));
    }

    @Test
    public void testGetters() {
        assertEquals(0, backpack.getAmountOfItems());
        assertEquals(20, backpack.getCapacity());
        assertNull(backpack.getItemAt(0));
    }

    @Test
    public void testStoreAndUnpackItem() throws Exception {
        Weapon weapon = new Weapon(entity, AnchorPoint.RIGHTHAND);
        backpack.storeItem(weapon);
        assertTrue(backpack.hasAsItem(weapon));
        backpack.unpackItem(weapon);
        assertFalse(backpack.hasAsItem(weapon));
    }

    @Test
    public void testCanAddItem_ValidAndInvalid() throws Exception {
        Weapon w = new Weapon(entity, AnchorPoint.RIGHTHAND);
        assertTrue(backpack.canAddItem(w));

        Backpack tiny = new Backpack(1, 10, 1, entity, anchorPoint, ShineLevel.LOW);
        Weapon heavy = new Weapon(150, null, null, ShineLevel.MEDIUM, 20);
        assertFalse(tiny.canAddItem(heavy));
    }

    @Test
    public void testIsValidCapacity() {
        assertTrue(backpack.isValidCapacity(10));
        assertFalse(backpack.isValidCapacity(0));
    }

    @Test
    public void testCanStoreAll_EmptyOrNullList() {
        assertTrue(backpack.canStoreAll(null));
        assertTrue(backpack.canStoreAll(new ArrayList<>()));
    }

    @Test
    public void testCanStoreAll_WithItems() throws Exception {
        Weapon w1 = new Weapon(entity, anchorPoint);
        Weapon w2 = new Weapon(entity, anchorPoint);
        List<Item> items = List.of(w1, w2);
        assertTrue(backpack.canStoreAll(items));

        Backpack small = new Backpack(1, 10, 1, entity, anchorPoint, ShineLevel.LOW);
        assertFalse(small.canStoreAll(items));
    }

    @Test
    public void testGetTotalWeight_IncludesContents() throws Exception {
        double baseWeight = backpack.getWeight();
        Weapon w = new Weapon(entity, anchorPoint);
        backpack.storeItem(w);
        assertEquals(baseWeight + w.getWeight(), backpack.getTotalWeight());
    }

    @Test
    public void testRemoveItem_HandlesMissingGracefully() throws Exception {
        Weapon w = new Weapon(entity, anchorPoint);
        assertFalse(backpack.hasAsItem(w));
        backpack.unpackItem(w); // should not throw
    }

    @Test
    public void testCapacitySetterFallbacksToDefault() throws Exception {
        Backpack backpack = new Backpack(5, 10, -10, entity, anchorPoint, ShineLevel.HIGH);
        assertEquals(20, backpack.getCapacity());
    }

    @Test
    public void testCanHoldItems() throws InvalidValueException, InvalidHolderException {
        assertTrue(backpack.canHoldItems());
        Weapon bigItem = new Weapon(150, null, null, ShineLevel.MEDIUM, 20);
        backpack.storeItem(bigItem);
        assertFalse(backpack.hasAsItem(bigItem));
    }
}
