package ec.edu.espe.eurotoldo.model;

import java.util.ArrayList;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@ToString
public class Inventory {

    private ArrayList<Equipment> equipmentList;

    public Inventory() {
        equipmentList = new ArrayList<>();
    }

    public Inventory(ArrayList<Equipment> equipmentList) {
        setEquipmentList(equipmentList);
    }

    public boolean addEquipment(Equipment equipment) {

        if (equipment == null) {
            return false;
        }

        if (findEquipmentById(equipment.getId()) != null) {
            return false;
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

    public boolean isEquipmentAvailable(int id, int quantity) {

        Equipment equipment = findEquipmentById(id);

        if (equipment == null) {
            return false;
        }

        return equipment.isAvailable(quantity);
    }

    public boolean reserveEquipment(int id, int quantity) {

        Equipment equipment = findEquipmentById(id);

        if (equipment == null) {
            return false;
        }

        return equipment.reserve(quantity);
    }

    public boolean releaseEquipment(int id, int quantity) {

        Equipment equipment = findEquipmentById(id);

        if (equipment == null) {
            return false;
        }

        return equipment.release(quantity);
    }

    public boolean removeEquipment(int id) {

        Equipment equipment = findEquipmentById(id);

        if (equipment == null) {
            return false;
        }

        if (equipment.getAvailableQuantity()
                != equipment.getTotalQuantity()) {
            return false;
        }

        return equipmentList.remove(equipment);
    }

    public void setEquipmentList(
            ArrayList<Equipment> equipmentList) {

        if (equipmentList == null) {
            this.equipmentList = new ArrayList<>();
        } else {
            this.equipmentList = equipmentList;
        }
    }
}