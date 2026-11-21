package ec.edu.espe.eurotoldo.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Payment {

    private int id;
    private int reservationId;
    private double amount;
    private LocalDateTime paymentDate;

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID del pago no puede ser negativo.");
        }
        this.id = id;
    }

    public void setReservationId(int reservationId) {
        if (reservationId < 0) {
            throw new IllegalArgumentException("El código de reserva no puede ser negativo.");
        }
        this.reservationId = reservationId;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El valor del pago debe ser mayor que cero.");
        }
        this.amount = amount;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        if (paymentDate == null) {
            throw new IllegalArgumentException("La fecha de pago no puede ser nula.");
        }
        this.paymentDate = paymentDate;
    }

    public abstract String getPaymentMethod();
}