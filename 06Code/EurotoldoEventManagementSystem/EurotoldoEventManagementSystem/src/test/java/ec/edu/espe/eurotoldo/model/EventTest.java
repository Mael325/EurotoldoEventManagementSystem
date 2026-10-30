package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event value;
    private final LocalDate date = LocalDate.of(2026, 11, 1);

    @BeforeEach
    public void setUp() {
        value = new Event(1, "Feria", "Quito", date, date.plusDays(1));
    }

    @Test
    public void testValidDate() {
        assertEquals(date, value.getStartDate());
    }

    @Test
    public void testSameDayBoundary() {
        value.setEndDate(date);
        assertEquals(value.getStartDate(), value.getEndDate());
    }

    @Test
    public void testChangeLocation() {
        value.setLocation("Cuenca");
        assertEquals("Cuenca", value.getLocation());
    }

    @Test
    public void testExtendEvent() {
        value.setEndDate(date.plusDays(5));
        assertEquals(date.plusDays(5), value.getEndDate());
    }

    @Test
    public void testNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setId(-1);
        });
    }

    @Test
    public void testBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setName("   ");
        });
    }

    @Test
    public void testBlankLocationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setLocation("   ");
        });
    }

    @Test
    public void testNullStartDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setStartDate(null);
        });
    }

    @Test
    public void testNullEndDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setEndDate(null);
        });
    }

    @Test
    public void testEndBeforeStartThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setEndDate(date.minusDays(1));
        });
    }
}
