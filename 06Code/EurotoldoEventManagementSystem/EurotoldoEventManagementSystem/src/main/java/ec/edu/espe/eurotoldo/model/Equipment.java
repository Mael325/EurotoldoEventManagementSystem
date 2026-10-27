
package ec.edu.espe.eurotoldo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */

@Getter
@Setter
@AllArgsConstructor
@ToString

public class Equipment {

    private int id;
    private String name;
    private Category category;
    private int totalQuantity;
}