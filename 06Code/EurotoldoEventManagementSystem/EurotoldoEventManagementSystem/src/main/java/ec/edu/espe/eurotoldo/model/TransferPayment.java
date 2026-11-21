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
public class TransferPayment extends Payment {

    private String bankName;
    private String transactionNumber;

    public TransferPayment(int id, int reservationId, double amount, LocalDateTime paymentDate,
            String bankName, String transactionNumber) {
        super(id, reservationId, amount, paymentDate);
        this.bankName = validateNotEmpty(bankName, "El nombre del banco no puede estar vacío.");
        this.transactionNumber = validateNotEmpty(transactionNumber, "El número de transacción no puede estar vacío.");
    }

    public void setBankName(String bankName) {
        this.bankName = validateNotEmpty(bankName, "El nombre del banco no puede estar vacío.");
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = validateNotEmpty(transactionNumber, "El número de transacción no puede estar vacío.");
    }

    private String validateNotEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    @Override
    public String getPaymentMethod() {
        return "Transfer";
    }
}