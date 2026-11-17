package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.CustomerController;
import ec.edu.espe.eurotoldo.model.Customer;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class CustomerView {

    private final CustomerController controller;
    private final ConsoleInput input;

    public CustomerView(
            CustomerController controller,
            ConsoleInput input) {

        this.controller = controller;
        this.input = input;
    }

    public void registerCustomer() {

        int id = input.readInt("Customer ID: ");

        if (controller.findCustomerById(id) != null) {
            System.out.println(
                    "A customer with that ID already exists.");
            return;
        }

        try {
            Customer customer = readCustomerData(id);

            if (!controller.addCustomer(customer)) {
                System.out.println(
                        "The customer could not be registered.");
                return;
            }

            if (controller.saveCustomers()) {
                System.out.println(
                        "Customer registered successfully.");
            } else {
                System.out.println(
                        "The customer was registered, "
                        + "but could not be saved.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCustomer() {

        int id = input.readInt("Customer ID: ");

        if (controller.findCustomerById(id) == null) {
            System.out.println(
                    "The customer was not found.");
            return;
        }

        try {
            Customer customer = readCustomerData(id);

            if (!controller.updateCustomer(customer)) {
                System.out.println(
                        "The customer could not be updated.");
                return;
            }

            if (controller.saveCustomers()) {
                System.out.println(
                        "Customer updated successfully.");
            } else {
                System.out.println(
                        "The customer was updated, "
                        + "but could not be saved.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findCustomer() {

        int id = input.readInt("Customer ID: ");
        Customer customer = controller.findCustomerById(id);

        if (customer == null) {
            System.out.println(
                    "The customer was not found.");
            return;
        }

        showCustomerInformation(customer);
    }

    public void removeCustomer() {

        int id = input.readInt("Customer ID: ");

        if (!controller.removeCustomer(id)) {
            System.out.println(
                    "The customer was not found.");
            return;
        }

        if (controller.saveCustomers()) {
            System.out.println(
                    "Customer removed successfully.");
        } else {
            System.out.println(
                    "The customer was removed, "
                    + "but the changes could not be saved.");
        }
    }

    
    public void showCustomers() {

    if (controller.getCustomers().isEmpty()) {
        System.out.println(
                "There are no registered customers.");
        return;
    }

    String border = "+------+"
            + "----------------------+"
            + "-----------------+"
            + "--------------------------+"
            + "------------------+";

    System.out.println("\nREGISTERED CUSTOMERS");
    System.out.println(border);

    System.out.printf(
            "| %-4s | %-20s | %-15s | %-24s | %-16s |%n",
            "ID",
            "Name",
            "Phone",
            "Email",
            "City"
    );

    System.out.println(border);

    for (Customer customer
            : controller.getCustomers()) {

        System.out.printf(
                "| %-4d | %-20s | %-15s | %-24s | %-16s |%n",
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getCity()
        );
    }

    System.out.println(border);
}
    

    private Customer readCustomerData(int id) {

        String name = input.readRequiredText(
                "Customer name: ");

        String phone = input.readRequiredText(
                "Customer phone: ");

        String email = input.readRequiredText(
                "Customer email: ");

        String city = input.readRequiredText(
                "Customer city: ");

        return new Customer(
                id,
                name,
                phone,
                email,
                city
        );
    }

    private void showCustomerInformation(
            Customer customer) {

        System.out.println("\nCUSTOMER INFORMATION");
        System.out.println("ID: " + customer.getId());
        System.out.println(
                "Name: " + customer.getName());
        System.out.println(
                "Phone: " + customer.getPhone());
        System.out.println(
                "Email: " + customer.getEmail());
        System.out.println(
                "City: " + customer.getCity());
    }
}