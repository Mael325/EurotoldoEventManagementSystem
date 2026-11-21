package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.EventController;
import ec.edu.espe.eurotoldo.model.Event;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
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

        int id = input.readInt("ID del evento: ");

        if (id <= 0) {
            System.out.println(
                    "El ID debe ser mayor que cero.");
            return;
        }

        if (controller.findEventById(id) != null) {
            System.out.println(
                    "Ya existe un evento con ese ID.");
            return;
        }

        try {
            Event event = readEventData(id);

            if (controller.addEvent(event)) {
                System.out.println(
                        "Evento registrado correctamente.");
            } else {
                System.out.println(
                        "No se pudo registrar el evento. "
                        + "Verifique los datos y la conexión.");
            }

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void updateEvent() {

        int id = input.readInt("ID del evento: ");

        if (controller.findEventById(id) == null) {
            System.out.println(
                    "No se encontró el evento.");
            return;
        }

        try {
            Event event = readEventData(id);

            if (controller.updateEvent(event)) {
                System.out.println(
                        "Evento actualizado correctamente.");
            } else {
                System.out.println(
                        "No se pudo actualizar el evento. "
                        + "Verifique los datos y la conexión.");
            }

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void findEvent() {

        int id = input.readInt("ID del evento: ");

        Event event = controller.findEventById(id);

        if (event == null) {
            System.out.println(
                    "No se encontró el evento.");
            return;
        }

        showEventInformation(event);
    }

    public void removeEvent() {

        int id = input.readInt("ID del evento: ");

        if (controller.findEventById(id) == null) {
            System.out.println(
                    "No se encontró el evento.");
            return;
        }

        if (controller.removeEvent(id)) {
            System.out.println(
                    "Evento eliminado correctamente.");
        } else {
            System.out.println(
                    "No se pudo eliminar el evento. "
                    + "Verifique la conexión con la API.");
        }
    }

    public void markSetupCompleted() {

        int id = input.readInt("ID del evento: ");

        if (controller.findEventById(id) == null) {
            System.out.println(
                    "No se encontró el evento.");
            return;
        }

        if (controller.markSetupCompleted(id)) {
            System.out.println(
                    "Montaje marcado como completado.");
        } else {
            System.out.println(
                    "No se pudo actualizar el montaje. "
                    + "Verifique la conexión con la API.");
        }
    }

    public void markPickupCompleted() {

        int id = input.readInt("ID del evento: ");

        Event event = controller.findEventById(id);

        if (event == null) {
            System.out.println(
                    "No se encontró el evento.");
            return;
        }

        if (!event.isSetupCompleted()) {
            System.out.println(
                    "Primero debe completarse el montaje.");
            return;
        }

        if (controller.markPickupCompleted(id)) {
            System.out.println(
                    "Retiro marcado como completado.");
        } else {
            System.out.println(
                    "No se pudo actualizar el retiro. "
                    + "Verifique la conexión con la API.");
        }
    }

    public void showEvents() {

        if (controller.getEvents().isEmpty()) {
            System.out.println(
                    "No existen eventos registrados.");
            return;
        }

        String border = "+------+"
                + "--------------------+"
                + "----------------+"
                + "--------------------+"
                + "------------+"
                + "------------+";

        System.out.println("\nEVENTOS REGISTRADOS");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-18s | %-14s | %-18s "
                + "| %-10s | %-10s |%n",
                "ID",
                "Nombre",
                "Tipo",
                "Lugar",
                "Inicio",
                "Fin"
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
                "Nombre del evento: ");

        String eventType = input.readRequiredText(
                "Tipo de evento: ");

        String location = input.readRequiredText(
                "Lugar del evento: ");

        LocalDate startDate = input.readDate(
                "Fecha de inicio (AAAA-MM-DD): ");

        LocalDate endDate = input.readDate(
                "Fecha de fin (AAAA-MM-DD): ");

        LocalDateTime setupDateTime
                = input.readDateTime(
                        "Fecha y hora del montaje "
                        + "(AAAA-MM-DD HH:MM): "
                );

        LocalDateTime pickupDateTime
                = input.readDateTime(
                        "Fecha y hora del retiro "
                        + "(AAAA-MM-DD HH:MM): "
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

        System.out.println("\nINFORMACIÓN DEL EVENTO");
        System.out.println("ID: " + event.getId());
        System.out.println(
                "Nombre: " + event.getName());
        System.out.println(
                "Tipo: " + event.getEventType());
        System.out.println(
                "Lugar: " + event.getLocation());
        System.out.println(
                "Fecha de inicio: "
                + event.getStartDate());
        System.out.println(
                "Fecha de fin: "
                + event.getEndDate());
        System.out.println(
                "Montaje: "
                + event.getSetupDateTime());
        System.out.println(
                "Retiro: "
                + event.getPickupDateTime());

        System.out.println(
                "Estado del montaje: "
                + (event.isSetupCompleted()
                        ? "Completado" : "Pendiente")
        );

        System.out.println(
                "Estado del retiro: "
                + (event.isPickupCompleted()
                        ? "Completado" : "Pendiente")
        );
    }
}