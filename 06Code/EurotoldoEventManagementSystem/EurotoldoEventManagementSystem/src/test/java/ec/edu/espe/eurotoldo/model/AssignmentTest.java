package ec.edu.espe.eurotoldo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Juan Alvarez, MosQ, @ESPE
 */
public class AssignmentTest {
   
    private Equipment equipment;
    private Assignment assignment;

    @BeforeEach
    public void setUp() {
        equipment = new Equipment(1, "Carpa 10x10", null, 10);
        assignment = new Assignment(100, equipment, 5);
    }

    @Test
    public void testGetId() {
        assertEquals(100, assignment.getId());
    }

    @Test
    public void testSetId() {
        assignment.setId(200);
        assertEquals(200, assignment.getId());
    }

    @Test
    public void testGetEquipment() {
        assertEquals(equipment, assignment.getEquipment());
    }

    @Test
    public void testSetEquipment() {
        Equipment newEq = new Equipment(2, "Silla Plegable", null, 20);
        assignment.setEquipment(newEq);
        assertEquals(newEq, assignment.getEquipment());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(5, assignment.getQuantity());
    }

    @Test
    public void testSetQuantity() {
        assignment.setQuantity(15);
        assertEquals(15, assignment.getQuantity());
    }

    @Test
    public void testGetIdError() {
        assertEquals(999, assignment.getId());
    }

    @Test
    public void testGetQuantityError() {
        assertEquals(50, assignment.getQuantity());
    }

    @Test
    public void testEquipmentNullError() {
        assertNull(assignment.getEquipment());
    }

    @Test
    public void testToStringError() {
        assertEquals("WrongString", assignment.toString());
    }
    
}
