package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Event;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EventController {

    private final ArrayList<Event> events;
    private final EventFileManager fileManager;

    public EventController() {
        events = new ArrayList<>();
        fileManager = new EventFileManager();
    }

    public boolean loadEvents() {

        ArrayList<Event> loadedEvents
                = fileManager.loadEvents();

        if (loadedEvents == null) {
            return false;
        }

        events.clear();

        for (Event event : loadedEvents) {

            if (!addEvent(event)) {
                events.clear();
                return false;
            }
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

        event.markSetupCompleted();
        return true;
    }

    public boolean markPickupCompleted(int eventId) {

        Event event = findEventById(eventId);

        if (event == null) {
            return false;
        }

        try {
            event.markPickupCompleted();
            return true;

        } catch (IllegalStateException e) {
            return false;
        }
    }

    public boolean removeEvent(int id) {

        Event event = findEventById(id);

        if (event == null) {
            return false;
        }

        return events.remove(event);
    }

    public ArrayList<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public boolean saveEvents() {
        return fileManager.saveEvents(events);
    }

    private boolean isValid(Event event) {

        if (event == null || event.getId() < 0) {
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