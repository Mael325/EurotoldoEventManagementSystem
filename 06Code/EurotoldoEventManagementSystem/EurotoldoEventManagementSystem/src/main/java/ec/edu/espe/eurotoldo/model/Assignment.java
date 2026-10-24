
package ec.edu.espe.eurotoldo.model;

/**
 *
 * @author Anthony Arevalo, MosQ ,@ESPE
 */
public class Assignment {
    
    private int id;
    private Equipment equipment;
    private int quantity;

    public Assignment(int id, Equipment equipment, int quantity) {
        this.id = id;
        this.equipment = equipment;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Assignment{" + "id=" + id + ", equipment=" + equipment + ", quantity=" + quantity + '}';
    }
    
    
    
}
