package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Reservation {

    private int id;
    private Customer customer;
    private Event event;
    private String status;
    private LocalDate reservationDate;
    private List<Assignment> assignments;
}
