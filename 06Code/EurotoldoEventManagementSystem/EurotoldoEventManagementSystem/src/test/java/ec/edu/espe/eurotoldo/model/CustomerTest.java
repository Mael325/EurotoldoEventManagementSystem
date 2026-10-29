package ec.edu.espe.eurotoldo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Juan Alvarez, MosQ, @ESPE
 */
public class CustomerTest {
    
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer(1, "Juan Perez", "0991234567", "juan@test.com", "Quito");
    }

    @Test
    public void testGetId() {
        assertEquals(1, customer.getId());
    }

    @Test
    public void testSetName() {
        customer.setName("Carlos Perez");
        assertEquals("Carlos Perez", customer.getName());
    }

    @Test
    public void testGetPhone() {
        assertEquals("0991234567", customer.getPhone());
    }

    @Test
    public void testSetPhone() {
        customer.setPhone("0987654321");
        assertEquals("0987654321", customer.getPhone());
    }

    @Test
    public void testGetEmail() {
        assertEquals("juan@test.com", customer.getEmail());
    }

    @Test
    public void testGetCity() {
        assertEquals("Quito", customer.getCity());
    }

    @Test
    public void testGetIdError() {
        assertEquals(99, customer.getId());
    }

    @Test
    public void testGetNameError() {
        assertEquals("Pedro Picapiedra", customer.getName());
    }

    @Test
    public void testGetEmailError() {
        assertEquals("falso@test.com", customer.getEmail());
    }

    @Test
    public void testGetCityError() {
        assertEquals("Cuenca", customer.getCity());
    }
}
