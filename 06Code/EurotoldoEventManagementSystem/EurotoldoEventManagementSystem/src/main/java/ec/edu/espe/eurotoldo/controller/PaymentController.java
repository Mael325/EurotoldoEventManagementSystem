package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.CashPayment;
import ec.edu.espe.eurotoldo.model.Payment;
import ec.edu.espe.eurotoldo.model.Reservation;
import ec.edu.espe.eurotoldo.model.TransferPayment;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentController {

    private final ReservationController reservationController;
    private final PaymentApiClient apiClient;
    private final ArrayList<Payment> payments;

    public PaymentController(
            ReservationController reservationController) {

        this.reservationController
                = reservationController;

        apiClient = new PaymentApiClient();
        payments = new ArrayList<>();
    }

    public boolean loadPayments() {

        ArrayList<Payment> loadedPayments
                = apiClient.getPayments();

        if (loadedPayments == null) {
            return false;
        }

        payments.clear();

        for (Payment payment : loadedPayments) {

            if (!isValid(payment)
                    || findPaymentById(
                            payment.getId()) != null) {

                payments.clear();
                return false;
            }

            payments.add(payment);
        }

        return true;
    }

    public ArrayList<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public Payment findPaymentById(int paymentId) {

        for (Payment payment : payments) {

            if (payment.getId() == paymentId) {
                return payment;
            }
        }

        return null;
    }

    public ArrayList<Payment>
            getPaymentsByReservation(
                    int reservationId) {

        ArrayList<Payment> result
                = new ArrayList<>();

        for (Payment payment : payments) {

            if (payment.getReservationId()
                    == reservationId) {

                result.add(payment);
            }
        }

        return result;
    }

    public double calculatePaidAmount(
            int reservationId) {

        double totalPaid = 0.0;

        for (Payment payment : payments) {

            if (payment.getReservationId()
                    == reservationId) {

                totalPaid += payment.getAmount();
            }
        }

        return Math.round(
                totalPaid * 100.0
        ) / 100.0;
    }

    public double calculateBalance(
            int reservationId) {

        Reservation reservation
                = reservationController
                        .findReservationById(
                                reservationId
                        );

        if (reservation == null) {
            return -1.0;
        }

        double balance
                = reservation.getTotalAmount()
                - calculatePaidAmount(
                        reservationId
                );

        return Math.max(
                0.0,
                Math.round(
                        balance * 100.0
                ) / 100.0
        );
    }

    private boolean validatePaymentRegistration(
            int paymentId,
            int reservationId,
            double amount) {

        if (paymentId <= 0) {
            System.out.println(
                    "Error: El código del pago "
                    + "debe ser mayor que cero."
            );
            return false;
        }

        Reservation reservation
                = reservationController
                        .findReservationById(
                                reservationId
                        );

        if (reservation == null) {
            System.out.println(
                    "Error: No existe una reserva "
                    + "con el ID "
                    + reservationId
                    + "."
            );
            return false;
        }

        String status = reservation.getStatus();

        if (status != null
                && (status.equalsIgnoreCase(
                        Reservation.CANCELLED)
                || status.equalsIgnoreCase(
                        "Cancelada"))) {

            System.out.println(
                    "Error: No se pueden registrar "
                    + "pagos para una reserva cancelada."
            );
            return false;
        }

        if (findPaymentById(paymentId) != null) {

            System.out.println(
                    "Error: Ya existe un pago "
                    + "registrado con el ID "
                    + paymentId
                    + "."
            );
            return false;
        }

        if (amount <= 0) {
            System.out.println(
                    "Error: El monto del pago "
                    + "debe ser mayor que cero."
            );
            return false;
        }

        double pendingBalance
                = calculateBalance(reservationId);

        if (amount > pendingBalance) {

            System.out.printf(
                    "Error: El pago ($%.2f) supera "
                    + "el saldo pendiente ($%.2f).%n",
                    amount,
                    pendingBalance
            );

            return false;
        }

        return true;
    }

    public boolean addCashPayment(
            int paymentId,
            int reservationId,
            double amount,
            String receivedBy) {

        if (!validatePaymentRegistration(
                paymentId,
                reservationId,
                amount)) {

            return false;
        }

        try {
            CashPayment cashPayment
                    = new CashPayment(
                            paymentId,
                            reservationId,
                            amount,
                            LocalDateTime.now(),
                            receivedBy
                    );

            if (!apiClient.addPayment(cashPayment)) {

                System.out.println(
                        "Error: No se pudo guardar "
                        + "el pago en MongoDB."
                );
                return false;
            }

            payments.add(cashPayment);
            return true;

        } catch (IllegalArgumentException exception) {

            System.out.println(
                    "Error de validación: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    public boolean addTransferPayment(
            int paymentId,
            int reservationId,
            double amount,
            String bankName,
            String transactionNumber) {

        if (!validatePaymentRegistration(
                paymentId,
                reservationId,
                amount)) {

            return false;
        }

        try {
            TransferPayment transferPayment
                    = new TransferPayment(
                            paymentId,
                            reservationId,
                            amount,
                            LocalDateTime.now(),
                            bankName,
                            transactionNumber
                    );

            if (!apiClient.addPayment(
                    transferPayment)) {

                System.out.println(
                        "Error: No se pudo guardar "
                        + "el pago en MongoDB."
                );
                return false;
            }

            payments.add(transferPayment);
            return true;

        } catch (IllegalArgumentException exception) {

            System.out.println(
                    "Error de validación: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    public boolean removePayment(int paymentId) {

        Payment payment
                = findPaymentById(paymentId);

        if (payment == null) {
            return false;
        }

        if (!apiClient.deletePayment(paymentId)) {
            return false;
        }

        return payments.remove(payment);
    }

    public Reservation findReservation(
            int reservationId) {

        return reservationController
                .findReservationById(
                        reservationId
                );
    }

    /*
     * Se conserva para mantener compatibilidad.
     * Cada operación ya se guarda mediante la API.
     */
    public boolean savePayments() {
        return true;
    }

    private boolean isValid(Payment payment) {

        return payment != null
                && payment.getId() > 0
                && payment.getReservationId() > 0
                && payment.getAmount() > 0
                && payment.getPaymentDate() != null
                && payment.getPaymentMethod() != null;
    }
}