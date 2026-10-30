package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    private Reservation value;
    private final LocalDate date = LocalDate.of(2026, 11, 1);

    @BeforeEach
    public void setUp() {
        value = new Reservation(1, new Customer(1, "Ana", "0991234567", "ana@example.com", "Quito"), new Event(1, "Feria", "Quito", date, date.plusDays(1)), "Confirmada", date, new ArrayList<>());
    }

    @Test
    public void testInitiallyEmptyAssignments() {
        assertTrue(value.getAssignments().isEmpty());
    }

    @Test
    public void testChangeStatus() {
        value.setStatus("Cancelada");
        assertEquals("Cancelada", value.getStatus());
    }

    @Test
    public void testAddValidAssignment() {
        Assignment assignment = new Assignment(1, new Equipment(1, "Mesa", null, 20), 2);
        value.getAssignments().add(assignment);
        assertEquals(1, value.getAssignments().size());
        assertSame(assignment, value.getAssignments().get(0));
    }

    @Test
    public void testNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setId(-1);
        });
    }

    @Test
    public void testNullCustomerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setCustomer(null);
        });
    }

    @Test
    public void testNullEventThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setEvent(null);
        });
    }

    @Test
    public void testBlankStatusThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setStatus("   ");
        });
    }

    @Test
    public void testNullDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setReservationDate(null);
        });
    }

    @Test
    public void testNullAssignmentsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setAssignments(null);
        });
    }

    @Test
    public void testAssignmentsContainingNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            value.setAssignments(new ArrayList<>(java.util.Arrays.asList((Assignment) null)));
        });
    }
}
