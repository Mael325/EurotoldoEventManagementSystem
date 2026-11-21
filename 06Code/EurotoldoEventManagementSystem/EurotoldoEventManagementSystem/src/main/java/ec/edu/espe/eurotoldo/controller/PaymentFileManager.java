package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.Payment;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentFileManager {

    private static final String FILE_NAME = "payments.json";
    private final PaymentJsonConverter converter;
    private final Gson gson;

    public PaymentFileManager() {
        this.converter = new PaymentJsonConverter();
        this.gson = GsonFactory.createGson();
    }

    public boolean savePayments(ArrayList<Payment> payments) {
        if (payments == null) {
            return false;
        }

        JsonArray jsonArray = new JsonArray();
        for (Payment payment : payments) {
            jsonArray.add(converter.toJsonObject(payment));
        }

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(jsonArray, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar pagos en " + FILE_NAME + ": " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Payment> loadPayments() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (root == null || root.isJsonNull()) {
                return new ArrayList<>();
            }
            if (!root.isJsonArray()) {
                return null;
            }

            JsonArray jsonArray = root.getAsJsonArray();
            ArrayList<Payment> payments = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                payments.add(converter.fromJsonElement(element));
            }
            return payments;
        } catch (Exception e) {
            System.out.println("Error al leer " + FILE_NAME + ": " + e.getMessage());
            return null;
        }
    }
}