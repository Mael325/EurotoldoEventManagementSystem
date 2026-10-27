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

public class Event {

    private int id;
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
}