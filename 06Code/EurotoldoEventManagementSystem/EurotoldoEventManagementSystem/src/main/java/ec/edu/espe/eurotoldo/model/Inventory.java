
package ec.edu.espe.eurotoldo.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Alvarez Juan, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@Setter
@AllArgsConstructor
@ToString

public class Inventory {

    private List<Equipment> equipmentList;

    public boolean addEquipment(Equipment equipment) {

        if (equipment == null) {
            return false;
        }

        if (equipment.getTotalQuantity() < 0) {
            return false;
        }

        for (Equipment registeredEquipment : equipmentList) {
            if (registeredEquipment.getId() == equipment.getId()) {
                return false;
            }
        }

        equipmentList.add(equipment);

        return true;
    }

    public Equipment findEquipmentById(int id) {

        for (Equipment equipment : equipmentList) {
            if (equipment.getId() == id) {
                return equipment;
            }
        }

        return null;
    }
}