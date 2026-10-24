
package ec.edu.espe.eurotoldo.model;

import java.util.List;

/**
 *
 * @author Anthony Arevalo, MosQ ,@ESPE
 */
public class Inventory {
    private List<Equipment> equipmentList;

    public Inventory(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    @Override
    public String toString() {
        return "Inventory{" + "equipmentList=" + equipmentList + '}';
    }
    
}
