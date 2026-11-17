package ec.edu.espe.eurotoldo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
@Getter
@NoArgsConstructor
@ToString
public class Customer {

    private int id;
    private String name;
    private String phone;
    private String email;
    private String city;

    public Customer(int id, String name, String phone,
            String email, String city) {

        setId(id);
        setName(name);
        setPhone(phone);
        setEmail(email);
        setCity(city);
    }

    public void setId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "The customer ID cannot be negative.");
        }

        this.id = id;
    }

    public void setName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "The customer name cannot be empty.");
        }

        this.name = name;
    }

    public void setPhone(String phone) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "The customer phone cannot be empty.");
        }

        this.phone = phone;
    }

    public void setEmail(String email) {

        if (email == null
                || email.isBlank()
                || !email.contains("@")) {

            throw new IllegalArgumentException(
                    "The customer email is invalid.");
        }

        this.email = email;
    }

    public void setCity(String city) {

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "The customer city cannot be empty.");
        }

        this.city = city;
    }
}