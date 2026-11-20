package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.UserController;
import ec.edu.espe.eurotoldo.model.User;
import ec.edu.espe.eurotoldo.model.UserRole;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class LoginView {

    private final UserController userController;
    private final ConsoleInput input;

    public LoginView(UserController userController, ConsoleInput input) {
        this.userController = userController;
        this.input = input;
    }

    public User authenticate() {
        if (userController.getUsers().isEmpty() || !userController.hasAdministrator()) {
            registerFirstAdministrator();
        }

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("       INICIO DE SESIÓN - EUROTOLDO       ");
            System.out.println("==========================================");
            String username = input.readRequiredText("Usuario: ");
            String password = input.readRequiredText("Contraseña: ");

            User loggedUser = userController.login(username, password);
            if (loggedUser != null) {
                System.out.println("\nBienvenido/a, " + loggedUser.getFullName() + ".");
                return loggedUser;
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }
        }
    }

    private void registerFirstAdministrator() {
        System.out.println("\nNo existe un administrador.");
        System.out.println("Registre el primer administrador.");

        while (true) {
            try {
                int id = input.readInt("ID: ");
                String fullName = input.readRequiredText("Nombre completo: ");
                String username = input.readRequiredText("Usuario: ");
                String password = input.readRequiredText("Contraseña: ");

                User admin = new User();
                admin.setId(id);
                admin.setFullName(fullName);
                admin.setUsername(username);
                admin.setPassword(password);
                admin.setRole(UserRole.ADMINISTRATOR);
                admin.setActive(true);

                if (userController.addUser(admin)) {
                    System.out.println("Primer administrador registrado correctamente.\n");
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}