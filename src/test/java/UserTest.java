import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
void user_ctor_and_getters() {
    var u = new User("u1", "Alice");
    assertEquals("u1", u.getId());
    assertEquals("Alice", u.getName());
    }

}
