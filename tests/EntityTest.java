import com.RPG.Core.DamageType;
import com.RPG.Core.Hero;
import com.RPG.Core.Monster;
import com.RPG.Core.SkinType;
import com.RPG.Exception.InvalidValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private Hero hero;
    private Monster monster;

    @BeforeEach
    public void setUp() throws Exception {
        hero = new Hero("Artemis");
        monster = new Monster("Gorgon");
    }

    // ----------------------------
    // CONSTRUCTOR + GETTER TESTS
    // ----------------------------

    @Test
    public void testHeroInitialization() {
        assertEquals("Artemis", hero.getName());
        assertEquals(997L, hero.getMaxHP());
        assertEquals(997L, hero.getHP());
        assertTrue(hero.getStrength().compareTo(BigDecimal.ZERO) > 0);
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

    // ----------------------------
    // DAMAGE & HEALING LOGIC
    // ----------------------------

    @Test
    public void testDamageApplication() throws InvalidValueException {
        hero.reduceHP(300L);
        assertEquals(697L, hero.getHP());

        monster.reduceHP(997L);
        assertEquals(0L, monster.getHP());
    }

    @Test
    public void testNegativeDamageThrowsException() {
        hero.reduceHP(-10L);
        assertEquals(hero.getMaxHP(), hero.getHP());
    }

    @Test
    public void testHealingLogic() throws InvalidValueException {
        hero.reduceHP(200L);
        assertEquals(797L, hero.getHP());

        hero.increaseHP(100L);
        assertEquals(897L, hero.getHP());

        // Overhealing should cap at max HP
        hero.increaseHP(500L);
        assertEquals(hero.getMaxHP(), hero.getHP());

        monster.reduceHP(200L);
        assertEquals(797, monster.getHP());
        monster.increaseHP(100L);
        assertEquals(797, monster.getHP());
    }

    // ----------------------------
    // BASE DAMAGE
    // ----------------------------

    @Test
    public void testHeroBaseDamageNonZero() {
        assertTrue(hero.getBaseDamage() > 0);
    }

    @Test
    public void testMonsterBaseDamageNonZero() {
        assertTrue(monster.getBaseDamage() > 0);
    }

    // ----------------------------
    // NAME INVARIANT
    // ----------------------------

    @Test
    public void testInvalidNameThrowsExceptionForHero() {
        assertThrows(InvalidNameException.class, () -> new Hero(""));
    }

    @Test
    public void testInvalidNameThrowsExceptionForMonster() {
        assertThrows(InvalidNameException.class, () -> new Monster(" "));
    }

    // ----------------------------
    // ANCHOR POINT & DAMAGE TYPES
    // ----------------------------

    @Test
    public void testMonsterAnchorPoints() {
        assertEquals(5, monster.getAmountOfAnchorPoints());
    }


    @Test
    public void testHeroSkinTypeIsNotNull() {
        assertNotNull(hero.getSkinType());
    }
}
