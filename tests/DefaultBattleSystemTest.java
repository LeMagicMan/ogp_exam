import com.RPG.Core.Hero;
import com.RPG.Core.Item;
import com.RPG.Core.Monster;
import com.RPG.Exception.*;
import com.RPG.Mechanics.DefaultBattleSystem;
import com.RPG.Mechanics.TreasureManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.naming.InvalidNameException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

class DefaultBattleSystemTest {

    private DefaultBattleSystem battleSystem;
    private Hero hero;
    private Monster monster;
    private ArrayList<Item> lootList;

    @BeforeEach
    void setUp() throws InvalidValueException, InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidHolderException {
        battleSystem = new DefaultBattleSystem();
        hero = new Hero("Gandalf");
        monster = new Monster("Sauron");
        lootList = new ArrayList<>();
    }

    /* ----------------------------------------------------------
     * Basic Battle Loop Behavior
     * ---------------------------------------------------------- */

    @Test
    void testBattleTerminatesImmediatelyIfHeroDead() {
        when(hero.isTerminated()).thenReturn(true);
        battleSystem.battle(hero, monster, lootList, monster);
        verify(monster, never()).getHP();
    }

    @Test
    void testBattleTerminatesImmediatelyIfMonsterDead() {
        when(hero.isTerminated()).thenReturn(false);
        when(monster.isTerminated()).thenReturn(true);
        battleSystem.battle(hero, monster, lootList, hero);
        verify(hero, never()).getHP();
    }

    @Test
    void testBattleAlternatesTurnsUntilOneDies() {
        when(hero.isTerminated()).thenReturn(false);
        when(monster.isTerminated()).thenReturn(false, true);
        when(hero.getAdjustedRoll(anyInt())).thenReturn(100);
        when(monster.getDefense()).thenReturn(10);
        when(hero.getBaseDamage()).thenReturn(50L);
        when(monster.getHP()).thenReturn(40L);

        try (MockedStatic<TreasureManager> tmMock = mockStatic(TreasureManager.class)) {
            battleSystem.battle(hero, monster, lootList, monster);

            verify(monster).reduceHP(50L);
            tmMock.verify(() -> TreasureManager.loot(monster, hero, lootList));
            verify(monster).kill();
            verify(hero).normaliseHP();
        }
    }

    /* ----------------------------------------------------------
     * Killing Blow Mechanics
     * ---------------------------------------------------------- */

    @Test
    void testKillingBlowTriggersLootKillAndHeal() {
        when(hero.isTerminated()).thenReturn(false);
        when(monster.isTerminated()).thenReturn(false, true);
        when(hero.getAdjustedRoll(anyInt())).thenReturn(99);
        when(monster.getDefense()).thenReturn(50);
        when(hero.getBaseDamage()).thenReturn(100L);
        when(monster.getHP()).thenReturn(50L);

        try (MockedStatic<TreasureManager> tmMock = mockStatic(TreasureManager.class)) {
            battleSystem.battle(hero, monster, lootList, hero);

            verify(monster).reduceHP(100L);
            verify(monster).kill();
            tmMock.verify(() -> TreasureManager.loot(monster, hero, lootList));
            verify(hero).normaliseHP();
        }
    }

    /* ----------------------------------------------------------
     * Missed Attacks and Non-Lethal Hits
     * ---------------------------------------------------------- */

    @Test
    void testAttackMissDoesNoDamage() {
        when(hero.isTerminated()).thenReturn(false);
        when(monster.isTerminated()).thenReturn(false, true);
        when(hero.getAdjustedRoll(anyInt())).thenReturn(20);
        when(monster.getDefense()).thenReturn(80);

        battleSystem.battle(hero, monster, lootList, monster);
        verify(monster, never()).reduceHP(anyLong());
        verify(monster, never()).kill();
    }

    @Test
    void testNonLethalHitDoesNotTriggerKill() {
        when(hero.isTerminated()).thenReturn(false);
        when(monster.isTerminated()).thenReturn(false, true);
        when(hero.getAdjustedRoll(anyInt())).thenReturn(100);
        when(monster.getDefense()).thenReturn(30);
        when(hero.getBaseDamage()).thenReturn(10L);
        when(monster.getHP()).thenReturn(100L);

        battleSystem.battle(hero, monster, lootList, hero);

        verify(monster).reduceHP(10L);
        verify(monster, never()).kill();
    }

}
