package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@NoArgsConstructor
@ToString
public class Event {

    private int id;
    private String name;
    private String eventType;
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime setupDateTime;
    private LocalDateTime pickupDateTime;

    private boolean setupCompleted;
    private boolean pickupCompleted;

    // Maintains compatibility with previous events.
    public Event(int id, String name, String location,
            LocalDate startDate, LocalDate endDate) {

        this(
                id,
                name,
                "Other",
                location,
                startDate,
                endDate,
                null,
                null
        );
    }

    public Event(int id, String name, String eventType,
            String location, LocalDate startDate,
            LocalDate endDate,
            LocalDateTime setupDateTime,
            LocalDateTime pickupDateTime) {

        setId(id);
        setName(name);
        setEventType(eventType);
        setLocation(location);
        setStartDate(startDate);
        setEndDate(endDate);

        this.setupDateTime = setupDateTime;
        this.pickupDateTime = pickupDateTime;

        setupCompleted = false;
        pickupCompleted = false;

        if (!hasValidDates()) {
            throw new IllegalArgumentException(
                    "The event dates are invalid.");
        }

        if (!hasValidSchedule()) {
            throw new IllegalArgumentException(
                    "The setup or pickup schedule is invalid.");
        }
    }

    public boolean hasValidDates() {

        if (startDate == null || endDate == null) {
            return false;
        }

        return !endDate.isBefore(startDate);
    }

    public boolean hasValidSchedule() {

        if (setupDateTime == null
                && pickupDateTime == null) {
            return true;
        }

        if (setupDateTime == null
                || pickupDateTime == null) {
            return false;
        }

        if (setupDateTime.isAfter(pickupDateTime)) {
            return false;
        }

        if (setupDateTime.toLocalDate()
                .isAfter(startDate)) {
            return false;
        }

        return !pickupDateTime.toLocalDate()
                .isBefore(endDate);
    }

    public void markSetupCompleted() {
        setupCompleted = true;
    }

    public void markPickupCompleted() {

        if (!setupCompleted) {
            throw new IllegalStateException(
                    "The setup must be completed first.");
        }

        pickupCompleted = true;
    }

    public void setId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "The event ID cannot be negative.");
        }

        this.id = id;
    }

    public void setName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "The event name cannot be empty.");
        }

        this.name = name;
    }

    public void setEventType(String eventType) {

        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException(
                    "The event type cannot be empty.");
        }

        this.eventType = eventType;
    }

    public void setLocation(String location) {

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException(
                    "The event location cannot be empty.");
        }

        this.location = location;
    }

    public void setStartDate(LocalDate startDate) {

        if (startDate == null) {
            throw new IllegalArgumentException(
                    "The start date cannot be empty.");
        }

        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {

        if (endDate == null) {
            throw new IllegalArgumentException(
                    "The end date cannot be empty.");
        }

        this.endDate = endDate;
    }

    public void setSetupDateTime(
            LocalDateTime setupDateTime) {

        this.setupDateTime = setupDateTime;
    }

    public void setPickupDateTime(
            LocalDateTime pickupDateTime) {

        this.pickupDateTime = pickupDateTime;
    }
}