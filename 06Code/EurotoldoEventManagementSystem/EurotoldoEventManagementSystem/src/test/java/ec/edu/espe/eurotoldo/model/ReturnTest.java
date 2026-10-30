package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReturnTest {
    private Return value;
    private final LocalDate date = LocalDate.of(2026, 11, 1);

    @BeforeEach
    public void setUp() {
        value = new Return(1, new Dispatch(1, new Reservation(1, new Customer(1, "Ana", "0991234567", "ana@example.com", "Quito"), new Event(1, "Feria", "Quito", date, date.plusDays(1)), "Confirmada", date, new ArrayList<>()), new Equipment(1, "Mesa", null, 20), 10, date, new User(1, "Luis", "luis", "test-hash", "Operador", true)), 8, 2, date.plusDays(1), new User(1, "Luis", "luis", "test-hash", "Operador", true));
    }

    @Test
    public void testValidReturnedQuantity() {
        assertEquals(8, value.getReturnedQuantity());
    }

    @Test
    public void testZeroDamageBoundary() {
        value.setDamagedQuantity(0);
        assertEquals(0, value.getDamagedQuantity());
    }

    @Test
    public void testCompleteUndamagedReturn() {
        value.setDamagedQuantity(0);
        value.setReturnedQuantity(10);
        assertEquals(10, value.getReturnedQuantity());
    }

    @Test
    public void testNullDispatchThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setDispatch(null);
        });
    }

    @Test
    public void testNegativeReturnedQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setReturnedQuantity(-1);
        });
    }

    @Test
    public void testNegativeDamagedQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setDamagedQuantity(-1);
        });
    }

    @Test
    public void testReturnExceedingDispatchThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setReturnedQuantity(11);
        });
    }

    @Test
    public void testDamageExceedingReturnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setDamagedQuantity(9);
        });
    }

    @Test
    public void testReturnBeforeDispatchThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setReturnDate(date.minusDays(1));
        });
    }

    @Test
    public void testNullResponsibleUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setResponsibleUser(null);
        });
    }
}
