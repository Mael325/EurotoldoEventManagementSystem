package ec.edu.espe.eurotoldo.view;

import ec.edu.espe.eurotoldo.controller.CustomerController;
import ec.edu.espe.eurotoldo.model.Customer;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
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

        int id = input.readInt("ID del cliente: ");

        if (id <= 0) {
            System.out.println(
                    "El ID debe ser mayor que cero.");
            return;
        }

        if (controller.findCustomerById(id) != null) {
            System.out.println(
                    "Ya existe un cliente con ese ID.");
            return;
        }

        try {
            Customer customer = readCustomerData(id);

            if (controller.addCustomer(customer)) {
                System.out.println(
                        "Cliente registrado correctamente.");
            } else {
                System.out.println(
                        "No se pudo registrar el cliente. "
                        + "Verifique la conexión con la API.");
            }

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void updateCustomer() {

        int id = input.readInt("ID del cliente: ");

        if (controller.findCustomerById(id) == null) {
            System.out.println(
                    "No se encontró el cliente.");
            return;
        }

        try {
            Customer customer = readCustomerData(id);

            if (controller.updateCustomer(customer)) {
                System.out.println(
                        "Cliente actualizado correctamente.");
            } else {
                System.out.println(
                        "No se pudo actualizar el cliente. "
                        + "Verifique la conexión con la API.");
            }

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void findCustomer() {

        int id = input.readInt("ID del cliente: ");

        Customer customer
                = controller.findCustomerById(id);

        if (customer == null) {
            System.out.println(
                    "No se encontró el cliente.");
            return;
        }

        showCustomerInformation(customer);
    }

    public void removeCustomer() {

        int id = input.readInt("ID del cliente: ");

        if (controller.findCustomerById(id) == null) {
            System.out.println(
                    "No se encontró el cliente.");
            return;
        }

        if (controller.removeCustomer(id)) {
            System.out.println(
                    "Cliente eliminado correctamente.");
        } else {
            System.out.println(
                    "No se pudo eliminar el cliente. "
                    + "Verifique la conexión con la API.");
        }
    }

    public void showCustomers() {

        if (controller.getCustomers().isEmpty()) {
            System.out.println(
                    "No existen clientes registrados.");
            return;
        }

        String border = "+------+"
                + "----------------------+"
                + "-----------------+"
                + "--------------------------+"
                + "------------------+";

        System.out.println("\nCLIENTES REGISTRADOS");
        System.out.println(border);

        System.out.printf(
                "| %-4s | %-20s | %-15s | %-24s | %-16s |%n",
                "ID",
                "Nombre",
                "Teléfono",
                "Correo",
                "Ciudad"
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
                "Nombre del cliente: ");

        String phone = input.readRequiredText(
                "Teléfono del cliente: ");

        String email = input.readRequiredText(
                "Correo del cliente: ");

        String city = input.readRequiredText(
                "Ciudad del cliente: ");

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

        System.out.println("\nINFORMACIÓN DEL CLIENTE");
        System.out.println("ID: " + customer.getId());
        System.out.println(
                "Nombre: " + customer.getName());
        System.out.println(
                "Teléfono: " + customer.getPhone());
        System.out.println(
                "Correo: " + customer.getEmail());
        System.out.println(
                "Ciudad: " + customer.getCity());
    }
}