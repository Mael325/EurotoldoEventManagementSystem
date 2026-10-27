package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Event;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class EventControllerTest {

    private EventController controller;
    
    @BeforeEach
    public void setUp() {
        controller = new EventController();
    }

    @Test
    public void testAddEvent() {
        Event event = new Event(1, "Boda Real", "Quito Tenis",
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 12));
        boolean result = controller.addEvent(event);
        assertTrue(result);
    }

    @Test
    public void testAddEventSameDates() {
        Event event = new Event(2, "Conferencia Tech", "Auditorio ESPE",
                LocalDate.of(2026, 7, 15), LocalDate.of(2026, 7, 15));
        boolean result = controller.addEvent(event);
        assertTrue(result);
    }

    @Test
    public void testFindEventById() {
        Event event = new Event(3, "Fiesta Corporativa", "Hotel Marriott",
                LocalDate.of(2026, 8, 20), LocalDate.of(2026, 8, 21));
        controller.addEvent(event);
        Event found = controller.findEventById(3);
        assertNotNull(found);
    }

    @Test
    public void testUpdateEvent() {
        Event original = new Event(4, "Graduación", "Campus Cumbayá",
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 2));
        controller.addEvent(original);
        Event updatedInfo = new Event(4, "Graduación ESPE", "Salón Principal",
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3));
        boolean updated = controller.updateEvent(updatedInfo);
        assertTrue(updated);
    }

    @Test
    public void testGetEvents() {
        controller.addEvent(new Event(5, "Aniversario", "Cumbayá", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2)));
        List<Event> list = controller.getEvents();
        assertEquals(1, list.size());
    }

    @Test
    public void testSaveAndLoadEvents() {
        EventFileManager fileManager = new EventFileManager();
        Event event = new Event(7, "Expo Feria", "Centro de Exposiciones",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5));
        fileManager.saveEvents(List.of(event));
        List<Event> loadedEvents = fileManager.loadEvents();
        assertNotNull(loadedEvents);
    }

    @Test
    public void testAddNullEvent() {
        // Al enviar null, controller.addEvent(null) retorna false.
        // Al forzar assertTrue(), NetBeans mostrará "Failed: expected <true> but was <false>"
        boolean result = controller.addEvent(null);
        assertTrue(result);
    }

    @Test
    public void testAddDuplicateIdEvent() {
        Event e1 = new Event(8, "Concierto 1", "Estadio", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 2));
        controller.addEvent(e1);
        Event e2 = new Event(8, "Concierto Repetido", "Coliseo", LocalDate.of(2026, 6, 5), LocalDate.of(2026, 6, 6));

        boolean result = controller.addEvent(e2);
        assertTrue(result); // Genera error de aserción
    }

    @Test
    public void testAddEmptyLocationEvent() {
        Event eEmptyLoc = new Event(9, "Reunión Privada", "   ", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        boolean result = controller.addEvent(eEmptyLoc);
        assertTrue(result); // Genera error de aserción
    }

    @Test
    public void testAddInvalidDatesEvent() {
        Event eBadDates = new Event(10, "Feria Libro", "Metropolitano",
                LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 5));
        boolean result = controller.addEvent(eBadDates);
        assertTrue(result); // Genera error de aserción
    }
}
