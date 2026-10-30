package ec.edu.espe.eurotoldo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User value;

    @BeforeEach
    public void setUp() {
        value = new User(1, "Luis", "luis", "test-hash", "Operador", true);
    }

    @Test
    public void testInitiallyActive() {
        assertTrue(value.isActive());
    }

    @Test
    public void testDeactivate() {
        value.setActive(false);
        assertFalse(value.isActive());
    }

    @Test
    public void testReactivate() {
        value.setActive(false);
        value.setActive(true);
        assertTrue(value.isActive());
    }

    @Test
    public void testPasswordHashExcludedFromText() {
        assertFalse(value.toString().contains("test-hash"));
        assertFalse(value.toString().contains("passwordHash"));
    }

    @Test
    public void testNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setId(-1);
        });
    }

    @Test
    public void testNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setName(null);
        });
    }

    @Test
    public void testBlankUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setUsername("   ");
        });
    }

    @Test
    public void testNullPasswordHashThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setPasswordHash(null);
        });
    }

    @Test
    public void testBlankPasswordHashThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setPasswordHash("   ");
        });
    }

    @Test
    public void testBlankRoleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setRole("   ");
        });
    }
}
