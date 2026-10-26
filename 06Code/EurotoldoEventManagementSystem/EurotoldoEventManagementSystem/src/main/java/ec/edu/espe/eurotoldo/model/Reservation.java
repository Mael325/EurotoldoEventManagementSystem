package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
class Reservation {
    
    private int id;
    private Customer customer;
    private Event event;
    private String status;
    private LocalDate reservationDate;
    private List<Assignment> assignments;

    public Reservation(int id, Customer customer, Event event, String status, LocalDate reservationDate, List<Assignment> assignments) {
        this.id = id;
        this.customer = customer;
        this.event = event;
        this.status = status;
        this.reservationDate = reservationDate;
        this.assignments = assignments;
    }

    @Override
    public String toString() {
        return "Reservation{" + "id=" + getId() + ", customer=" + getCustomer() + ", event=" + getEvent() + ", status=" + getStatus() + ", reservationDate=" + getReservationDate() + ", assignments=" + getAssignments() + '}';
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the reservationDate
     */
    public LocalDate getReservationDate() {
        return reservationDate;
    }

    /**
     * @param reservationDate the reservationDate to set
     */
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * @return the assignments
     */
    public List<Assignment> getAssignments() {
        return assignments;
    }

    /**
     * @param assignments the assignments to set
     */
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }
    
    
}
