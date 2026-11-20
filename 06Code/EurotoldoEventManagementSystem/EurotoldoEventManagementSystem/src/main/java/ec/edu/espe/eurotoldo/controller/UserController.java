package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.User;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class UserController {

    private ArrayList<User> users;
    private final UserFileManager fileManager;

    public UserController() {
        this.fileManager = new UserFileManager();
        this.users = new ArrayList<>();
        loadUsers();
    }

    public boolean loadUsers() {
        this.users = fileManager.loadUsers();
        return this.users != null;
    }

    public boolean saveUsers() {
        return fileManager.saveUsers(this.users);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public boolean hasAdministrator() {
        for (User user : users) {
            if (user.isAdministrator() && user.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        if (findUserById(user.getId()) != null) {
            System.out.println("Error: Ya existe un usuario con el ID " + user.getId() + ".");
            return false;
        }
        if (findUserByUsername(user.getUsername()) != null) {
            System.out.println("Error: El nombre de usuario '" + user.getUsername() + "' ya existe.");
            return false;
        }
        users.add(user);
        return saveUsers();
    }

    public User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {
                return user;
            }
        }
        return null;
    }

    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        User user = findUserByUsername(username);
        if (user == null || !user.isActive() || !user.checkPassword(password)) {
            return null;
        }
        return user;
    }

    public boolean updateUser(User updatedUser) {
        if (updatedUser == null) {
            return false;
        }
        User existingUser = findUserById(updatedUser.getId());
        if (existingUser == null) {
            return false;
        }

        User duplicate = findUserByUsername(updatedUser.getUsername());
        if (duplicate != null && duplicate.getId() != updatedUser.getId()) {
            System.out.println("Error: El nombre de usuario '" + updatedUser.getUsername() + "' ya está en uso.");
            return false;
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setActive(updatedUser.isActive());

        return saveUsers();
    }

    public boolean deactivateUser(int id) {
        User user = findUserById(id);
        if (user == null) {
            return false;
        }
        if (user.isAdministrator()) {
            long activeAdmins = users.stream()
                    .filter(u -> u.isAdministrator() && u.isActive())
                    .count();
            if (activeAdmins <= 1) {
                System.out.println("Error: No se puede desactivar el único administrador activo.");
                return false;
            }
        }
        user.setActive(false);
        return saveUsers();
    }
}