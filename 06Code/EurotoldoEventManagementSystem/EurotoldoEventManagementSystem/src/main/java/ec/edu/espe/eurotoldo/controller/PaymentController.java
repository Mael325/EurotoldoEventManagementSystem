package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.CashPayment;
import ec.edu.espe.eurotoldo.model.Payment;
import ec.edu.espe.eurotoldo.model.Reservation;
import ec.edu.espe.eurotoldo.model.TransferPayment;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentController {

    private final ReservationController reservationController;
    private final PaymentFileManager fileManager;
    private ArrayList<Payment> payments;

    public PaymentController(ReservationController reservationController) {
        this.reservationController = reservationController;
        this.fileManager = new PaymentFileManager();
        this.payments = new ArrayList<>();
    }

    public boolean loadPayments() {
        ArrayList<Payment> loaded = fileManager.loadPayments();
        if (loaded == null) {
            return false;
        }
        this.payments = loaded;
        return true;
    }

    public boolean savePayments() {
        return fileManager.savePayments(this.payments);
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public Payment findPaymentById(int paymentId) {
        for (Payment payment : payments) {
            if (payment.getId() == paymentId) {
                return payment;
            }
        }
        return null;
    }

    public ArrayList<Payment> getPaymentsByReservation(int reservationId) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getReservationId() == reservationId) {
                result.add(payment);
            }
        }
        return result;
    }

    public double calculatePaidAmount(int reservationId) {
        double totalPaid = 0.0;
        for (Payment payment : payments) {
            if (payment.getReservationId() == reservationId) {
                totalPaid += payment.getAmount();
            }
        }
        return totalPaid;
    }

    public double calculateBalance(int reservationId) {
        Reservation reservation = reservationController.findReservationById(reservationId);
        if (reservation == null) {
            return -1.0;
        }
        double balance = reservation.getTotalAmount() - calculatePaidAmount(reservationId);
        return Math.max(0.0, Math.round(balance * 100.0) / 100.0);
    }

    private boolean validatePaymentRegistration(int paymentId, int reservationId, double amount) {
        Reservation reservation = reservationController.findReservationById(reservationId);
        if (reservation == null) {
            System.out.println("Error: No existe una reserva con el ID " + reservationId + ".");
            return false;
        }

        if (reservation.getStatus() != null
                && (reservation.getStatus().equalsIgnoreCase("Cancelada")
                || reservation.getStatus().equalsIgnoreCase("Cancelled"))) {
            System.out.println("Error: No se pueden registrar pagos para una reserva cancelada.");
            return false;
        }

        if (findPaymentById(paymentId) != null) {
            System.out.println("Error: Ya existe un pago registrado con el ID " + paymentId + ".");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Error: El monto del pago debe ser mayor que cero.");
            return false;
        }

        double pendingBalance = calculateBalance(reservationId);
        if (amount > pendingBalance) {
            System.out.printf("Error: El pago ($%.2f) supera el saldo pendiente ($%.2f).%n",
                    amount, pendingBalance);
            return false;
        }

        return true;
    }

    public boolean addCashPayment(int paymentId, int reservationId, double amount, String receivedBy) {
        if (!validatePaymentRegistration(paymentId, reservationId, amount)) {
            return false;
        }

        try {
            CashPayment cashPayment = new CashPayment(
                    paymentId,
                    reservationId,
                    amount,
                    LocalDateTime.now(),
                    receivedBy
            );
            payments.add(cashPayment);
            return savePayments();
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            return false;
        }
    }

    public boolean addTransferPayment(int paymentId, int reservationId, double amount,
            String bankName, String transactionNumber) {
        if (!validatePaymentRegistration(paymentId, reservationId, amount)) {
            return false;
        }

        try {
            TransferPayment transferPayment = new TransferPayment(
                    paymentId,
                    reservationId,
                    amount,
                    LocalDateTime.now(),
                    bankName,
                    transactionNumber
            );
            payments.add(transferPayment);
            return savePayments();
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            return false;
        }
    }

    public boolean removePayment(int paymentId) {
        Payment payment = findPaymentById(paymentId);
        if (payment == null) {
            return false;
        }
        payments.remove(payment);
        return savePayments();
    }

    public Reservation findReservation(int reservationId) {
        return reservationController.findReservationById(reservationId);
    }
}