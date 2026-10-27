package ec.edu.espe.eurotoldo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Alvarez Juan, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {

    private int id;
    private String name;
    private String username;

    @ToString.Exclude
    private String passwordHash;

    private String role;
    private boolean active;
}