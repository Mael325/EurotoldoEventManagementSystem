package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Inventory;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentController {

    private final Inventory inventory;
    private final EquipmentFileManager fileManager;
    private final CsvReportManager reportManager;

    public EquipmentController() {

        inventory = new Inventory();
        fileManager = new EquipmentFileManager();
        reportManager = new CsvReportManager();
    }

    public boolean loadEquipment() {

        ArrayList<Equipment> loadedEquipment
                = fileManager.loadEquipment();

        if (loadedEquipment == null) {
            return false;
        }

        inventory.setEquipmentList(loadedEquipment);
        return true;
    }

    public boolean addEquipment(Equipment equipment) {
        return inventory.addEquipment(equipment);
    }

    public Equipment findEquipmentById(int id) {
        return inventory.findEquipmentById(id);
    }

    public boolean isEquipmentAvailable(
            int id, int quantity) {

        return inventory.isEquipmentAvailable(
                id,
                quantity
        );
    }

    public boolean reserveEquipment(
            int id, int quantity) {

        return inventory.reserveEquipment(
                id,
                quantity
        );
    }

    public boolean releaseEquipment(
            int id, int quantity) {

        return inventory.releaseEquipment(
                id,
                quantity
        );
    }

    public boolean removeEquipment(int id) {
        return inventory.removeEquipment(id);
    }

    public ArrayList<Equipment> getEquipmentList() {
        return inventory.getEquipmentList();
    }

    public boolean saveEquipment() {

        return fileManager.saveEquipment(
                inventory.getEquipmentList()
        );
    }

    public boolean exportEquipmentReport(
            String fileName) {

        return reportManager.exportEquipmentReport(
                inventory.getEquipmentList(),
                fileName
        );
    }
}