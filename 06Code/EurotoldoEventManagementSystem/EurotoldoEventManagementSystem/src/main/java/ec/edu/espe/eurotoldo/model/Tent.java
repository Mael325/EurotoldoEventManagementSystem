package ec.edu.espe.eurotoldo.model;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class Tent extends Equipment {

    private String size;
    private int capacity;

    public Tent() {
    }

    public Tent(int id, String name, Category category,
            double rentalPrice, int totalQuantity,
            String size, int capacity) {

        super(id, name, category, rentalPrice, totalQuantity);
        setSize(size);
        setCapacity(capacity);
    }

    @Override
    public String getEquipmentType() {
        return "Tent";
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {

        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException(
                    "The tent size cannot be empty.");
        }

        this.size = size;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "The tent capacity must be greater than zero.");
        }

        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Tent{"
                + super.toString()
                + ", size=" + size
                + ", capacity=" + capacity
                + '}';
    }
}