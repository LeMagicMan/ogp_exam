import com.RPG.Core.*;
import com.RPG.Exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class HeroTest {

    private String validName;
    private long validHP;
    private BigDecimal validStrength;
    private ArrayList<Item> defaultItems;

    @BeforeEach
    public void setUp() throws InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidHolderException {
        validName = "Hero";
        validHP = 997L;
        validStrength = BigDecimal.valueOf(50);
        defaultItems = new ArrayList<>();
        try {
            defaultItems.add(new Weapon(null, AnchorPoint.RIGHTHAND));
        } catch (Exception e) {
            fail("Failed to create default weapon: " + e.getMessage());
        }
    }

    @Test
    public void testHeroConstructorWithValidParams() throws Exception {
        Hero hero = new Hero(validName, validHP, validStrength, defaultItems);
        assertEquals(validName, hero.getName());
        assertEquals(SkinType.NORMAL, hero.getSkinType());
        assertTrue(hero.hasProperAnchorpoints());
        assertTrue(hero.isValidSkinType(hero.getSkinType()));
        assertTrue(hero.hasDamageType(DamageType.NORMAL));
    }

    @Test
    public void testHeroBasicConstructor() throws Exception {
        Hero hero = new Hero(validName);
        assertEquals(validName, hero.getName());
        assertEquals(SkinType.NORMAL, hero.getSkinType());
        assertTrue(hero.hasProperAnchorpoints());
        assertTrue(hero.isValidSkinType(hero.getSkinType()));
        assertTrue(hero.hasDamageType(DamageType.NORMAL));
    }

    @Test
    public void testValidNameWithColonAndSpaces() throws Exception {
        Hero hero = new Hero("John: The Brave");
        assertEquals("John: The Brave", hero.getName());
    }

    @Test
    public void testInvalidNameTooManyApostrophes() {
        assertThrows(InvalidNameException.class, () -> new Hero("O'Neil’O’Brien’Smith"));
    }

    @Test
    public void testInvalidNameColonWithoutSpace() {
        assertThrows(InvalidNameException.class, () -> new Hero("John:TheBrave"));
    }

    @Test
    public void testHeroSkinTypeIsAlwaysNormal() throws Exception {
        Hero hero = new Hero(validName);
        assertEquals(SkinType.NORMAL, hero.getSkinType());
        assertTrue(hero.isValidSkinType(hero.getSkinType()));
    }

    @Test
    public void testHeroHasOnlyNormalDamageType() throws Exception {
        Hero hero = new Hero(validName);;
        assertEquals(1, hero.getAmountOfDamageTypes());
        assertTrue(hero.hasDamageType(DamageType.NORMAL));
    }

    @Test
    public void testHeroWithInvalidDamageTypesFailsValidation() throws Exception {
        Hero hero = new Hero(validName);
        HashSet<DamageType> invalidTypes = new HashSet<>();
        invalidTypes.add(DamageType.NORMAL);
        invalidTypes.add(DamageType.CLAWS);
        assertFalse(hero.areValidDamageTypes(invalidTypes));
    }

    @Test
    public void testHeroCapacityBasedOnStrength() throws Exception {
        Hero hero = new Hero(validName, validHP, BigDecimal.valueOf(10), defaultItems);
        assertEquals(50, hero.getCapacity());
    }

    @Test
    public void testNegativeStrengthIsInvalid() {
        Hero hero = assertDoesNotThrow(() -> new Hero(validName));
        assertFalse(hero.isValidStrength(BigDecimal.valueOf(-1)));
    }

    @Test
    public void testZeroStrengthIsValid() {
        Hero hero = assertDoesNotThrow(() -> new Hero(validName));
        assertTrue(hero.isValidStrength(BigDecimal.ZERO));
    }

    @Test
    public void testStrengthScaleIsCorrect() throws Exception {
        Hero hero = new Hero(validName);
        assertEquals(2, hero.getStrength().scale());
    }

    @Test
    public void testHeroHasProperAnchorpoints() throws Exception {
        Hero hero = new Hero(validName);
        assertTrue(hero.hasProperAnchorpoints());
    }

    @Test
    public void testHeroWithoutBackpackFailsIfExtraItems() {
        ArrayList<Item> items = new ArrayList<>();
        try {
            items.add(new Weapon(null, AnchorPoint.RIGHTHAND));
            items.add(new Weapon(null, AnchorPoint.LEFTHAND));
            items.add(new Weapon(null, AnchorPoint.BELT));
            items.add(new Weapon(null, AnchorPoint.BODY));
            items.add(new Weapon(null, AnchorPoint.BACK));
            items.add(new Weapon(null, AnchorPoint.BELT)); // too many items
        } catch (Exception e) {
            fail("Item setup failed");
        }
        assertThrows(InvalidItemsException.class, () -> new Hero(validName, validHP, validStrength, items));
    }

    @Test
    public void testNullItemListDoesNotThrow() {
        assertDoesNotThrow(() -> new Hero(validName, validHP, validStrength, null));
    }

    @Test
    public void testEmptyItemListDoesNotThrow() {
        assertDoesNotThrow(() -> new Hero(validName, validHP, validStrength, new ArrayList<>()));
    }

    @Test
    public void testHeroIsHealable() throws Exception {
        Hero hero = new Hero(validName);
        assertTrue(hero.isHealable());
    }

    @Test
    public void testHeroIsIntelligent() throws Exception {
        Hero hero = new Hero(validName);
        assertTrue(hero.isIntelligent());
    }
}
