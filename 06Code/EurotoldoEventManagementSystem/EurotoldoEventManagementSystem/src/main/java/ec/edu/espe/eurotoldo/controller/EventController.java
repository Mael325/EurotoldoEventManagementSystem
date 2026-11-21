package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Event;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class EventController {

    private final ArrayList<Event> events;
    private final EventApiClient apiClient;

    public EventController() {

        events = new ArrayList<>();
        apiClient = new EventApiClient();
    }

    public boolean loadEvents() {

        ArrayList<Event> loadedEvents
                = apiClient.getEvents();

        if (loadedEvents == null) {
            return false;
        }

        events.clear();

        for (Event event : loadedEvents) {

            if (!isValid(event)
                    || findEventById(event.getId()) != null) {

                events.clear();
                return false;
            }

            events.add(event);
        }

        return true;
    }

    public boolean addEvent(Event event) {

        if (!isValid(event)) {
            return false;
        }

        if (findEventById(event.getId()) != null) {
            return false;
        }

        if (!apiClient.addEvent(event)) {
            return false;
        }

        events.add(event);
        return true;
    }

    public Event findEventById(int id) {

        for (Event event : events) {

            if (event.getId() == id) {
                return event;
            }
        }

        return null;
    }

    public boolean updateEvent(Event event) {

        if (!isValid(event)) {
            return false;
        }

        Event existingEvent
                = findEventById(event.getId());

        if (existingEvent == null) {
            return false;
        }

        if (!apiClient.updateEvent(event)) {
            return false;
        }

        existingEvent.setName(event.getName());
        existingEvent.setEventType(
                event.getEventType()
        );
        existingEvent.setLocation(
                event.getLocation()
        );
        existingEvent.setStartDate(
                event.getStartDate()
        );
        existingEvent.setEndDate(
                event.getEndDate()
        );
        existingEvent.setSetupDateTime(
                event.getSetupDateTime()
        );
        existingEvent.setPickupDateTime(
                event.getPickupDateTime()
        );

        return true;
    }

    public boolean markSetupCompleted(int eventId) {

        Event event = findEventById(eventId);

        if (event == null) {
            return false;
        }

        if (!apiClient.markSetupCompleted(eventId)) {
            return false;
        }

        event.markSetupCompleted();
        return true;
    }

    public boolean markPickupCompleted(int eventId) {

        Event event = findEventById(eventId);

        if (event == null
                || !event.isSetupCompleted()) {
            return false;
        }

        if (!apiClient.markPickupCompleted(eventId)) {
            return false;
        }

        try {
            event.markPickupCompleted();
            return true;

        } catch (IllegalStateException exception) {
            return false;
        }
    }

    public boolean removeEvent(int id) {

        Event event = findEventById(id);

        if (event == null) {
            return false;
        }

        if (!apiClient.deleteEvent(id)) {
            return false;
        }

        return events.remove(event);
    }

    public ArrayList<Event> getEvents() {
        return new ArrayList<>(events);
    }

    /*
     * Se conserva temporalmente porque algunas vistas todavía
     * pueden utilizarlo. La API ya guarda cada cambio.
     */
    public boolean saveEvents() {
        return true;
    }

    private boolean isValid(Event event) {

        if (event == null || event.getId() <= 0) {
            return false;
        }

        if (event.getName() == null
                || event.getName().isBlank()) {
            return false;
        }

        if (event.getEventType() == null
                || event.getEventType().isBlank()) {
            return false;
        }

        if (event.getLocation() == null
                || event.getLocation().isBlank()) {
            return false;
        }

        return event.hasValidDates()
                && event.hasValidSchedule();
    }
}