package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Alvarez Juan, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Return {

    private int id;
    private Dispatch dispatch;
    private int returnedQuantity;
    private int damagedQuantity;
    private LocalDate returnDate;
    private User responsibleUser;
}