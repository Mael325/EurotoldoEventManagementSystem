package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Inventory;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentController {

    private final Inventory inventory;
    private final EquipmentApiClient apiClient;
    private final CsvReportManager reportManager;

    public EquipmentController() {

        inventory = new Inventory();
        apiClient = new EquipmentApiClient();
        reportManager = new CsvReportManager();
    }

    public boolean loadEquipment() {

        ArrayList<Equipment> loadedEquipment
                = apiClient.getEquipment();

        if (loadedEquipment == null) {
            return false;
        }

        inventory.setEquipmentList(new ArrayList<>());

        for (Equipment equipment : loadedEquipment) {

            if (!isValid(equipment)
                    || !inventory.addEquipment(equipment)) {

                inventory.setEquipmentList(
                        new ArrayList<>()
                );

                return false;
            }
        }

        return true;
    }

    public boolean addEquipment(Equipment equipment) {

        if (!isValid(equipment)) {
            return false;
        }

        if (findEquipmentById(equipment.getId()) != null) {
            return false;
        }

        if (!apiClient.addEquipment(equipment)) {
            return false;
        }

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

        if (!inventory.reserveEquipment(id, quantity)) {
            return false;
        }

        Equipment equipment = findEquipmentById(id);

        if (apiClient.updateEquipment(equipment)) {
            return true;
        }

        inventory.releaseEquipment(id, quantity);
        return false;
    }

    public boolean releaseEquipment(
            int id, int quantity) {

        if (!inventory.releaseEquipment(id, quantity)) {
            return false;
        }

        Equipment equipment = findEquipmentById(id);

        if (apiClient.updateEquipment(equipment)) {
            return true;
        }

        inventory.reserveEquipment(id, quantity);
        return false;
    }

    public boolean removeEquipment(int id) {

        Equipment equipment = findEquipmentById(id);

        if (equipment == null) {
            return false;
        }

        if (!apiClient.deleteEquipment(id)) {
            return false;
        }

        return inventory.removeEquipment(id);
    }

    public ArrayList<Equipment> getEquipmentList() {
        return inventory.getEquipmentList();
    }

    /*
     * Se conserva temporalmente porque algunas vistas todavía
     * utilizan este método. Cada operación ya se guarda en la API.
     */
    public boolean saveEquipment() {
        return true;
    }

    public boolean exportEquipmentReport(
            String fileName) {

        return reportManager.exportEquipmentReport(
                inventory.getEquipmentList(),
                fileName
        );
    }

    private boolean isValid(Equipment equipment) {

        if (equipment == null || equipment.getId() <= 0) {
            return false;
        }

        if (equipment.getName() == null
                || equipment.getName().isBlank()) {
            return false;
        }

        if (equipment.getRentalPrice() < 0
                || equipment.getTotalQuantity() < 0
                || equipment.getAvailableQuantity() < 0) {
            return false;
        }

        return equipment.getAvailableQuantity()
                <= equipment.getTotalQuantity();
    }
}