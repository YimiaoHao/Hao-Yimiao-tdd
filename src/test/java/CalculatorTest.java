import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


}
