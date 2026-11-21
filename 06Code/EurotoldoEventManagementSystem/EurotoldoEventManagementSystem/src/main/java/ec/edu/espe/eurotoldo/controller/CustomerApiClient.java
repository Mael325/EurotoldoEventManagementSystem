package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ec.edu.espe.eurotoldo.model.Customer;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class CustomerApiClient extends ApiClient {

    private static final String CUSTOMERS_URL
            = "http://localhost:3000/api/customers";

    private final Gson gson;

    public CustomerApiClient() {
        gson = GsonFactory.createGson();
    }

    public ArrayList<Customer> getCustomers() {

        ApiResponse response = sendRequest(
                "GET", CUSTOMERS_URL, null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            ArrayList<Customer> customers = gson.fromJson(
                    response.getBody(),
                    new TypeToken<ArrayList<Customer>>() {
                    }.getType()
            );

            if (customers == null) {
                return new ArrayList<>();
            }

            return customers;

        } catch (JsonParseException exception) {
            return null;
        }
    }

    public Customer getCustomerById(int id) {

        ApiResponse response = sendRequest(
                "GET",
                CUSTOMERS_URL + "/" + id,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            return gson.fromJson(
                    response.getBody(),
                    Customer.class
            );

        } catch (JsonParseException exception) {
            return null;
        }
    }

    public boolean addCustomer(Customer customer) {

        ApiResponse response = sendRequest(
                "POST",
                CUSTOMERS_URL,
                gson.toJson(customer)
        );

        return response != null
                && response.getStatusCode() == 201;
    }

    public boolean updateCustomer(Customer customer) {

        ApiResponse response = sendRequest(
                "PUT",
                CUSTOMERS_URL + "/" + customer.getId(),
                gson.toJson(customer)
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean deleteCustomer(int id) {

        ApiResponse response = sendRequest(
                "DELETE",
                CUSTOMERS_URL + "/" + id,
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }
}