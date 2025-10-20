
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    @Test
    void add_twoPlusThree_returnsFive() {
        Calculator c = new Calculator();
        assertEquals(5, c.add(2, 3));
    }
}