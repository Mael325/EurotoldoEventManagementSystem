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
public class Reservation {

    public static final String PENDING = "PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String CANCELLED = "CANCELLED";
    public static final String COMPLETED = "COMPLETED";

    private int id;
    private Customer customer;
    private Event event;
    private String status;
    private LocalDate reservationDate;
    private ArrayList<Assignment> assignments
            = new ArrayList<>();

    private double totalAmount;

    public Reservation(
            int id,
            Customer customer,
            Event event,
            String status,
            LocalDate reservationDate,
            ArrayList<Assignment> assignments) {

        setId(id);
        setCustomer(customer);
        setEvent(event);
        setStatus(status);
        setReservationDate(reservationDate);
        setAssignments(assignments);

        totalAmount = calculateTotal();
    }

    public Reservation(int id, Quote quote) {

        if (quote == null || !quote.isApproved()) {
            throw new IllegalArgumentException(
                    "The quote must be approved.");
        }

        setId(id);
        setCustomer(quote.getCustomer());
        setEvent(quote.getEvent());
        setStatus(CONFIRMED);
        setReservationDate(LocalDate.now());

        setAssignments(
                new ArrayList<>(
                        quote.getAssignments()
                )
        );

        setTotalAmount(
                quote.calculateTotal()
        );
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

    public boolean cancel() {

        if (CANCELLED.equals(status)
                || COMPLETED.equals(status)) {

            return false;
        }

        status = CANCELLED;
        return true;
    }

    public boolean complete() {

        if (!CONFIRMED.equals(status)) {
            return false;
        }

        status = COMPLETED;
        return true;
    }

    public void setId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "The reservation ID cannot be negative.");
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

    public void setStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "The reservation status cannot be empty.");
        }

        this.status = status;
    }

    public void setReservationDate(
            LocalDate reservationDate) {

        if (reservationDate == null) {
            throw new IllegalArgumentException(
                    "The reservation date cannot be empty.");
        }

        this.reservationDate = reservationDate;
    }

    public void setAssignments(
            ArrayList<Assignment> assignments) {

        if (assignments == null
                || assignments.contains(null)) {

            throw new IllegalArgumentException(
                    "The assignments are invalid.");
        }

        this.assignments = assignments;
    }

    public void setTotalAmount(double totalAmount) {

        if (totalAmount < 0) {
            throw new IllegalArgumentException(
                    "The total amount cannot be negative.");
        }

        this.totalAmount = totalAmount;
    }
}