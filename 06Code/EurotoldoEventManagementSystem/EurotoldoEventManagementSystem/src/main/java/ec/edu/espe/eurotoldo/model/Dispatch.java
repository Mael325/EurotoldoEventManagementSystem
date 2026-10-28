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

public class Dispatch {

    private int id;
    private Reservation reservation;
    private Equipment equipment;
    private int quantity;
    private LocalDate dispatchDate;
    private User responsibleUser;
}
