package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;
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

public class Damage {

    private int id;
    private Equipment equipment;
    private int quantity;
    private String description;
    private LocalDate reportedDate;
    private String status;
}
