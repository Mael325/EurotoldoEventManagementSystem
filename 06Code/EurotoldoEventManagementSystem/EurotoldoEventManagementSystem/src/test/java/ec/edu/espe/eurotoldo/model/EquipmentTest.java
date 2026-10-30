package ec.edu.espe.eurotoldo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EquipmentTest {
    private Equipment value;

    @BeforeEach
    public void setUp() {
        value = new Equipment(1, "Mesa", null, 20);
    }

    @Test
    public void testValidStock() {
        assertEquals(20, value.getTotalQuantity());
    }

    @Test
    public void testZeroStockBoundary() {
        value.setTotalQuantity(0);
        assertEquals(0, value.getTotalQuantity());
    }

    @Test
    public void testLargeValidStock() {
        value.setTotalQuantity(100000);
        assertEquals(100000, value.getTotalQuantity());
    }

    @Test
    public void testOptionalCategory() {
        assertNull(value.getCategory());
    }

    @Test
    public void testChangeName() {
        value.setName("Mesa redonda");
        assertEquals("Mesa redonda", value.getName());
    }

    @Test
    public void testChangeCategory() {
        Category category = new Category(2, "Mesas", "Mobiliario");
        value.setCategory(category);
        assertSame(category, value.getCategory());
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
    public void testBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setName("   ");
        });
    }

    @Test
    public void testNegativeStockThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setTotalQuantity(-1);
        });
    }
}
