package ec.edu.espe.eurotoldo.model;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class Customer {
    
    private int id;
    private String name;
    private String phone;
    private String email;
    private String city;

    public Customer(int id, String name, String phone, String email, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", city=" + city + '}';
    }
    
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    
    
}
