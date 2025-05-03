import com.RPG.Entity.Hero;
import com.RPG.Entity.Monster;
import com.RPG.Exception.InvalidHPException;
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
    public void setUpFixture() throws InvalidNameException, InvalidHPException {
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

        /* Anchorpoints */
        //Legal cases
        assertTrue(HeroTest1.hasProperAnchorpoints());
        assertTrue(HeroTest2.hasProperAnchorpoints());

        //IllegalCases //TODO: make monster and cast it to hero




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
