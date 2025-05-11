import com.RPG.Core.AnchorPoint;
import com.RPG.Core.Hero;
import com.RPG.Core.Monster;
import com.RPG.Exception.InvalidHPException;
import com.RPG.Exception.InvalidHolderException;
import com.RPG.Exception.InvalidValueException;
import com.RPG.Core.ShineLevel;
import com.RPG.Core.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {
    Hero HeroTest1;
    Monster MonsterTest1;
    Hero HeroTest2;
    Monster MonsterTest2;

    @BeforeEach
    public void setUpFixture() throws InvalidNameException, InvalidHPException, InvalidValueException, InvalidHolderException {
        HeroTest1 = new Hero("Gandalf");
        MonsterTest1 = new Monster("Carcharoth");
        HeroTest2 = new Hero("James: o’Ha’ra");
        MonsterTest2 = new Monster("James o’Hara");
    }

    @Test
    public void testHeroConstructor() {

        /* name */
        //Legal cases
        assertEquals("Gandalf", HeroTest1.getName());
        assertEquals("James: o’Ha’ra", HeroTest2.getName());

        //IllegalCases
        assertThrows(InvalidNameException.class, () -> new Hero("gandalf"));
        assertThrows(InvalidNameException.class, () -> new Hero("Ga’nd’al’f"));
        assertThrows(InvalidNameException.class, () -> new Hero("Gand:alf"));
        assertThrows(InvalidNameException.class, () -> new Hero("Gand&alf"));

        /* HP */
        //Legal cases
        assertEquals(997L, HeroTest1.getHP());
        assertEquals(997L, HeroTest2.getHP());
        assertTrue(HeroTest1.isValidHp(293L));

        //IllegalCases
        assertFalse(HeroTest1.isValidHp(-10L));
        assertFalse(HeroTest2.isValidHp(800L));
        assertFalse(HeroTest2.isValidHp(-1303L));

        /* Strength */
        //Legal cases
        assertTrue(HeroTest1.isValidStrength(HeroTest1.getStrength()));
        assertTrue(HeroTest2.isValidStrength(HeroTest2.getStrength()));
        assertTrue(HeroTest1.isValidStrength(800));

        //IllegalCases
        assertFalse(HeroTest2.isValidStrength(-800));
        
        /* Anchorpoints */
        //Legal cases
        assertTrue(HeroTest1.hasProperAnchorpoints());
        assertTrue(HeroTest2.hasProperAnchorpoints());

        //IllegalCases //TODO: make monster and cast it to hero

        /* DamageTypes */
        //Legal cases

        //IllegalCases



        /* Capacity */
        assertEquals(1000L, HeroTest1.getCapacity());
    }

    @Test
    public void testMonsterConstructor() {

        /* name */
        //Legal cases
        assertEquals("Carcharoth", MonsterTest1.getName());
        assertEquals("James o’Hara", MonsterTest2.getName());

        //IllegalCases
        assertThrows(InvalidNameException.class, () -> new Monster("carcharoth"));
        assertThrows(InvalidNameException.class, () -> new Monster("Carchar|oth"));

        /* Skin */

    }
}
