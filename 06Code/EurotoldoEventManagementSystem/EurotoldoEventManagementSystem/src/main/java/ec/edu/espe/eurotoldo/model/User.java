package ec.edu.espe.eurotoldo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private int id;
    private String username;

    @ToString.Exclude
    private String password;

    private String fullName;
    private UserRole role;
    private boolean active = true;

    public User(int id, String username, String password, String fullName, UserRole role) {
        this(id, username, password, fullName, role, true);
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID del usuario no puede ser negativo.");
        }
        this.id = id;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        this.username = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        this.password = password;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío.");
        }
        this.fullName = fullName.trim();
    }

    public void setRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol del usuario no puede ser nulo.");
        }
        this.role = role;
    }

    public boolean checkPassword(String password) {
        if (password == null || this.password == null) {
            return false;
        }
        return this.password.equals(password);
    }

    public boolean isAdministrator() {
        return this.role == UserRole.ADMINISTRATOR;
    }
}