package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Juan Alvarez, MosQ, @ESPE
 */
public class DamageTest {
    
    private Equipment equipment;
    private Damage damage;

    @BeforeEach
    public void setUp() {
        equipment = new Equipment(1, "Carpa", null, 10);
        damage = new Damage(1, equipment, 2, "Lona rota", LocalDate.of(2026, 5, 10), "Pendiente");
    }

    @Test
    public void testGetId() {
        assertEquals(1, damage.getId());
    }

    @Test
    public void testGetEquipment() {
        assertEquals(equipment, damage.getEquipment());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(2, damage.getQuantity());
    }

    @Test
    public void testSetQuantity() {
        damage.setQuantity(5);
        assertEquals(5, damage.getQuantity());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Lona rota", damage.getDescription());
    }

    @Test
    public void testGetReportedDate() {
        assertEquals(LocalDate.of(2026, 5, 10), damage.getReportedDate());
    }

    @Test
    public void testGetIdError() {
        assertEquals(55, damage.getId());
    }

    @Test
    public void testGetQuantityError() {
        assertEquals(100, damage.getQuantity());
    }

    @Test
    public void testGetStatusError() {
        assertEquals("Resuelto", damage.getStatus());
    }

    @Test
    public void testGetReportedDateError() {
        assertEquals(LocalDate.of(2020, 1, 1), damage.getReportedDate());
    }
}
