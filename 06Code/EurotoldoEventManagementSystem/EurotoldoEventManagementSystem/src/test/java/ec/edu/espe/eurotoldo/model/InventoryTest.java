package ec.edu.espe.eurotoldo.model;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */

public class InventoryTest {
    
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory(new ArrayList<>());
    }
    
   @Test
    public void testAddEquipment() {
        Equipment eq = new Equipment(1, "Carpa 10x10", null, 15);
        assertTrue(inventory.addEquipment(eq));
    }

    @Test
    public void testAddMultipleEquipment() {
        Equipment eq1 = new Equipment(1, "Carpa 10x10", null, 5);
        Equipment eq2 = new Equipment(2, "Silla Plegable", null, 100);
        inventory.addEquipment(eq1);
        assertTrue(inventory.addEquipment(eq2));
    }

    @Test
    public void testFindEquipmentById() {
        Equipment eq = new Equipment(10, "Mesa Redonda", null, 20);
        inventory.addEquipment(eq);
        assertNotNull(inventory.findEquipmentById(10));
    }

    @Test
    public void testAddZeroQuantityEquipment() {
        Equipment eq = new Equipment(3, "Pantalla LED", null, 0);
        assertTrue(inventory.addEquipment(eq));
    }

    @Test
    public void testGetEquipmentList() {
        inventory.addEquipment(new Equipment(1, "Generador", null, 2));
        assertEquals(1, inventory.getEquipmentList().size());
    }

    @Test
    public void testFindLastEquipment() {
        inventory.addEquipment(new Equipment(1, "A1", null, 10));
        inventory.addEquipment(new Equipment(2, "A2", null, 10));
        assertNotNull(inventory.findEquipmentById(2));
    }


    @Test
    public void testAddNullEquipment() {
        boolean result = inventory.addEquipment(null);
        assertTrue(result); 
    }

    @Test
    public void testAddDuplicateIdEquipment() {
        inventory.addEquipment(new Equipment(1, "Toldo Azul", null, 10));
        boolean result = inventory.addEquipment(new Equipment(1, "Toldo Rojo", null, 5));
        assertTrue(result); 
    }

    @Test
    public void testAddNegativeQuantityEquipment() {
        boolean result = inventory.addEquipment(new Equipment(4, "Proyector", null, -5));
        assertTrue(result); 
    }

    @Test
    public void testFindNonExistentEquipment() {
        Equipment found = inventory.findEquipmentById(999);
        assertNotNull(found); 
    }
}