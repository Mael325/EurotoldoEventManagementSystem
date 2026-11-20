package ec.edu.espe.eurotoldo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User value;

    @BeforeEach
    public void setUp() {
        value = new User(1, "luis", "clave123", "Luis", UserRole.OPERATIONAL_STAFF, true);
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
    public void testCheckPasswordAndRole() {
        assertTrue(value.checkPassword("clave123"));
        assertFalse(value.checkPassword("incorrecta"));
        assertFalse(value.isAdministrator());

        value.setRole(UserRole.ADMINISTRATOR);
        assertTrue(value.isAdministrator());
    }

    @Test
    public void testPasswordExcludedFromText() {
        assertFalse(value.toString().contains("clave123"));
        assertFalse(value.toString().contains("password="));
    }

    @Test
    public void testNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setId(-1);
        });
    }

    @Test
    public void testNullFullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setFullName(null);
        });
    }

    @Test
    public void testBlankFullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setFullName("   ");
        });
    }

    @Test
    public void testBlankUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setUsername("   ");
        });
    }

    @Test
    public void testNullPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setPassword(null);
        });
    }

    @Test
    public void testBlankPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setPassword("   ");
        });
    }

    @Test
    public void testNullRoleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setRole(null);
        });
    }
}