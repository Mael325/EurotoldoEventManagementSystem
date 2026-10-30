package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaintenanceTest {
    private Maintenance value;
    private final LocalDate date = LocalDate.of(2026, 11, 1);

    @BeforeEach
    public void setUp() {
        value = new Maintenance(1, new Equipment(1, "Mesa", null, 20), 2, "Limpieza", date, date.plusDays(1), "Pendiente");
    }

    @Test
    public void testValidQuantity() {
        assertEquals(2, value.getQuantity());
    }

    @Test
    public void testSameDayBoundary() {
        value.setEndDate(date);
        assertEquals(value.getStartDate(), value.getEndDate());
    }

    @Test
    public void testChangeDescription() {
        value.setDescription("Reparación");
        assertEquals("Reparación", value.getDescription());
    }

    @Test
    public void testQuantityAtStockBoundary() {
        value.setQuantity(20);
        assertEquals(20, value.getQuantity());
    }

    @Test
    public void testNullEquipmentThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setEquipment(null);
        });
    }

    @Test
    public void testNegativeQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setQuantity(-1);
        });
    }

    @Test
    public void testQuantityExceedingStockThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setQuantity(21);
        });
    }

    @Test
    public void testNullStartDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setStartDate(null);
        });
    }

    @Test
    public void testEndBeforeStartThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setEndDate(date.minusDays(1));
        });
    }

    @Test
    public void testBlankStatusThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setStatus("   ");
        });
    }
}
