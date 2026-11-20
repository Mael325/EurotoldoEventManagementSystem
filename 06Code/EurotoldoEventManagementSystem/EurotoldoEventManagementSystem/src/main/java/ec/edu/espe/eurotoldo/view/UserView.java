package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.UserController;
import ec.edu.espe.eurotoldo.model.User;
import ec.edu.espe.eurotoldo.model.UserRole;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class UserView {

    private final UserController userController;
    private final ConsoleInput input;

    public UserView(UserController userController, ConsoleInput input) {
        this.userController = userController;
        this.input = input;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- GESTIÓN DE USUARIOS ---");
            System.out.println("[1] Registrar usuario");
            System.out.println("[2] Ver usuarios");
            System.out.println("[3] Buscar usuario");
            System.out.println("[4] Actualizar usuario");
            System.out.println("[5] Desactivar usuario");
            System.out.println("[0] Volver");

            option = input.readInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> registerUser();
                case 2 -> listUsers();
                case 3 -> searchUser();
                case 4 -> updateUser();
                case 5 -> deactivateUser();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (option != 0);
    }

    private void registerUser() {
        System.out.println("\n--- REGISTRAR USUARIO ---");
        try {
            int id = input.readInt("ID: ");
            String fullName = input.readRequiredText("Nombre completo: ");
            String username = input.readRequiredText("Usuario: ");
            String password = input.readRequiredText("Contraseña: ");
            UserRole role = selectRole();

            User user = new User();
            user.setId(id);
            user.setFullName(fullName);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            user.setActive(true);

            if (userController.addUser(user)) {
                System.out.println("Usuario registrado correctamente.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listUsers() {
        System.out.println("\n--- LISTA DE USUARIOS ---");
        ArrayList<User> users = userController.getUsers();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.printf("%-6s | %-18s | %-28s | %-20s | %-10s%n",
                "ID", "USUARIO", "NOMBRE COMPLETO", "ROL", "ESTADO");
        System.out.println("-------------------------------------------------------------------------------------------");
        for (User user : users) {
            String roleStr = user.isAdministrator() ? "Administrador" : "Personal Operativo";
            String statusStr = user.isActive() ? "Activo" : "Inactivo";
            System.out.printf("%-6d | %-18s | %-28s | %-20s | %-10s%n",
                    user.getId(), user.getUsername(), user.getFullName(), roleStr, statusStr);
        }
    }

    private void searchUser() {
        System.out.println("\n--- BUSCAR USUARIO ---");
        int id = input.readInt("Ingrese el ID del usuario: ");
        User user = userController.findUserById(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private void updateUser() {
        System.out.println("\n--- ACTUALIZAR USUARIO ---");
        int id = input.readInt("Ingrese el ID del usuario a actualizar: ");
        User existing = userController.findUserById(id);
        if (existing == null) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        }

        System.out.println("Usuario actual: " + existing);

        try {
            String fullName = input.readText("Nuevo nombre completo (Enter para mantener): ");
            if (fullName.isEmpty()) {
                fullName = existing.getFullName();
            }

            String username = input.readText("Nuevo usuario (Enter para mantener): ");
            if (username.isEmpty()) {
                username = existing.getUsername();
            }

            String password = input.readText("Nueva contraseña (Enter para mantener): ");
            if (password.isEmpty()) {
                password = existing.getPassword();
            }

            UserRole role = selectRole();
            String activeOpt = input.readRequiredText("¿Activo? (S/N): ");
            boolean active = activeOpt.equalsIgnoreCase("S");

            User updated = new User();
            updated.setId(id);
            updated.setFullName(fullName);
            updated.setUsername(username);
            updated.setPassword(password);
            updated.setRole(role);
            updated.setActive(active);

            if (userController.updateUser(updated)) {
                System.out.println("Usuario actualizado correctamente.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deactivateUser() {
        System.out.println("\n--- DESACTIVAR USUARIO ---");
        int id = input.readInt("Ingrese el ID del usuario a desactivar: ");
        User user = userController.findUserById(id);
        if (user == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        if (!user.isActive()) {
            System.out.println("El usuario ya está inactivo.");
            return;
        }
        if (userController.deactivateUser(id)) {
            System.out.println("Usuario desactivado correctamente.");
        }
    }

    private UserRole selectRole() {
        while (true) {
            System.out.println("Seleccione el rol:");
            System.out.println("[1] ADMINISTRATOR");
            System.out.println("[2] OPERATIONAL_STAFF");
            int choice = input.readInt("Opción: ");
            if (choice == 1) {
                return UserRole.ADMINISTRATOR;
            } else if (choice == 2) {
                return UserRole.OPERATIONAL_STAFF;
            }
            System.out.println("Opción no válida.");
        }
    }
}