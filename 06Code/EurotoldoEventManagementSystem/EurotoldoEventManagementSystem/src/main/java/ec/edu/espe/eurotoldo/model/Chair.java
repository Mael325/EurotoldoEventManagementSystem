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
public class Chair extends Equipment {

    private String material;
    private String color;

    public Chair(int id, String name, Category category,
            double rentalPrice, int totalQuantity,
            String material, String color) {

        super(id, name, category, rentalPrice, totalQuantity);
        setMaterial(material);
        setColor(color);
    }

    @Override
    public String getEquipmentType() {
        return "Chair";
    }

    public void setMaterial(String material) {

        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException(
                    "The chair material cannot be empty.");
        }

        this.material = material;
    }

    public void setColor(String color) {

        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException(
                    "The chair color cannot be empty.");
        }

        this.color = color;
    }
}