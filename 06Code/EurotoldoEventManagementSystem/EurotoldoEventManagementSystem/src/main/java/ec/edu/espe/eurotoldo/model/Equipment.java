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
public class Equipment implements Rentable {

    private int id;
    private String name;
    private Category category;
    private double rentalPrice;
    private int totalQuantity;
    private int availableQuantity;

    // Maintains compatibility with the previous code.
    public Equipment(int id, String name, Category category,
            int totalQuantity) {

        this(id, name, category, 0.0, totalQuantity);
    }

    public Equipment(int id, String name, Category category,
            double rentalPrice, int totalQuantity) {

        setId(id);
        setName(name);
        setCategory(category);
        setRentalPrice(rentalPrice);
        setTotalQuantity(totalQuantity);
        setAvailableQuantity(totalQuantity);
    }

    @Override
    public double calculateRentalCost(int quantity, int days) {

        if (quantity <= 0 || days <= 0) {
            return 0.0;
        }

        return rentalPrice * quantity * days;
    }

    @Override
    public boolean isAvailable(int quantity) {
        return quantity > 0 && quantity <= availableQuantity;
    }

    public String getEquipmentType() {
        return "General Equipment";
    }

    public boolean reserve(int quantity) {

        if (!isAvailable(quantity)) {
            return false;
        }

        availableQuantity -= quantity;
        return true;
    }

    public boolean release(int quantity) {

        if (quantity <= 0
                || availableQuantity + quantity > totalQuantity) {
            return false;
        }

        availableQuantity += quantity;
        return true;
    }

    public void setId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "The equipment ID cannot be negative.");
        }

        this.id = id;
    }

    public void setName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "The equipment name cannot be empty.");
        }

        this.name = name;
    }

    public void setCategory(Category category) {

        if (category == null) {
            throw new IllegalArgumentException(
                    "The equipment category cannot be empty.");
        }

        this.category = category;
    }

    public void setRentalPrice(double rentalPrice) {

        if (rentalPrice < 0) {
            throw new IllegalArgumentException(
                    "The rental price cannot be negative.");
        }

        this.rentalPrice = rentalPrice;
    }

    public void setTotalQuantity(int totalQuantity) {

        if (totalQuantity < 0) {
            throw new IllegalArgumentException(
                    "The total quantity cannot be negative.");
        }

        this.totalQuantity = totalQuantity;

        if (availableQuantity > totalQuantity) {
            availableQuantity = totalQuantity;
        }
    }

    public void setAvailableQuantity(int availableQuantity) {

        if (availableQuantity < 0
                || availableQuantity > totalQuantity) {

            throw new IllegalArgumentException(
                    "The available quantity is invalid.");
        }

        this.availableQuantity = availableQuantity;
    }
}