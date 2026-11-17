package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
public class Quote {

    private int id;
    private Customer customer;
    private Event event;

    private ArrayList<Assignment> assignments
            = new ArrayList<>();

    private LocalDate issueDate;
    private boolean approved;

    public Quote(
            int id,
            Customer customer,
            Event event) {

        setId(id);
        setCustomer(customer);
        setEvent(event);

        issueDate = LocalDate.now();
        approved = false;
    }

    public boolean addAssignment(
            Assignment assignment) {

        if (assignment == null) {
            return false;
        }

        if (findAssignmentById(
                assignment.getId()) != null) {

            return false;
        }

        if (findAssignmentByEquipmentId(
                assignment.getEquipment().getId()) != null) {

            return false;
        }

        assignments.add(assignment);
        return true;
    }

    public Assignment findAssignmentById(int id) {

        for (Assignment assignment : assignments) {

            if (assignment.getId() == id) {
                return assignment;
            }
        }

        return null;
    }

    public Assignment findAssignmentByEquipmentId(
            int equipmentId) {

        for (Assignment assignment : assignments) {

            if (assignment.getEquipment().getId()
                    == equipmentId) {

                return assignment;
            }
        }

        return null;
    }

    public boolean removeAssignment(int assignmentId) {

        Assignment assignment
                = findAssignmentById(assignmentId);

        if (assignment == null) {
            return false;
        }

        return assignments.remove(assignment);
    }

    public int calculateRentalDays() {

        if (event == null
                || !event.hasValidDates()) {

            return 0;
        }

        long days = ChronoUnit.DAYS.between(
                event.getStartDate(),
                event.getEndDate()
        );

        return (int) days + 1;
    }

    public double calculateTotal() {

        int rentalDays = calculateRentalDays();
        double total = 0.0;

        for (Assignment assignment : assignments) {
            total += assignment.calculateSubtotal(
                    rentalDays
            );
        }

        return total;
    }

    public boolean approve() {

        if (assignments.isEmpty()
                || calculateTotal() <= 0) {

            return false;
        }

        approved = true;
        return true;
    }

    public void setId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "The quote ID cannot be negative.");
        }

        this.id = id;
    }

    public void setCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "The customer cannot be empty.");
        }

        this.customer = customer;
    }

    public void setEvent(Event event) {

        if (event == null) {
            throw new IllegalArgumentException(
                    "The event cannot be empty.");
        }

        this.event = event;
    }

    public void setAssignments(
            ArrayList<Assignment> assignments) {

        if (assignments == null) {
            this.assignments = new ArrayList<>();
        } else {
            this.assignments = assignments;
        }
    }
}