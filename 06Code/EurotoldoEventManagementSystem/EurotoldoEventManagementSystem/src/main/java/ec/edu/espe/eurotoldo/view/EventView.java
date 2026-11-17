package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.EventController;
import ec.edu.espe.eurotoldo.model.Event;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EventView {

    private final EventController controller;
    private final ConsoleInput input;

    public EventView(
            EventController controller,
            ConsoleInput input) {

        this.controller = controller;
        this.input = input;
    }

    public void registerEvent() {

        int id = input.readInt("Event ID: ");

        if (controller.findEventById(id) != null) {
            System.out.println(
                    "An event with that ID already exists.");
            return;
        }

        try {
            Event event = readEventData(id);

            if (!controller.addEvent(event)) {
                System.out.println(
                        "The event information is invalid.");
                return;
            }

            if (controller.saveEvents()) {
                System.out.println(
                        "Event registered successfully.");
            } else {
                System.out.println(
                        "The event was registered, "
                        + "but could not be saved.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEvent() {

        int id = input.readInt("Event ID: ");

        if (controller.findEventById(id) == null) {
            System.out.println(
                    "The event was not found.");
            return;
        }

        try {
            Event event = readEventData(id);

            if (!controller.updateEvent(event)) {
                System.out.println(
                        "The event information is invalid.");
                return;
            }

            if (controller.saveEvents()) {
                System.out.println(
                        "Event updated successfully.");
            } else {
                System.out.println(
                        "The event was updated, "
                        + "but could not be saved.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findEvent() {

        int id = input.readInt("Event ID: ");
        Event event = controller.findEventById(id);

        if (event == null) {
            System.out.println(
                    "The event was not found.");
            return;
        }

        showEventInformation(event);
    }

    public void removeEvent() {

        int id = input.readInt("Event ID: ");

        if (!controller.removeEvent(id)) {
            System.out.println(
                    "The event was not found.");
            return;
        }

        if (controller.saveEvents()) {
            System.out.println(
                    "Event removed successfully.");
        } else {
            System.out.println(
                    "The event was removed, "
                    + "but the changes could not be saved.");
        }
    }

    public void markSetupCompleted() {

        int id = input.readInt("Event ID: ");

        if (!controller.markSetupCompleted(id)) {
            System.out.println(
                    "The event was not found.");
            return;
        }

        if (controller.saveEvents()) {
            System.out.println(
                    "Setup marked as completed.");
        } else {
            System.out.println(
                    "The setup status could not be saved.");
        }
    }

    public void markPickupCompleted() {

        int id = input.readInt("Event ID: ");

        if (!controller.markPickupCompleted(id)) {
            System.out.println(
                    "The event was not found or "
                    + "the setup is still pending.");
            return;
        }

        if (controller.saveEvents()) {
            System.out.println(
                    "Pickup marked as completed.");
        } else {
            System.out.println(
                    "The pickup status could not be saved.");
        }
    }

    public void showEvents() {

        if (controller.getEvents().isEmpty()) {
            System.out.println(
                    "There are no registered events.");
            return;
        }

        String border = "+------+"
                + "--------------------+"
                + "----------------+"
                + "--------------------+"
                + "------------+"
                + "------------+";

        System.out.println("\nREGISTERED EVENTS");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-18s | %-14s | %-18s "
                + "| %-10s | %-10s |%n",
                "ID",
                "Name",
                "Type",
                "Location",
                "Start",
                "End"
        );

        System.out.println(border);

        for (Event event : controller.getEvents()) {

            System.out.printf(
                    "| %-4d | %-18s | %-14s | %-18s "
                    + "| %-10s | %-10s |%n",
                    event.getId(),
                    event.getName(),
                    event.getEventType(),
                    event.getLocation(),
                    event.getStartDate(),
                    event.getEndDate()
            );
        }

        System.out.println(border);
    }

    private Event readEventData(int id) {

        String name = input.readRequiredText(
                "Event name: ");

        String eventType = input.readRequiredText(
                "Event type: ");

        String location = input.readRequiredText(
                "Event location: ");

        LocalDate startDate = input.readDate(
                "Start date (YYYY-MM-DD): ");

        LocalDate endDate = input.readDate(
                "End date (YYYY-MM-DD): ");

        LocalDateTime setupDateTime
                = input.readDateTime(
                        "Setup date and time "
                        + "(YYYY-MM-DD HH:MM): "
                );

        LocalDateTime pickupDateTime
                = input.readDateTime(
                        "Pickup date and time "
                        + "(YYYY-MM-DD HH:MM): "
                );

        return new Event(
                id,
                name,
                eventType,
                location,
                startDate,
                endDate,
                setupDateTime,
                pickupDateTime
        );
    }

    private void showEventInformation(Event event) {

        System.out.println("\nEVENT INFORMATION");
        System.out.println("ID: " + event.getId());
        System.out.println(
                "Name: " + event.getName());
        System.out.println(
                "Type: " + event.getEventType());
        System.out.println(
                "Location: " + event.getLocation());
        System.out.println(
                "Start date: " + event.getStartDate());
        System.out.println(
                "End date: " + event.getEndDate());
        System.out.println(
                "Setup: " + event.getSetupDateTime());
        System.out.println(
                "Pickup: " + event.getPickupDateTime());

        System.out.println(
                "Setup status: "
                + (event.isSetupCompleted()
                        ? "Completed" : "Pending")
        );

        System.out.println(
                "Pickup status: "
                + (event.isPickupCompleted()
                        ? "Completed" : "Pending")
        );
    }
}