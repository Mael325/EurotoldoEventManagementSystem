package ec.edu.espe.eurotoldo.model;

import java.time.LocalDateTime;
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
@ToString(callSuper = true)
public class CashPayment extends Payment {

    private String receivedBy;

    public CashPayment(int id, int reservationId, double amount, LocalDateTime paymentDate, String receivedBy) {
        super(id, reservationId, amount, paymentDate);
        this.receivedBy = validateReceivedBy(receivedBy);
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = validateReceivedBy(receivedBy);
    }

    private String validateReceivedBy(String receivedBy) {
        if (receivedBy == null || receivedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de quien recibe el efectivo no puede estar vacío.");
        }
        return receivedBy.trim();
    }

    @Override
    public String getPaymentMethod() {
        return "Cash";
    }
}