
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

public class Maintenance {

    private int id;
    private Equipment equipment;
    private int quantity;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}