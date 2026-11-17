package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.EquipmentController;
import ec.edu.espe.eurotoldo.model.Category;
import ec.edu.espe.eurotoldo.model.Chair;
import ec.edu.espe.eurotoldo.model.Chandelier;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Table;
import ec.edu.espe.eurotoldo.model.Tent;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentView {

    private final EquipmentController controller;
    private final ConsoleInput input;

    public EquipmentView(
            EquipmentController controller,
            ConsoleInput input) {

        this.controller = controller;
        this.input = input;
    }

    public void registerEquipment() {

        int id = input.readInt("Equipment ID: ");

        if (controller.findEquipmentById(id) != null) {
            System.out.println(
                    "An equipment item with this ID already exists.");
            return;
        }

        showEquipmentTypes();
        int equipmentType = input.readInt(
                "Choose the equipment type: ");

        if (equipmentType < 1 || equipmentType > 5) {
            System.out.println("Invalid equipment type.");
            return;
        }

        String name = input.readRequiredText(
                "Equipment name: ");

        int categoryId = input.readInt(
                "Category ID: ");

        String categoryName = input.readRequiredText(
                "Category name: ");

        String categoryDescription = input.readText(
                "Category description: ");

        double rentalPrice = input.readDouble(
                "Daily rental price: ");

        int totalQuantity = input.readInt(
                "Total quantity: ");

        Category category = new Category(
                categoryId,
                categoryName,
                categoryDescription
        );

        try {
            Equipment equipment = createEquipment(
                    equipmentType,
                    id,
                    name,
                    category,
                    rentalPrice,
                    totalQuantity
            );

            if (!controller.addEquipment(equipment)) {
                System.out.println(
                        "The equipment could not be registered.");
                return;
            }

            if (controller.saveEquipment()) {
                System.out.println(
                        "Equipment registered successfully.");
            } else {
                System.out.println(
                        "Equipment registered, but it could not be saved.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private Equipment createEquipment(
            int equipmentType,
            int id,
            String name,
            Category category,
            double rentalPrice,
            int totalQuantity) {

        Equipment equipment;

        switch (equipmentType) {

            case 1:
                String size = input.readRequiredText(
                        "Tent size: ");

                int tentCapacity = input.readInt(
                        "Tent capacity: ");

                equipment = new Tent(
                        id,
                        name,
                        category,
                        rentalPrice,
                        totalQuantity,
                        size,
                        tentCapacity
                );
                break;

            case 2:
                String chairMaterial = input.readRequiredText(
                        "Chair material: ");

                String chairColor = input.readRequiredText(
                        "Chair color: ");

                equipment = new Chair(
                        id,
                        name,
                        category,
                        rentalPrice,
                        totalQuantity,
                        chairMaterial,
                        chairColor
                );
                break;

            case 3:
                String shape = input.readRequiredText(
                        "Table shape: ");

                int tableCapacity = input.readInt(
                        "Table capacity: ");

                equipment = new Table(
                        id,
                        name,
                        category,
                        rentalPrice,
                        totalQuantity,
                        shape,
                        tableCapacity
                );
                break;

            case 4:
                String chandelierMaterial
                        = input.readRequiredText(
                                "Chandelier material: ");

                int numberOfLights = input.readInt(
                        "Number of lights: ");

                equipment = new Chandelier(
                        id,
                        name,
                        category,
                        rentalPrice,
                        totalQuantity,
                        chandelierMaterial,
                        numberOfLights
                );
                break;

            default:
                equipment = new Equipment(
                        id,
                        name,
                        category,
                        rentalPrice,
                        totalQuantity
                );
                break;
        }

        return equipment;
    }

    public void showEquipment() {

        if (controller.getEquipmentList().isEmpty()) {
            System.out.println(
                    "There is no registered equipment.");
            return;
        }

        System.out.println("\nREGISTERED EQUIPMENT");
        System.out.println(
                "+------+-------------------+"
                + "--------------------------+"
                + "--------------------+"
                + "------------+---------+-----------+");

        System.out.printf(
                "| %-4s | %-17s | %-24s | %-18s "
                + "| %-10s | %-7s | %-9s |%n",
                "ID",
                "Type",
                "Name",
                "Category",
                "Price",
                "Total",
                "Available"
        );

        System.out.println(
                "+------+-------------------+"
                + "--------------------------+"
                + "--------------------+"
                + "------------+---------+-----------+");

        for (Equipment equipment
                : controller.getEquipmentList()) {

            String categoryName = "No category";

            if (equipment.getCategory() != null
                    && equipment.getCategory().getName() != null) {

                categoryName = equipment
                        .getCategory()
                        .getName();
            }

            System.out.printf(
                    "| %-4d | %-17s | %-24s | %-18s "
                    + "| $%-9.2f | %-7d | %-9d |%n",
                    equipment.getId(),
                    equipment.getEquipmentType(),
                    equipment.getName(),
                    categoryName,
                    equipment.getRentalPrice(),
                    equipment.getTotalQuantity(),
                    equipment.getAvailableQuantity()
            );
        }

        System.out.println(
                "+------+-------------------+"
                + "--------------------------+"
                + "--------------------+"
                + "------------+---------+-----------+");
    }

    private void showEquipmentTypes() {

        System.out.println("\nEQUIPMENT TYPES");
        System.out.println("[1] Tent");
        System.out.println("[2] Chair");
        System.out.println("[3] Table");
        System.out.println("[4] Chandelier");
        System.out.println("[5] Other equipment");
    }
}