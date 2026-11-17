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
@ToString
public class Assignment {

    private int id;
    private Equipment equipment;
    private int quantity;

    public Assignment(
            int id,
            Equipment equipment,
            int quantity) {

        setId(id);
        setEquipment(equipment);
        setQuantity(quantity);
    }

    public double calculateSubtotal(int rentalDays) {

        if (equipment == null
                || quantity <= 0
                || rentalDays <= 0) {

            return 0.0;
        }

        return equipment.calculateRentalCost(
                quantity,
                rentalDays
        );
    }

    public void setId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "The assignment ID cannot be negative.");
        }

        this.id = id;
    }

    public void setEquipment(Equipment equipment) {

        if (equipment == null) {
            throw new IllegalArgumentException(
                    "The equipment cannot be empty.");
        }

        this.equipment = equipment;
    }

    public void setQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "The quantity must be greater than zero.");
        }

        this.quantity = quantity;
    }
}