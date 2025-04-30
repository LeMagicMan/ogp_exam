import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InitialTest {

    Boolean test;

    @BeforeEach
    public void setUpFixture() {
        test = false;
    }
    @Test
    public void test() {
        assertFalse(test);
        //assertTrue(test);
    }
}
