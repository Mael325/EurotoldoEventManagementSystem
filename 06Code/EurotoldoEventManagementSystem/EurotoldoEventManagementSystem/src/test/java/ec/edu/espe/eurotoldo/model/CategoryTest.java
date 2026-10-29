package ec.edu.espe.eurotoldo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Juan Alvarez, MosQ, @ESPE
 */
public class CategoryTest {
    
   private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(1, "Estructuras", "Carpas de gran tamano");
    }

    @Test
    public void testGetId() {
        assertEquals(1, category.getId());
    }

    @Test
    public void testSetId() {
        category.setId(5);
        assertEquals(5, category.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Estructuras", category.getName());
    }

    @Test
    public void testSetName() {
        category.setName("Mobiliario");
        assertEquals("Mobiliario", category.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Carpas de gran tamano", category.getDescription());
    }

    @Test
    public void testSetDescription() {
        category.setDescription("Sillas y mesas");
        assertEquals("Sillas y mesas", category.getDescription());
    }

    @Test
    public void testGetIdError() {
        assertEquals(100, category.getId());
    }

    @Test
    public void testGetNameError() {
        assertEquals("Invalido", category.getName());
    }

    @Test
    public void testGetDescriptionError() {
        assertEquals("Otra descripcion", category.getDescription());
    }

    @Test
    public void testToStringError() {
        assertEquals("CategoryNull", category.toString());
    }
}
