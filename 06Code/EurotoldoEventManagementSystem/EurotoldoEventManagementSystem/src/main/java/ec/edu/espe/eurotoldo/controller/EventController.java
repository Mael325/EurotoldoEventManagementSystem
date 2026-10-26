package ec.edu.espe.eurotoldo.controller;


import ec.edu.espe.eurotoldo.model.Event;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class EventController {
    
    private final List<Event> events = new ArrayList<>();
    
    public boolean addEvent(Event event){
        if (!isValid(event)){
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

        Event existingEvent = findEventById(event.getId());

        if (existingEvent == null) {
            return false;
        }

        existingEvent.setName(event.getName());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setEndDate(event.getEndDate());

        return true;
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    private boolean isValid(Event event) {
        if (event == null) {
            return false;
        }

        if (event.getLocation() == null
                || event.getLocation().trim().isEmpty()) {
            return false;
        }

        if (event.getStartDate() == null
                || event.getEndDate() == null) {
            return false;
        }

        return !event.getEndDate().isBefore(event.getStartDate());
    }
}
    
