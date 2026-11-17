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
public class Chandelier extends Equipment {

    private String material;
    private int numberOfLights;

    public Chandelier(int id, String name, Category category,
            double rentalPrice, int totalQuantity,
            String material, int numberOfLights) {

        super(id, name, category, rentalPrice, totalQuantity);
        setMaterial(material);
        setNumberOfLights(numberOfLights);
    }

    @Override
    public String getEquipmentType() {
        return "Chandelier";
    }

    public void setMaterial(String material) {

        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException(
                    "The chandelier material cannot be empty.");
        }

        this.material = material;
    }

    public void setNumberOfLights(int numberOfLights) {

        if (numberOfLights <= 0) {
            throw new IllegalArgumentException(
                    "The number of lights must be greater than zero.");
        }

        this.numberOfLights = numberOfLights;
    }
}