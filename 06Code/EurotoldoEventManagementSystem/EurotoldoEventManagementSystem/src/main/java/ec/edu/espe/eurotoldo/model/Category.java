package ec.edu.espe.eurotoldo.model;

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
public class Category {

    private int id;
    private String name;
    private String description;
}
