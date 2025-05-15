import com.RPG.Core.AnchorPoint;
import com.RPG.Core.DamageType;
import com.RPG.Core.Monster;
import com.RPG.Core.SkinType;
import com.RPG.Exception.InvalidDamageTypesException;
import com.RPG.Exception.InvalidSkinTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterTest {

    private String validName;
    private Long validHP;
    private ArrayList<AnchorPoint> defaultAnchorPoints;
    private HashSet<DamageType> validDamageTypes;
    private SkinType validSkinType;

    @BeforeEach
    public void setUp() {
        validName = "Goblin";
        validHP = 123L;
        defaultAnchorPoints = new ArrayList<>(List.of(
                AnchorPoint.BACK,
                AnchorPoint.LEFTHAND,
                AnchorPoint.BODY
        ));
        validDamageTypes = new HashSet<>(Set.of(DamageType.CLAWS));
        validSkinType = SkinType.THICK;
    }

    /***************
     * Constructor Tests
     ***************/

    @Test
    public void testFullConstructorValid() throws Exception {
        Monster monster = new Monster(validName, validHP, defaultAnchorPoints, validDamageTypes, validSkinType);
        assertEquals(validName, monster.getName());
        assertEquals(validSkinType, monster.getSkinType());
        assertTrue(monster.areValidDamageTypes(validDamageTypes));
        assertEquals(validHP, monster.getHP());
        assertTrue(monster.hasProperAnchorpoints());
    }

    @Test
    public void testBasicConstructorDefaults() throws Exception {
        Monster monster = new Monster("Troll");
        assertEquals("Troll", monster.getName());
        assertEquals(SkinType.THICK, monster.getSkinType());
        assertTrue(monster.hasDamageType(DamageType.CLAWS));
        assertEquals(5, monster.getAmountOfAnchorPoints());
    }

    /***************
     * Name Validation
     ***************/

    @Test
    public void testValidMonsterNameWithColon() throws Exception {
        Monster monster = new Monster("Brute: Alpha");
        assertEquals("Brute: Alpha", monster.getName());
    }

    @Test
    public void testInvalidMonsterNameFormat() {
        assertThrows(InvalidNameException.class, () -> new Monster("brute"));
        assertThrows(InvalidNameException.class, () -> new Monster("123Orc"));
        assertThrows(InvalidNameException.class, () -> new Monster(""));
    }

    /***************
     * SkinType & DamageType Validation
     ***************/

    @Test
    public void testValidSkinTypeCheck() throws Exception {
        Monster monster = new Monster(validName);
        assertTrue(monster.isValidSkinType(monster.getSkinType()));
    }

    @Test
    public void testInvalidSkinTypeRejected() throws Exception {
        Monster monster = new Monster(validName);
        assertFalse(monster.isValidSkinType(SkinType.NORMAL));
    }

    @Test
    public void testInvalidDamageTypeWithNORMAL() throws InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException {
        HashSet<DamageType> badTypes = new HashSet<>(List.of(DamageType.NORMAL));
        assertFalse(new Monster("Warg").areValidDamageTypes(badTypes));
    }

    @Test
    public void testMultipleDamageTypesInvalid() throws InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException {
        HashSet<DamageType> multi = new HashSet<>(Set.of(DamageType.CLAWS, DamageType.TAIL));
        assertFalse(new Monster("Warg").areValidDamageTypes(multi));
    }

    /***************
     * Capacity & AnchorPoints
     ***************/

    @Test
    public void testHasProperAnchorpoints() throws Exception {
        Monster monster = new Monster(validName);
        assertTrue(monster.hasProperAnchorpoints());
    }

    /***************
     * Adjusted Roll
     ***************/

    @Test
    public void testAdjustedRollLessThanHP() throws Exception {
        Monster monster = new Monster(validName);
        assertEquals(50, monster.getAdjustedRoll(50));
    }

    @Test
    public void testAdjustedRollMoreThanHP() throws Exception {
        Monster monster = new Monster(validName);
        assertEquals(997, monster.getAdjustedRoll(1000));
    }

    /***************
     * Healable / Intelligent
     ***************/

    @Test
    public void testMonsterIsNotHealable() throws Exception {
        Monster monster = new Monster(validName);
        assertFalse(monster.isHealable());
    }

    @Test
    public void testMonsterIsNotIntelligentByDefault() throws Exception {
        Monster monster = new Monster(validName);
        assertFalse(monster.isIntelligent());
    }

    /***************
     * Name Regex Getter
     ***************/

    @Test
    public void testGetNameRegex() {
        Monster monster = assertDoesNotThrow(() -> new Monster(validName));
        assertEquals("^[A-Z][a-zA-Z ’:]*$", monster.getNameRegex());
    }
}
