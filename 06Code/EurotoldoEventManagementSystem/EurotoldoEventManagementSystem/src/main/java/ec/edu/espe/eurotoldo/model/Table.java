package ec.edu.espe.eurotoldo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Table extends Equipment {

    private String shape;
    private int capacity;

    public Table(int id, String name, Category category,
            double rentalPrice, int totalQuantity,
            String shape, int capacity) {

        super(id, name, category, rentalPrice, totalQuantity);
        setShape(shape);
        setCapacity(capacity);
    }

    @Override
    public String getEquipmentType() {
        return "Table";
    }

    public void setShape(String shape) {

        if (shape == null || shape.isBlank()) {
            throw new IllegalArgumentException(
                    "The table shape cannot be empty.");
        }

        this.shape = shape;
    }

    public void setCapacity(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "The table capacity must be greater than zero.");
        }

        this.capacity = capacity;
    }
}