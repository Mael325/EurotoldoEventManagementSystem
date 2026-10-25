

package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.EquipmentFileManager;
import ec.edu.espe.eurotoldo.model.Category;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Inventory;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Dylan Cumbajin, MosQ, @ESPE
 */
public class EurotoldoApp {

    public static void main(String[] args) {
        showMenu();
    }
    

    public static void showMenu() {
        EquipmentFileManager fileManager = new EquipmentFileManager();
        List<Equipment> equipmentList = fileManager.loadEquipment();

        if (equipmentList == null) {
            System.out.println("Error loading equipment.json.");
            System.out.println("Check the file before continuing.");
            return;
        }

        Inventory inventory = new Inventory(equipmentList);
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nEUROTOLDO EVENT MANAGEMENT SYSTEM");
            System.out.println("1. Register equipment");
            System.out.println("2. View equipment");
            System.out.println("3. Save equipment");
            System.out.println("0. Exit");

            option = readInt(scanner, "Choose an option: ");

            switch (option) {
                case 1:
                    registerEquipment(scanner, inventory, fileManager);
                    break;

                case 2:
                    viewEquipment(inventory);
                    break;

                case 3:
                    if (fileManager.saveEquipment(inventory.getEquipmentList())) {
                        System.out.println("Equipment saved successfully.");
                    } else {
                        System.out.println("Could not save equipment.");
                    }
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }

        } while (option != 0);

        scanner.close();
    }

    public static void registerEquipment(
            Scanner scanner,
            Inventory inventory,
            EquipmentFileManager fileManager) {

        int id = readInt(scanner, "Equipment ID: ");

        if (inventory.findEquipmentById(id) != null) {
            System.out.println("This equipment ID already exists.");
            return;
        }

        System.out.print("Equipment name: ");
        String name = scanner.nextLine();

        int categoryId = readInt(scanner, "Category ID: ");

        System.out.print("Category name: ");
        String categoryName = scanner.nextLine();

        System.out.print("Category description: ");
        String categoryDescription = scanner.nextLine();

        int totalQuantity = readInt(scanner, "Total quantity: ");

        if (name.trim().isEmpty() || categoryName.trim().isEmpty()) {
            System.out.println("Equipment and category names cannot be empty.");
            return;
        }

        Category category = new Category(
                categoryId,
                categoryName,
                categoryDescription
        );

        Equipment equipment = new Equipment(
                id,
                name,
                category,
                totalQuantity
        );

        if (inventory.addEquipment(equipment)) {
            if (fileManager.saveEquipment(inventory.getEquipmentList())) {
                System.out.println("Equipment registered and saved.");
            } else {
                System.out.println("Equipment added only to this session.");
                System.out.println("Saving failed. Use option 3 before exiting.");
            }
        } else {
            System.out.println("Could not add equipment.");
            System.out.println("Check the ID and quantity.");
        }
    }

    public static void viewEquipment(Inventory inventory) {

        if (inventory.getEquipmentList().isEmpty()) {
            System.out.println("No equipment registered.");
            return;
        }

        for (Equipment equipment : inventory.getEquipmentList()) {
            System.out.println(equipment);
        }
    }

    public static int readInt(Scanner scanner, String message) {

        System.out.print(message);

        while (!scanner.hasNextInt()) {
            System.out.println("Enter a valid whole number.");
            scanner.nextLine();
            System.out.print(message);
        }

        int value = scanner.nextInt();
        scanner.nextLine();

        return value;
    }
}