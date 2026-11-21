package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.PaymentController;
import ec.edu.espe.eurotoldo.model.Payment;
import ec.edu.espe.eurotoldo.model.Reservation;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentView {

    private final PaymentController paymentController;
    private final ConsoleInput input;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PaymentView(PaymentController paymentController, ConsoleInput input) {
        this.paymentController = paymentController;
        this.input = input;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n+------------------------------------------+");
            System.out.println("|             GESTIÓN DE PAGOS             |");
            System.out.println("+------------------------------------------+");
            System.out.println("|  [1] Registrar pago en efectivo          |");
            System.out.println("|  [2] Registrar transferencia             |");
            System.out.println("|  [3] Ver todos los pagos                 |");
            System.out.println("|  [4] Ver pagos de una reserva            |");
            System.out.println("|  [5] Consultar saldo pendiente           |");
            System.out.println("|  [6] Eliminar pago incorrecto            |");
            System.out.println("|  [0] Volver al menú principal            |");
            System.out.println("+------------------------------------------+");

            option = input.readInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> registerCashPayment();
                case 2 -> registerTransferPayment();
                case 3 -> showAllPayments();
                case 4 -> showPaymentsByReservation();
                case 5 -> checkPendingBalance();
                case 6 -> removeIncorrectPayment();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida. Inténtelo nuevamente.");
            }
        } while (option != 0);
    }

    private void registerCashPayment() {
        System.out.println("\n--- REGISTRAR PAGO EN EFECTIVO ---");
        int paymentId = input.readInt("Código del pago: ");
        int reservationId = input.readInt("Código de reserva: ");
        double amount = input.readDouble("Valor a pagar ($): ");
        String receivedBy = input.readRequiredText("Recibido por: ");

        if (paymentController.addCashPayment(paymentId, reservationId, amount, receivedBy)) {
            System.out.println("Pago en efectivo registrado correctamente.");
            printBalanceSummary(reservationId);
        }
    }

    private void registerTransferPayment() {
        System.out.println("\n--- REGISTRAR PAGO POR TRANSFERENCIA ---");
        int paymentId = input.readInt("Código del pago: ");
        int reservationId = input.readInt("Código de reserva: ");
        double amount = input.readDouble("Valor a pagar ($): ");
        String bankName = input.readRequiredText("Nombre del banco: ");
        String transactionNumber = input.readRequiredText("Número de transacción: ");

        if (paymentController.addTransferPayment(paymentId, reservationId, amount, bankName, transactionNumber)) {
            System.out.println("Pago por transferencia registrado correctamente.");
            printBalanceSummary(reservationId);
        }
    }

    private void showAllPayments() {
        System.out.println("\n--- TODOS LOS PAGOS REGISTRADOS ---");
        ArrayList<Payment> payments = paymentController.getPayments();
        printPaymentsTable(payments);
    }

    private void showPaymentsByReservation() {
        System.out.println("\n--- PAGOS POR RESERVA ---");
        int reservationId = input.readInt("Ingrese el código de reserva: ");
        Reservation reservation = paymentController.findReservation(reservationId);
        if (reservation == null) {
            System.out.println("No existe una reserva con el código " + reservationId + ".");
            return;
        }

        ArrayList<Payment> reservationPayments = paymentController.getPaymentsByReservation(reservationId);
        printPaymentsTable(reservationPayments);
        printBalanceSummary(reservationId);
    }

    private void checkPendingBalance() {
        System.out.println("\n--- CONSULTAR SALDO PENDIENTE ---");
        int reservationId = input.readInt("Ingrese el código de reserva: ");
        Reservation reservation = paymentController.findReservation(reservationId);
        if (reservation == null) {
            System.out.println("No existe una reserva con el código " + reservationId + ".");
            return;
        }
        printBalanceSummary(reservationId);
    }

    private void removeIncorrectPayment() {
        System.out.println("\n--- ELIMINAR PAGO INCORRECTO ---");
        int paymentId = input.readInt("Ingrese el código del pago a eliminar: ");
        Payment payment = paymentController.findPaymentById(paymentId);
        if (payment == null) {
            System.out.println("No se encontró ningún pago con ese código.");
            return;
        }

        System.out.println("Pago encontrado: " + payment);
        String confirm = input.readRequiredText("¿Confirma que desea eliminarlo? (S/N): ");
        if (confirm.equalsIgnoreCase("S")) {
            if (paymentController.removePayment(paymentId)) {
                System.out.println("Pago eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar el pago.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void printPaymentsTable(ArrayList<Payment> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No hay pagos registrados para mostrar.");
            return;
        }

        System.out.printf("%-10s | %-12s | %-12s | %-12s | %-18s%n",
                "ID PAGO", "ID RESERVA", "MÉTODO", "VALOR", "FECHA");
        System.out.println("---------------------------------------------------------------------------");
        for (Payment p : list) {
            String dateStr = (p.getPaymentDate() != null) ? p.getPaymentDate().format(dateFormatter) : "N/A";
            System.out.printf("%-10d | %-12d | %-12s | $%-11.2f | %-18s%n",
                    p.getId(), p.getReservationId(), p.getPaymentMethod(), p.getAmount(), dateStr);
        }
    }

    private void printBalanceSummary(int reservationId) {
        Reservation reservation = paymentController.findReservation(reservationId);
        if (reservation == null) {
            return;
        }
        double total = reservation.getTotalAmount();
        double paid = paymentController.calculatePaidAmount(reservationId);
        double balance = paymentController.calculateBalance(reservationId);

        System.out.println("\n--- RESUMEN FINANCIERO DE RESERVA #" + reservationId + " ---");
        System.out.printf("Total de la reserva : $%.2f%n", total);
        System.out.printf("Total pagado        : $%.2f%n", paid);
        System.out.printf("Saldo pendiente     : $%.2f%n", balance);
    }
}