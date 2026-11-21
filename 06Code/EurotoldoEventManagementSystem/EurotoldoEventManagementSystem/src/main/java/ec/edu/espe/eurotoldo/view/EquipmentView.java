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
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
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

        int id = input.readInt("ID del equipo: ");

        if (id <= 0) {
            System.out.println(
                    "El ID debe ser mayor que cero.");
            return;
        }

        if (controller.findEquipmentById(id) != null) {
            System.out.println(
                    "Ya existe un equipo con ese ID.");
            return;
        }

        showEquipmentTypes();

        int equipmentType = input.readInt(
                "Seleccione el tipo de equipo: ");

        if (equipmentType < 1 || equipmentType > 5) {
            System.out.println(
                    "El tipo de equipo no es válido.");
            return;
        }

        String name = input.readRequiredText(
                "Nombre del equipo: ");

        int categoryId = input.readInt(
                "ID de la categoría: ");

        String categoryName = input.readRequiredText(
                "Nombre de la categoría: ");

        String categoryDescription = input.readText(
                "Descripción de la categoría: ");

        double rentalPrice = input.readDouble(
                "Precio diario de alquiler: ");

        int totalQuantity = input.readInt(
                "Cantidad total: ");

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

            if (controller.addEquipment(equipment)) {
                System.out.println(
                        "Equipo registrado correctamente.");
            } else {
                System.out.println(
                        "No se pudo registrar el equipo. "
                        + "Verifique la conexión con la API.");
            }

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
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
                        "Tamaño de la carpa: ");

                int tentCapacity = input.readInt(
                        "Capacidad de la carpa: ");

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
                String chairMaterial
                        = input.readRequiredText(
                                "Material de la silla: ");

                String chairColor
                        = input.readRequiredText(
                                "Color de la silla: ");

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
                        "Forma de la mesa: ");

                int tableCapacity = input.readInt(
                        "Capacidad de la mesa: ");

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
                                "Material del candelabro: ");

                int numberOfLights = input.readInt(
                        "Número de luces: ");

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
                    "No existen equipos registrados.");
            return;
        }

        String border
                = "+------+-------------------+"
                + "--------------------------+"
                + "--------------------+"
                + "------------+---------+-----------+";

        System.out.println("\nEQUIPOS REGISTRADOS");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-17s | %-24s | %-18s "
                + "| %-10s | %-7s | %-9s |%n",
                "ID",
                "Tipo",
                "Nombre",
                "Categoría",
                "Precio",
                "Total",
                "Disponible"
        );

        System.out.println(border);

        for (Equipment equipment
                : controller.getEquipmentList()) {

            String categoryName = "Sin categoría";

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

        System.out.println(border);
    }

    private void showEquipmentTypes() {

        System.out.println("\nTIPOS DE EQUIPO");
        System.out.println("[1] Carpa");
        System.out.println("[2] Silla");
        System.out.println("[3] Mesa");
        System.out.println("[4] Candelabro");
        System.out.println("[5] Otro equipo");
    }
}