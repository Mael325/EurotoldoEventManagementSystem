package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.CustomerController;
import ec.edu.espe.eurotoldo.controller.EquipmentController;
import ec.edu.espe.eurotoldo.controller.EventController;
import ec.edu.espe.eurotoldo.controller.QuoteController;
import ec.edu.espe.eurotoldo.controller.ReservationController;
import ec.edu.espe.eurotoldo.controller.UserController;
import ec.edu.espe.eurotoldo.model.User;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EurotoldoApp {

    private final ConsoleInput input;
    private User loggedUser;

    private final CustomerController customerController;
    private final EquipmentController equipmentController;
    private final EventController eventController;
    private final QuoteController quoteController;
    private final ReservationController reservationController;
    private final UserController userController;

    private final CustomerView customerView;
    private final EquipmentView equipmentView;
    private final EventView eventView;
    private final QuoteView quoteView;
    private final ReservationView reservationView;
    private final LoginView loginView;
    private final UserView userView;

    public EurotoldoApp() {

        input = new ConsoleInput();

        customerController = new CustomerController();
        equipmentController = new EquipmentController();
        eventController = new EventController();
        userController = new UserController();

        quoteController = new QuoteController(
                customerController,
                eventController,
                equipmentController
        );

        reservationController = new ReservationController(
                quoteController,
                equipmentController
        );

        customerView = new CustomerView(
                customerController,
                input
        );

        equipmentView = new EquipmentView(
                equipmentController,
                input
        );

        eventView = new EventView(
                eventController,
                input
        );

        quoteView = new QuoteView(
                quoteController,
                input
        );

        reservationView = new ReservationView(
                reservationController,
                input
        );

        loginView = new LoginView(
                userController,
                input
        );

        userView = new UserView(
                userController,
                input
        );
    }

    public static void main(String[] args) {

        EurotoldoApp app = new EurotoldoApp();
        app.run();
    }

    public void run() {

        if (!loadData()) {
            input.close();
            return;
        }

        loggedUser = loginView.authenticate();

        int option;

        do {
            if (loggedUser.isAdministrator()) {
                showAdministratorMenu();
            } else {
                showOperationalMenu();
            }

            option = input.readInt("Seleccione una opcion: ");

            executeOption(option);

        } while (option != 0);

        input.close();
    }

    private boolean loadData() {

        if (!customerController.loadCustomers()) {
            System.out.println("Error al cargar customers.json.");
            return false;
        }

        if (!equipmentController.loadEquipment()) {
            System.out.println("Error al cargar equipment.json.");
            return false;
        }

        if (!eventController.loadEvents()) {
            System.out.println("Error al cargar events.json.");
            return false;
        }

        if (!quoteController.loadQuotes()) {
            System.out.println("Error al cargar quotes.json.");
            return false;
        }

        if (!reservationController.loadReservations()) {
            System.out.println("Error al cargar reservations.json.");
            return false;
        }

        if (!userController.loadUsers()) {
            System.out.println("Error al cargar users.json.");
            return false;
        }

        return true;
    }

    private void showAdministratorMenu() {

        System.out.println("\n+------------------------------------------+");
        System.out.println("|                EUROTOLDO                 |");
        System.out.println("|       SISTEMA DE GESTIÓN DE EVENTOS      |");
        System.out.println("|  Rol: ADMINISTRADOR                      |");
        System.out.println("+------------------------------------------+");

        System.out.println("| CLIENTES                                 |");
        System.out.println("|   [1] Registrar cliente                  |");
        System.out.println("|   [2] Ver clientes                       |");
        System.out.println("|   [3] Buscar cliente                     |");
        System.out.println("|   [4] Actualizar cliente                 |");
        System.out.println("|   [5] Eliminar cliente                   |");
        System.out.println("+------------------------------------------+");

        System.out.println("| EQUIPOS                                  |");
        System.out.println("|   [6] Registrar equipo                   |");
        System.out.println("|   [7] Ver equipos                        |");
        System.out.println("+------------------------------------------+");

        System.out.println("| EVENTOS                                  |");
        System.out.println("|   [8] Registrar evento                   |");
        System.out.println("|   [9] Ver eventos                        |");
        System.out.println("|  [10] Buscar evento                      |");
        System.out.println("|  [11] Actualizar evento                  |");
        System.out.println("|  [12] Eliminar evento                    |");
        System.out.println("|  [13] Completar montaje                  |");
        System.out.println("|  [14] Completar retiro                   |");
        System.out.println("+------------------------------------------+");

        System.out.println("| COTIZACIONES                             |");
        System.out.println("|  [15] Gestionar cotizaciones             |");
        System.out.println("+------------------------------------------+");

        System.out.println("| RESERVAS                                 |");
        System.out.println("|  [16] Gestionar reservas                 |");
        System.out.println("+------------------------------------------+");

        System.out.println("| REPORTES                                 |");
        System.out.println("|  [17] Exportar reporte de equipos        |");
        System.out.println("+------------------------------------------+");

        System.out.println("| USUARIOS                                 |");
        System.out.println("|  [18] Gestionar usuarios                 |");
        System.out.println("+------------------------------------------+");

        System.out.println("|   [0] Salir                              |");
        System.out.println("+------------------------------------------+");
    }

    private void showOperationalMenu() {

        System.out.println("\n+------------------------------------------+");
        System.out.println("|                EUROTOLDO                 |");
        System.out.println("|       SISTEMA DE GESTIÓN DE EVENTOS      |");
        System.out.println("|  Rol: PERSONAL OPERATIVO                 |");
        System.out.println("+------------------------------------------+");

        System.out.println("| EQUIPOS                                  |");
        System.out.println("|   [6] Registrar equipo                   |");
        System.out.println("|   [7] Ver equipos                        |");
        System.out.println("+------------------------------------------+");

        System.out.println("| EVENTOS                                  |");
        System.out.println("|   [8] Registrar evento                   |");
        System.out.println("|   [9] Ver eventos                        |");
        System.out.println("|  [10] Buscar evento                      |");
        System.out.println("|  [11] Actualizar evento                  |");
        System.out.println("|  [12] Eliminar evento                    |");
        System.out.println("|  [13] Completar montaje                  |");
        System.out.println("|  [14] Completar retiro                   |");
        System.out.println("+------------------------------------------+");

        System.out.println("| RESERVAS                                 |");
        System.out.println("|  [16] Gestionar reservas                 |");
        System.out.println("+------------------------------------------+");

        System.out.println("|   [0] Salir                              |");
        System.out.println("+------------------------------------------+");
    }

    private void executeOption(int option) {

        // Bloqueo de seguridad: impide ejecutar opciones restringidas si no es administrador
        if (!loggedUser.isAdministrator()) {
            if ((option >= 1 && option <= 5) || option == 15 || option == 17 || option == 18) {
                System.out.println("Acceso denegado: no tiene permisos para realizar esta acción.");
                return;
            }
        }

        switch (option) {

            case 1 ->
                customerView.registerCustomer();

            case 2 ->
                customerView.showCustomers();

            case 3 ->
                customerView.findCustomer();

            case 4 ->
                customerView.updateCustomer();

            case 5 ->
                customerView.removeCustomer();

            case 6 ->
                equipmentView.registerEquipment();

            case 7 ->
                equipmentView.showEquipment();

            case 8 ->
                eventView.registerEvent();

            case 9 ->
                eventView.showEvents();

            case 10 ->
                eventView.findEvent();

            case 11 ->
                eventView.updateEvent();

            case 12 ->
                eventView.removeEvent();

            case 13 ->
                eventView.markSetupCompleted();

            case 14 ->
                eventView.markPickupCompleted();

            case 15 ->
                quoteView.showMenu();

            case 16 ->
                reservationView.showMenu();

            case 17 ->
                exportEquipmentReport();

            case 18 ->
                userView.showMenu();

            case 0 -> {
                saveDataBeforeExit();
                System.out.println("Gracias por utilizar Eurotoldo.");
            }

            default ->
                System.out.println("Opción inválida. Inténtelo nuevamente.");
        }
    }

    private void exportEquipmentReport() {

        if (equipmentController.exportEquipmentReport("equipment_report.csv")) {
            System.out.println("Reporte exportado correctamente.");
        } else {
            System.out.println("No se pudo exportar el reporte.");
        }
    }

    private void saveDataBeforeExit() {

        boolean customersSaved = customerController.saveCustomers();
        boolean equipmentSaved = equipmentController.saveEquipment();
        boolean eventsSaved = eventController.saveEvents();
        boolean quotesSaved = quoteController.saveQuotes();
        boolean reservationsSaved = reservationController.saveReservations();
        boolean usersSaved = userController.saveUsers();

        if (!customersSaved
                || !equipmentSaved
                || !eventsSaved
                || !quotesSaved
                || !reservationsSaved
                || !usersSaved) {

            System.out.println("Alguna información no pudo guardarse.");
        }
    }
}
