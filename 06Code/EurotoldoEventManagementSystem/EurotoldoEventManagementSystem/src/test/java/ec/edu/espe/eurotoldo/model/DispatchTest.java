package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DispatchTest {
    private Dispatch value;
    private final LocalDate date = LocalDate.of(2026, 11, 1);

    @BeforeEach
    public void setUp() {
        value = new Dispatch(1, new Reservation(1, new Customer(1, "Ana", "0991234567", "ana@example.com", "Quito"), new Event(1, "Feria", "Quito", date, date.plusDays(1)), "Confirmada", date, new ArrayList<>()), new Equipment(1, "Mesa", null, 20), 10, date, new User(1, "Luis", "luis", "test-hash", "Operador", true));
    }

    @Test
    public void testValidQuantity() {
        assertEquals(10, value.getQuantity());
    }

    @Test
    public void testChangeDate() {
        value.setDispatchDate(date.plusDays(1));
        assertEquals(date.plusDays(1), value.getDispatchDate());
    }

    @Test
    public void testQuantityAtStockBoundary() {
        value.setQuantity(20);
        assertEquals(20, value.getQuantity());
    }

    @Test
    public void testNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setId(-1);
        });
    }

    @Test
    public void testNullReservationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setReservation(null);
        });
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
    public void testNullDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setDispatchDate(null);
        });
    }

    @Test
    public void testNullResponsibleUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setResponsibleUser(null);
        });
    }
}
