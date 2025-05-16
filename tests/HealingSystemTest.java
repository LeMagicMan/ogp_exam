import com.RPG.Core.Hero;
import com.RPG.Core.Monster;
import com.RPG.Exception.*;
import com.RPG.Mechanics.HealingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;

import static org.junit.jupiter.api.Assertions.*;

public class HealingSystemTest {

    private HealingSystem healingSystem;
    private Hero hero;
    private Monster monster;

    @BeforeEach
    void setUp() throws Exception {
        healingSystem = new HealingSystem();
        hero = new Hero("Hero");
        monster = new Monster("Monster");
    }

    @Test
    void heroShouldBeHealable() {
        assertTrue(hero.isHealable(), "Hero should be healable");
    }

    @Test
    void monsterShouldNotBeHealable() {
        assertFalse(monster.isHealable(), "Monster should not be healable");
    }

    @Test
    void healingShouldIncreaseHeroHPButNotExceedMaxHP() {
        long initialHP = hero.getHP();
        long maxHP = hero.getMaxHP();

        healingSystem.heal(hero);

        long afterHP = hero.getHP();
        assertTrue(afterHP >= initialHP, "Hero HP should not decrease after healing");
        assertTrue(afterHP <= maxHP, "Hero HP should not exceed max HP");
    }

    @Test
    void healingShouldNotChangeMonsterHP() {
        long initialHP = monster.getHP();

        healingSystem.heal(monster);

        long afterHP = monster.getHP();
        assertEquals(initialHP, afterHP, "Monster HP should not change after healing");
    }

    @Test
    void healingShouldDoNothingIfHeroHPIsMax() throws Exception {
        hero = new Hero("Hero");
        long maxHP = hero.getMaxHP();

        while (hero.getHP() < maxHP) {
            hero.increaseHP(1);
        }
        assertEquals(maxHP, hero.getHP());

        healingSystem.heal(hero);

        assertEquals(maxHP, hero.getHP(), "Healing when HP is max should not increase HP");
    }

    @Test
    void healingAmountIsWithinExpectedBounds() throws InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidHolderException {
        hero = new Hero("Hero");
        long initialHP = hero.getHP();
        long maxHP = hero.getMaxHP();

        healingSystem.heal(hero);

        long afterHP = hero.getHP();
        long healedAmount = afterHP - initialHP;

        assertTrue(healedAmount >= 0, "Healed amount should be non-negative");
        assertTrue(healedAmount <= (maxHP - initialHP), "Healed amount should not exceed missing HP");
    }
}
