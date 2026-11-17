package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ec.edu.espe.eurotoldo.model.Customer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class CustomerFileManager {

    private final Path filePath = Path.of("customers.json");

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public boolean saveCustomers(
            ArrayList<Customer> customers) {

        if (customers == null) {
            return false;
        }

        try {
            String json = gson.toJson(customers);

            Files.writeString(
                    filePath,
                    json,
                    StandardCharsets.UTF_8
            );

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Customer> loadCustomers() {

        try {
            String json = Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
            );

            ArrayList<Customer> customers
                    = gson.fromJson(
                            json,
                            new TypeToken<ArrayList<Customer>>() {
                            }.getType()
                    );

            if (customers == null) {
                return new ArrayList<>();
            }

            return customers;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();

        } catch (IOException | JsonParseException e) {
            return null;
        }
    }
}