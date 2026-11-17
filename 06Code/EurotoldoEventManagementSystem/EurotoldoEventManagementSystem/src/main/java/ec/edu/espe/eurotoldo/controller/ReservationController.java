package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Event;
import ec.edu.espe.eurotoldo.model.Quote;
import ec.edu.espe.eurotoldo.model.Reservation;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class ReservationController {

    private final ArrayList<Reservation> reservations;
    private final ReservationFileManager fileManager;

    private final QuoteController quoteController;
    private final EquipmentController equipmentController;

    public ReservationController(
            QuoteController quoteController,
            EquipmentController equipmentController) {

        reservations = new ArrayList<>();
        fileManager = new ReservationFileManager();

        this.quoteController = quoteController;
        this.equipmentController = equipmentController;
    }

    public boolean loadReservations() {

        ArrayList<Reservation> loadedReservations
                = fileManager.loadReservations();

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

        if (findReservationById(quoteId) != null) {
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
            Reservation reservation
                    = new Reservation(
                            quoteId,
                            quote
                    );

            reservations.add(reservation);
            return true;

        } catch (IllegalArgumentException e) {
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

                    reservedQuantity +=
                            assignment.getQuantity();
                }
            }
        }

        return reservedQuantity;
    }

    public boolean cancelReservation(int id) {

        Reservation reservation
                = findReservationById(id);

        if (reservation == null) {
            return false;
        }

        return reservation.cancel();
    }

    public boolean completeReservation(int id) {

        Reservation reservation
                = findReservationById(id);

        if (reservation == null) {
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

        return reservations.remove(reservation);
    }

    public ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public boolean saveReservations() {

        return fileManager.saveReservations(
                reservations
        );
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

        return reservation != null
                && reservation.getId() >= 0
                && reservation.getCustomer() != null
                && reservation.getEvent() != null
                && reservation.getEvent().hasValidDates()
                && reservation.getStatus() != null
                && !reservation.getStatus().isBlank()
                && reservation.getReservationDate() != null
                && reservation.getAssignments() != null
                && !reservation.getAssignments().isEmpty()
                && reservation.getTotalAmount() > 0;
    }
}