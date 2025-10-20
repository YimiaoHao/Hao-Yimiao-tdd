import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculatorTest  {
    @Test
    void add_twoPlusThree_returnsFive() {
        Calculator c = new Calculator();
        assertEquals(5, c.add(2, 3));
    }

    @Test
    void add_onePlusOne_returnsTwo() {
        Calculator c = new Calculator();
        assertEquals(2, c.add(1, 1));
    }

    @Test
    void subtract_threeMinusOne_returnsTwo() {
        Calculator c = new Calculator();
        assertEquals(2, c.subtract(3, 1));
    }

    @Test
    void multiply_twoTimesFour_returnsEight() {
        Calculator c = new Calculator();
        assertEquals(8, c.multiply(2, 4));
    }

    @Test
    void divide_byZero_throws() {
        Calculator c = new Calculator();
        assertThrows(IllegalArgumentException.class, () -> c.divide(1, 0));
    }
    @Test
    void divide_sixByThree_returnsTwo() {
        Calculator c = new Calculator();
        assertEquals(2, c.divide(6, 3));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "2, 3, 5",
        "-1, 4, 3",
        "0, 0, 0"
    })
    void add_manyCases(int a, int b, int expected) {
        assertEquals(expected, new Calculator().add(a, b));
    }

}
