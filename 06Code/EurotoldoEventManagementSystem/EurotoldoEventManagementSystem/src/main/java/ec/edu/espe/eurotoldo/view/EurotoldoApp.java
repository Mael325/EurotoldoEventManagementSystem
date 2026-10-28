package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.EquipmentFileManager;
import ec.edu.espe.eurotoldo.controller.CsvReportManager;
import ec.edu.espe.eurotoldo.model.Category;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Inventory;
import java.util.ArrayList;
import java.util.Scanner;
import ec.edu.espe.eurotoldo.controller.EventController;
import ec.edu.espe.eurotoldo.controller.EventFileManager;
import ec.edu.espe.eurotoldo.model.Event;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EurotoldoApp {

    public static void main(String[] args) {
        showMenu();
    }

    public static void showMenu() {
        EquipmentFileManager fileManager = new EquipmentFileManager();
        CsvReportManager csvReportManager = new CsvReportManager();
        ArrayList<Equipment> equipmentList = fileManager.loadEquipment();

        if (equipmentList == null) {
            System.out.println("Error al cargar equipment.json.");
            System.out.println("Revisa el archivo antes de continuar.");
            return;
        }

        Inventory inventory = new Inventory(equipmentList);

        EventController eventController = new EventController();
        EventFileManager eventFileManager = new EventFileManager();
        ArrayList<Event> savedEvents = eventFileManager.loadEvents();

        if (savedEvents == null) {
            System.out.println("Error al leer events.json.");
            System.out.println("Revisa el archivo antes de continuar.");
            return;
        }

        for (Event event : savedEvents) {
            if (!eventController.addEvent(event)) {
                System.out.println(
                        "events.json contiene eventos invalidos o repetidos.");
                System.out.println("Revisa el archivo antes de continuar.");
                return;
            }
        }

        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n+--------------------------------------------+");
            System.out.println("|                 EUROTOLDO                  |");
            System.out.println("|        SISTEMA DE GESTION DE EVENTOS       |");
            System.out.println("+--------------------------------------------+");
            System.out.println("| MENU PRINCIPAL - Elige una opcion          |");
            System.out.println("+--------------------------------------------+");
            System.out.println("| EQUIPOS                                    |");
            System.out.println("|   [1] Registrar equipo                     |");
            System.out.println("|   [2] Ver equipos                          |");
            System.out.println("|   [3] Guardar equipos                      |");
            System.out.println("+--------------------------------------------+");
            System.out.println("| EVENTOS                                    |");
            System.out.println("|   [4] Registrar evento                     |");
            System.out.println("|   [5] Ver eventos                          |");
            System.out.println("|   [6] Buscar evento                        |");
            System.out.println("|   [7] Actualizar evento                    |");
            System.out.println("|   [8] Guardar eventos                      |");
            System.out.println("+--------------------------------------------+");
            System.out.println("| REPORTES                                   |");
            System.out.println("|   [9] Exportar inventario a CSV            |");
            System.out.println("+--------------------------------------------+");
            System.out.println("|   [0] Salir                                |");
            System.out.println("+--------------------------------------------+");

            option = readInt(scanner, "Elige una opcion: ");

            switch (option) {
                case 1:
                    registerEquipment(scanner, inventory, fileManager);
                    break;

                case 2:
                    viewEquipment(inventory);
                    break;

                case 3:
                    if (fileManager.saveEquipment(
                            inventory.getEquipmentList())) {
                        System.out.println("Equipos guardados correctamente.");
                    } else {
                        System.out.println("No se pudieron guardar los equipos.");
                    }
                    break;

                case 4:
                    editEvent(
                            scanner,
                            eventController,
                            eventFileManager,
                            false
                    );
                    break;

                case 5:
                    viewEvents(eventController);
                    break;

                case 6:
                    int eventId = readInt(scanner, "Codigo del evento: ");
                    Event found = eventController.findEventById(eventId);

                    if (found == null) {
                        System.out.println("Evento no encontrado.");
                    } else {
                        System.out.println(found);
                    }
                    break;

                case 7:
                    editEvent(
                            scanner,
                            eventController,
                            eventFileManager,
                            true
                    );
                    break;

                case 8:
                    if (eventFileManager.saveEvents(
                            eventController.getEvents())) {
                        System.out.println("Eventos guardados.");
                    } else {
                        System.out.println(
                                "No se pudieron guardar los eventos.");
                    }
                    break;

                case 9:
                    if (csvReportManager.exportEquipmentReport(
                            inventory.getEquipmentList(), "equipment_report.csv")) {
                        System.out.println("Inventario exportado a equipment_report.csv.");
                    } else {
                        System.out.println("No se pudo exportar el inventario a CSV.");
                    }
                    break;

                case 0:
                    System.out.println("Hasta luego!");
                    break;

                default:
                    System.out.println("Opcion invalida. Intentalo de nuevo.");
                    break;
            }

        } while (option != 0);

        scanner.close();
    }

    public static void registerEquipment(
            Scanner scanner,
            Inventory inventory,
            EquipmentFileManager fileManager) {

        int id = readInt(scanner, "Codigo del equipo: ");

        if (inventory.findEquipmentById(id) != null) {
            System.out.println("Ese codigo de equipo ya esta registrado.");
            return;
        }

        System.out.print("Nombre del equipo: ");
        String name = scanner.nextLine();

        int categoryId = readInt(scanner, "Codigo de la categoria: ");

        System.out.print("Nombre de la categoria: ");
        String categoryName = scanner.nextLine();

        System.out.print("Descripcion de la categoria: ");
        String categoryDescription = scanner.nextLine();

        int totalQuantity = readInt(scanner, "Cantidad total: ");

        if (name.trim().isEmpty() || categoryName.trim().isEmpty()) {
            System.out.println(
                    "Los nombres del equipo y de la categoria no pueden estar vacios.");
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
                System.out.println("Equipo registrado y guardado.");
            } else {
                System.out.println("Equipo anadido solo a esta sesion.");
                System.out.println(
                        "No se pudo guardar. Usa la opcion 3 antes de salir.");
            }
        } else {
            System.out.println("No se pudo anadir el equipo.");
            System.out.println("Revisa el codigo y la cantidad.");
        }
    }

    public static void viewEquipment(Inventory inventory) {

        if (inventory.getEquipmentList().isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        for (Equipment equipment : inventory.getEquipmentList()) {
            System.out.println(equipment);
        }
    }

    // Mostrar todos los eventos registrados.
    public static void viewEvents(EventController controller) {
        ArrayList<Event> events = controller.getEvents();

        if (events.isEmpty()) {
            System.out.println("No hay eventos registrados.");
            return;
        }

        for (Event event : events) {
            System.out.println(event);
        }
    }

    // Registrar o actualizar un evento y guardar la lista.
    public static void editEvent(
            Scanner scanner,
            EventController controller,
            EventFileManager fileManager,
            boolean updating) {

        int id = readInt(scanner, "Codigo del evento: ");
        Event existing = controller.findEventById(id);

        if (updating && existing == null) {
            System.out.println("No existe un evento con ese codigo.");
            return;
        }

        if (!updating && existing != null) {
            System.out.println("Ese codigo ya esta registrado.");
            return;
        }

        System.out.print("Nombre: ");
        String name = scanner.nextLine();

        System.out.print("Ubicacion: ");
        String location = scanner.nextLine();

        try {
            System.out.print("Fecha inicial (AAAA-MM-DD): ");
            LocalDate start = LocalDate.parse(
                    scanner.nextLine().trim());

            System.out.print("Fecha final (AAAA-MM-DD): ");
            LocalDate end = LocalDate.parse(
                    scanner.nextLine().trim());

            Event event = new Event(id, name, location, start, end);

            boolean success;

            if (updating) {
                success = controller.updateEvent(event);
            } else {
                success = controller.addEvent(event);
            }

            if (!success) {
                System.out.println(
                        "Revisa la ubicacion y el orden de las fechas.");
                return;
            }

            if (fileManager.saveEvents(controller.getEvents())) {
                System.out.println("Evento guardado correctamente.");
            } else {
                System.out.println(
                        "Cambio realizado solo en esta sesion.");
                System.out.println(
                        "Usa la opcion 8 para intentar guardar nuevamente.");
            }

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha invalida. Usa el formato AAAA-MM-DD.");
        }
    }

    public static int readInt(Scanner scanner, String message) {

        System.out.print(message);

        while (!scanner.hasNextInt()) {
            System.out.println("Introduce un numero entero valido.");
            scanner.nextLine();
            System.out.print(message);
        }

        int value = scanner.nextInt();
        scanner.nextLine();

        return value;
    }
}
