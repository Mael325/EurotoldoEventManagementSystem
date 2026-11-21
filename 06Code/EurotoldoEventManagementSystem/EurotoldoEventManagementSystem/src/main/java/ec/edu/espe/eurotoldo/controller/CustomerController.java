package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Customer;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class CustomerController {

    private final ArrayList<Customer> customers;
    private final CustomerApiClient apiClient;

    public CustomerController() {
        customers = new ArrayList<>();
        apiClient = new CustomerApiClient();
    }

    public boolean loadCustomers() {

        ArrayList<Customer> loadedCustomers
                = apiClient.getCustomers();

        if (loadedCustomers == null) {
            return false;
        }

        customers.clear();

        for (Customer customer : loadedCustomers) {

            if (!isValid(customer)
                    || findCustomerById(customer.getId()) != null) {

                customers.clear();
                return false;
            }

            customers.add(customer);
        }

        return true;
    }

    public boolean addCustomer(Customer customer) {

        if (!isValid(customer)) {
            return false;
        }

        if (findCustomerById(customer.getId()) != null) {
            return false;
        }

        if (!apiClient.addCustomer(customer)) {
            return false;
        }

        customers.add(customer);
        return true;
    }

    public Customer findCustomerById(int id) {

        for (Customer customer : customers) {

            if (customer.getId() == id) {
                return customer;
            }
        }

        return null;
    }

    public boolean updateCustomer(Customer customer) {

        if (!isValid(customer)) {
            return false;
        }

        Customer existingCustomer
                = findCustomerById(customer.getId());

        if (existingCustomer == null) {
            return false;
        }

        if (!apiClient.updateCustomer(customer)) {
            return false;
        }

        existingCustomer.setName(customer.getName());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setCity(customer.getCity());

        return true;
    }

    public boolean removeCustomer(int id) {

        Customer customer = findCustomerById(id);

        if (customer == null) {
            return false;
        }

        if (!apiClient.deleteCustomer(id)) {
            return false;
        }

        return customers.remove(customer);
    }

    public ArrayList<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    /*
     * Se conserva temporalmente porque CustomerView todavía
     * utiliza este método. Los cambios ya son guardados por la API.
     */
    public boolean saveCustomers() {
        return true;
    }

    private boolean isValid(Customer customer) {

        if (customer == null || customer.getId() <= 0) {
            return false;
        }

        if (customer.getName() == null
                || customer.getName().isBlank()) {
            return false;
        }

        if (customer.getPhone() == null
                || customer.getPhone().isBlank()) {
            return false;
        }

        if (customer.getEmail() == null
                || customer.getEmail().isBlank()
                || !customer.getEmail().contains("@")) {
            return false;
        }

        return customer.getCity() != null
                && !customer.getCity().isBlank();
    }
}