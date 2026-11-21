package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.QuoteController;
import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Quote;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class QuoteView {

    private final QuoteController controller;
    private final ConsoleInput input;

    public QuoteView(
            QuoteController controller,
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
                    "|          GESTIÓN DE COTIZACIONES         |");
            System.out.println(
                    "+------------------------------------------+");
            System.out.println(
                    "|  [1] Crear cotización                    |");
            System.out.println(
                    "|  [2] Agregar equipo                      |");
            System.out.println(
                    "|  [3] Ver cotizaciones                    |");
            System.out.println(
                    "|  [4] Buscar cotización                   |");
            System.out.println(
                    "|  [5] Eliminar equipo                     |");
            System.out.println(
                    "|  [6] Aprobar cotización                  |");
            System.out.println(
                    "|  [7] Eliminar cotización                 |");
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
                createQuote();
                break;

            case 2:
                addEquipment();
                break;

            case 3:
                showQuotes();
                break;

            case 4:
                findQuote();
                break;

            case 5:
                removeEquipment();
                break;

            case 6:
                approveQuote();
                break;

            case 7:
                removeQuote();
                break;

            case 0:
                break;

            default:
                System.out.println(
                        "Opción inválida.");
                break;
        }
    }

    private void createQuote() {

        int quoteId = input.readInt(
                "Código de la cotización: ");

        int customerId = input.readInt(
                "Código del cliente: ");

        int eventId = input.readInt(
                "Código del evento: ");

        if (!controller.createQuote(
                quoteId,
                customerId,
                eventId)) {

            System.out.println(
                    "No se pudo crear la cotización.");
            System.out.println(
                    "Revise los códigos ingresados.");
            return;
        }

        System.out.println(
                "Cotización creada correctamente.");
    }

    private void addEquipment() {

        int quoteId = input.readInt(
                "Código de la cotización: ");

        int assignmentId = input.readInt(
                "Código del detalle: ");

        int equipmentId = input.readInt(
                "Código del equipo: ");

        int quantity = input.readInt(
                "Cantidad solicitada: ");

        if (!controller.addAssignment(
                quoteId,
                assignmentId,
                equipmentId,
                quantity)) {

            System.out.println(
                    "No se pudo agregar el equipo.");
            System.out.println(
                    "Revise la disponibilidad y los códigos.");
            return;
        }

        System.out.println(
                "Equipo agregado correctamente.");
    }

    private void removeEquipment() {

        int quoteId = input.readInt(
                "Código de la cotización: ");

        int assignmentId = input.readInt(
                "Código del detalle: ");

        if (!controller.removeAssignment(
                quoteId,
                assignmentId)) {

            System.out.println(
                    "No se pudo eliminar el equipo.");
            return;
        }

        System.out.println(
                "Equipo eliminado correctamente.");
    }

    private void approveQuote() {

        int quoteId = input.readInt(
                "Código de la cotización: ");

        if (!controller.approveQuote(quoteId)) {

            System.out.println(
                    "No se pudo aprobar la cotización.");
            System.out.println(
                    "Compruebe sus equipos y disponibilidad.");
            return;
        }

        System.out.println(
                "Cotización aprobada correctamente.");
    }

    private void removeQuote() {

        int quoteId = input.readInt(
                "Código de la cotización: ");

        if (!controller.removeQuote(quoteId)) {

            System.out.println(
                    "No se pudo eliminar la cotización.");
            return;
        }

        System.out.println(
                "Cotización eliminada correctamente.");
    }

    private void showQuotes() {

        if (controller.getQuotes().isEmpty()) {

            System.out.println(
                    "No existen cotizaciones registradas.");
            return;
        }

        String border = "+------+"
                + "----------------------+"
                + "----------------------+"
                + "-------+"
                + "------------+"
                + "------------+";

        System.out.println("\nCOTIZACIONES");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-20s | %-20s | %-5s "
                + "| %-10s | %-10s |%n",
                "ID",
                "Cliente",
                "Evento",
                "Días",
                "Total",
                "Estado"
        );

        System.out.println(border);

        for (Quote quote : controller.getQuotes()) {

            System.out.printf(
                    "| %-4d | %-20s | %-20s | %-5d "
                    + "| $%-9.2f | %-10s |%n",
                    quote.getId(),
                    quote.getCustomer().getName(),
                    quote.getEvent().getName(),
                    quote.calculateRentalDays(),
                    quote.calculateTotal(),
                    quote.isApproved()
                            ? "Aprobada"
                            : "Pendiente"
            );
        }

        System.out.println(border);
    }

    private void findQuote() {

        int quoteId = input.readInt(
                "Código de la cotización: ");

        Quote quote = controller.findQuoteById(
                quoteId
        );

        if (quote == null) {

            System.out.println(
                    "La cotización no fue encontrada.");
            return;
        }

        showQuoteInformation(quote);
    }

    private void showQuoteInformation(
            Quote quote) {

        System.out.println(
                "\nINFORMACIÓN DE LA COTIZACIÓN");

        System.out.println(
                "Código: " + quote.getId());

        System.out.println(
                "Cliente: "
                + quote.getCustomer().getName());

        System.out.println(
                "Evento: "
                + quote.getEvent().getName());

        System.out.println(
                "Fecha de emisión: "
                + quote.getIssueDate());

        System.out.println(
                "Días de alquiler: "
                + quote.calculateRentalDays());

        System.out.println(
                "Estado: "
                + (quote.isApproved()
                        ? "Aprobada"
                        : "Pendiente"));

        showAssignments(quote);

        System.out.printf(
                "TOTAL: $%.2f%n",
                quote.calculateTotal()
        );
    }

    private void showAssignments(
            Quote quote) {

        if (quote.getAssignments().isEmpty()) {

            System.out.println(
                    "La cotización no tiene equipos.");
            return;
        }

        int rentalDays
                = quote.calculateRentalDays();

        String border = "+------+"
                + "----------------------+"
                + "----------+"
                + "------------+"
                + "------------+";

        System.out.println(
                "\nEQUIPOS SOLICITADOS");

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
                : quote.getAssignments()) {

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
}