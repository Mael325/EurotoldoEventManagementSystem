package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.ReservationController;
import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Reservation;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class ReservationView {

    private final ReservationController controller;
    private final ConsoleInput input;

    public ReservationView(
            ReservationController controller,
            ConsoleInput input) {

        this.controller = controller;
        this.input = input;
    }

    public void showMenu() {

        int option;

        do {
            System.out.println(
                    "\n+------------------------------------------+");
            System.out.println(
                    "|            GESTIÓN DE RESERVAS           |");
            System.out.println(
                    "+------------------------------------------+");
            System.out.println(
                    "|  [1] Crear desde una cotización          |");
            System.out.println(
                    "|  [2] Ver reservas                        |");
            System.out.println(
                    "|  [3] Buscar reserva                      |");
            System.out.println(
                    "|  [4] Cancelar reserva                    |");
            System.out.println(
                    "|  [5] Completar reserva                   |");
            System.out.println(
                    "|  [6] Eliminar reserva cancelada          |");
            System.out.println(
                    "|  [0] Volver al menú principal            |");
            System.out.println(
                    "+------------------------------------------+");

            option = input.readInt(
                    "Seleccione una opción: ");

            executeOption(option);

        } while (option != 0);
    }

    private void executeOption(int option) {

        switch (option) {

            case 1:
                createReservation();
                break;

            case 2:
                showReservations();
                break;

            case 3:
                findReservation();
                break;

            case 4:
                cancelReservation();
                break;

            case 5:
                completeReservation();
                break;

            case 6:
                removeReservation();
                break;

            case 0:
                break;

            default:
                System.out.println(
                        "Opción inválida.");
                break;
        }
    }

    private void createReservation() {

        int quoteId = input.readInt(
                "Código de la cotización aprobada: ");

        if (quoteId <= 0) {
            System.out.println(
                    "El código debe ser mayor que cero.");
            return;
        }

        if (!controller.createReservationFromQuote(
                quoteId)) {

            System.out.println(
                    "No se pudo crear la reserva.");

            System.out.println(
                    "Compruebe que la cotización esté "
                    + "aprobada, que exista disponibilidad "
                    + "y que la API esté funcionando.");

            return;
        }

        System.out.println(
                "Reserva creada correctamente.");
    }

    private void showReservations() {

        if (controller.getReservations().isEmpty()) {
            System.out.println(
                    "No existen reservas registradas.");
            return;
        }

        String border = "+------+"
                + "----------------------+"
                + "----------------------+"
                + "------------+"
                + "------------+"
                + "------------+";

        System.out.println("\nRESERVAS");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-20s | %-20s "
                + "| %-10s | %-10s | %-10s |%n",
                "ID",
                "Cliente",
                "Evento",
                "Inicio",
                "Total",
                "Estado"
        );

        System.out.println(border);

        for (Reservation reservation
                : controller.getReservations()) {

            System.out.printf(
                    "| %-4d | %-20s | %-20s "
                    + "| %-10s | $%-9.2f | %-10s |%n",
                    reservation.getId(),
                    reservation.getCustomer().getName(),
                    reservation.getEvent().getName(),
                    reservation.getEvent().getStartDate(),
                    reservation.getTotalAmount(),
                    translateStatus(
                            reservation.getStatus()
                    )
            );
        }

        System.out.println(border);
    }

    private void findReservation() {

        int reservationId = input.readInt(
                "Código de la reserva: ");

        Reservation reservation
                = controller.findReservationById(
                        reservationId
                );

        if (reservation == null) {
            System.out.println(
                    "La reserva no fue encontrada.");
            return;
        }

        showReservationInformation(reservation);
    }

    private void cancelReservation() {

        int reservationId = input.readInt(
                "Código de la reserva: ");

        Reservation reservation
                = controller.findReservationById(
                        reservationId
                );

        if (reservation == null) {
            System.out.println(
                    "La reserva no fue encontrada.");
            return;
        }

        if (Reservation.CANCELLED.equals(
                reservation.getStatus())
                || Reservation.COMPLETED.equals(
                        reservation.getStatus())) {

            System.out.println(
                    "La reserva ya no puede cancelarse.");
            return;
        }

        if (!controller.cancelReservation(
                reservationId)) {

            System.out.println(
                    "No se pudo cancelar la reserva. "
                    + "Verifique la conexión con la API.");

            return;
        }

        System.out.println(
                "Reserva cancelada correctamente.");
    }

    private void completeReservation() {

        int reservationId = input.readInt(
                "Código de la reserva: ");

        Reservation reservation
                = controller.findReservationById(
                        reservationId
                );

        if (reservation == null) {
            System.out.println(
                    "La reserva no fue encontrada.");
            return;
        }

        if (!Reservation.CONFIRMED.equals(
                reservation.getStatus())) {

            System.out.println(
                    "Solo puede completarse una "
                    + "reserva confirmada.");

            return;
        }

        if (!controller.completeReservation(
                reservationId)) {

            System.out.println(
                    "No se pudo completar la reserva. "
                    + "Verifique la conexión con la API.");

            return;
        }

        System.out.println(
                "Reserva completada correctamente.");
    }

    private void removeReservation() {

        int reservationId = input.readInt(
                "Código de la reserva: ");

        Reservation reservation
                = controller.findReservationById(
                        reservationId
                );

        if (reservation == null) {
            System.out.println(
                    "La reserva no fue encontrada.");
            return;
        }

        if (!Reservation.CANCELLED.equals(
                reservation.getStatus())) {

            System.out.println(
                    "Solo pueden eliminarse "
                    + "reservas canceladas.");

            return;
        }

        if (!controller.removeReservation(
                reservationId)) {

            System.out.println(
                    "No se pudo eliminar la reserva. "
                    + "Verifique la conexión con la API.");

            return;
        }

        System.out.println(
                "Reserva eliminada correctamente.");
    }

    private void showReservationInformation(
            Reservation reservation) {

        System.out.println(
                "\nINFORMACIÓN DE LA RESERVA");

        System.out.println(
                "Código: " + reservation.getId());

        System.out.println(
                "Cliente: "
                + reservation.getCustomer().getName());

        System.out.println(
                "Evento: "
                + reservation.getEvent().getName());

        System.out.println(
                "Lugar: "
                + reservation.getEvent().getLocation());

        System.out.println(
                "Fecha inicial: "
                + reservation.getEvent().getStartDate());

        System.out.println(
                "Fecha final: "
                + reservation.getEvent().getEndDate());

        System.out.println(
                "Fecha de reserva: "
                + reservation.getReservationDate());

        System.out.println(
                "Estado: "
                + translateStatus(
                        reservation.getStatus()
                ));

        showAssignments(reservation);

        System.out.printf(
                "TOTAL: $%.2f%n",
                reservation.getTotalAmount()
        );
    }

    private void showAssignments(
            Reservation reservation) {

        int rentalDays
                = reservation.calculateRentalDays();

        String border = "+------+"
                + "----------------------+"
                + "----------+"
                + "------------+"
                + "------------+";

        System.out.println("\nEQUIPOS RESERVADOS");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-20s | %-8s "
                + "| %-10s | %-10s |%n",
                "ID",
                "Equipo",
                "Cantidad",
                "Precio",
                "Subtotal"
        );

        System.out.println(border);

        for (Assignment assignment
                : reservation.getAssignments()) {

            System.out.printf(
                    "| %-4d | %-20s | %-8d "
                    + "| $%-9.2f | $%-9.2f |%n",
                    assignment.getId(),
                    assignment.getEquipment().getName(),
                    assignment.getQuantity(),
                    assignment.getEquipment()
                            .getRentalPrice(),
                    assignment.calculateSubtotal(
                            rentalDays
                    )
            );
        }

        System.out.println(border);
    }

    private String translateStatus(String status) {

        if (Reservation.CONFIRMED.equals(status)) {
            return "Confirmada";
        }

        if (Reservation.CANCELLED.equals(status)) {
            return "Cancelada";
        }

        if (Reservation.COMPLETED.equals(status)) {
            return "Completada";
        }

        return "Pendiente";
    }
}