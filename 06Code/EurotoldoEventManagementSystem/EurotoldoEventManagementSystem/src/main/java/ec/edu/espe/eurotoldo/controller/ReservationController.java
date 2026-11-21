package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Event;
import ec.edu.espe.eurotoldo.model.Quote;
import ec.edu.espe.eurotoldo.model.Reservation;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class ReservationController {

    private final ArrayList<Reservation> reservations;
    private final ReservationApiClient apiClient;

    private final QuoteController quoteController;
    private final EquipmentController equipmentController;

    public ReservationController(
            QuoteController quoteController,
            EquipmentController equipmentController) {

        reservations = new ArrayList<>();
        apiClient = new ReservationApiClient();

        this.quoteController = quoteController;
        this.equipmentController = equipmentController;
    }

    public boolean loadReservations() {

        ArrayList<Reservation> loadedReservations
                = apiClient.getReservations();

        if (loadedReservations == null) {
            return false;
        }

        reservations.clear();

        for (Reservation reservation
                : loadedReservations) {

            if (!isValid(reservation)
                    || findReservationById(
                            reservation.getId()) != null) {

                reservations.clear();
                return false;
            }

            reservations.add(reservation);
        }

        return true;
    }

    public boolean createReservationFromQuote(
            int quoteId) {

        if (quoteId <= 0
                || findReservationById(quoteId) != null) {
            return false;
        }

        Quote quote = quoteController.findQuoteById(
                quoteId
        );

        if (quote == null || !quote.isApproved()) {
            return false;
        }

        for (Assignment assignment
                : quote.getAssignments()) {

            int equipmentId
                    = assignment.getEquipment().getId();

            if (!isEquipmentAvailableForEvent(
                    equipmentId,
                    assignment.getQuantity(),
                    quote.getEvent())) {

                return false;
            }
        }

        try {
            Reservation reservation = new Reservation(
                    quoteId,
                    quote
            );

            if (!apiClient.addReservation(reservation)) {
                return false;
            }

            reservations.add(reservation);
            return true;

        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public Reservation findReservationById(int id) {

        for (Reservation reservation : reservations) {

            if (reservation.getId() == id) {
                return reservation;
            }
        }

        return null;
    }

    public boolean isEquipmentAvailableForEvent(
            int equipmentId,
            int requestedQuantity,
            Event event) {

        Equipment equipment
                = equipmentController.findEquipmentById(
                        equipmentId
                );

        if (equipment == null
                || event == null
                || requestedQuantity <= 0) {

            return false;
        }

        int reservedQuantity = getReservedQuantity(
                equipmentId,
                event
        );

        int availableQuantity
                = equipment.getTotalQuantity()
                - reservedQuantity;

        return requestedQuantity <= availableQuantity;
    }

    public int getReservedQuantity(
            int equipmentId,
            Event requestedEvent) {

        int reservedQuantity = 0;

        for (Reservation reservation : reservations) {

            if (Reservation.CANCELLED.equals(
                    reservation.getStatus())
                    || Reservation.COMPLETED.equals(
                            reservation.getStatus())) {

                continue;
            }

            if (!datesOverlap(
                    reservation.getEvent(),
                    requestedEvent)) {

                continue;
            }

            for (Assignment assignment
                    : reservation.getAssignments()) {

                if (assignment.getEquipment().getId()
                        == equipmentId) {

                    reservedQuantity
                            += assignment.getQuantity();
                }
            }
        }

        return reservedQuantity;
    }

    public boolean cancelReservation(int id) {

        Reservation reservation
                = findReservationById(id);

        if (reservation == null
                || Reservation.CANCELLED.equals(
                        reservation.getStatus())
                || Reservation.COMPLETED.equals(
                        reservation.getStatus())) {

            return false;
        }

        if (!apiClient.cancelReservation(id)) {
            return false;
        }

        return reservation.cancel();
    }

    public boolean completeReservation(int id) {

        Reservation reservation
                = findReservationById(id);

        if (reservation == null
                || !Reservation.CONFIRMED.equals(
                        reservation.getStatus())) {

            return false;
        }

        if (!apiClient.completeReservation(id)) {
            return false;
        }

        return reservation.complete();
    }

    public boolean removeReservation(int id) {

        Reservation reservation
                = findReservationById(id);

        if (reservation == null
                || !Reservation.CANCELLED.equals(
                        reservation.getStatus())) {

            return false;
        }

        if (!apiClient.deleteReservation(id)) {
            return false;
        }

        return reservations.remove(reservation);
    }

    public ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    /*
     * Se conserva temporalmente para mantener compatibilidad
     * con las vistas anteriores. La API guarda cada cambio.
     */
    public boolean saveReservations() {
        return true;
    }

    private boolean datesOverlap(
            Event firstEvent,
            Event secondEvent) {

        if (firstEvent == null
                || secondEvent == null
                || !firstEvent.hasValidDates()
                || !secondEvent.hasValidDates()) {

            return false;
        }

        return !firstEvent.getEndDate().isBefore(
                secondEvent.getStartDate()
        ) && !secondEvent.getEndDate().isBefore(
                firstEvent.getStartDate()
        );
    }

    private boolean isValid(
            Reservation reservation) {

        if (reservation == null
                || reservation.getId() <= 0
                || reservation.getCustomer() == null
                || reservation.getEvent() == null
                || !reservation.getEvent().hasValidDates()
                || reservation.getReservationDate() == null
                || reservation.getAssignments() == null
                || reservation.getAssignments().isEmpty()
                || reservation.getTotalAmount() <= 0) {

            return false;
        }

        String status = reservation.getStatus();

        return Reservation.PENDING.equals(status)
                || Reservation.CONFIRMED.equals(status)
                || Reservation.CANCELLED.equals(status)
                || Reservation.COMPLETED.equals(status);
    }
}