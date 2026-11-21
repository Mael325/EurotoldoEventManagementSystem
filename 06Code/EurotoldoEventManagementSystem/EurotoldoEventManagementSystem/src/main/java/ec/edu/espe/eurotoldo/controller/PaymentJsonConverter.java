package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.CashPayment;
import ec.edu.espe.eurotoldo.model.Payment;
import ec.edu.espe.eurotoldo.model.TransferPayment;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentJsonConverter {

    private final Gson gson;

    public PaymentJsonConverter() {
        gson = GsonFactory.createGson();
    }

    public JsonObject toJsonObject(
            Payment payment) {

        if (payment == null) {
            return null;
        }

        JsonObject jsonObject = gson.toJsonTree(
                payment
        ).getAsJsonObject();

        jsonObject.addProperty(
                "paymentType",
                payment.getPaymentMethod()
        );

        return jsonObject;
    }

    public String toJson(Payment payment) {

        JsonObject paymentJson
                = toJsonObject(payment);

        if (paymentJson == null) {
            return null;
        }

        return gson.toJson(paymentJson);
    }

    public Payment fromJsonObject(
            JsonObject json) {

        if (json == null
                || !json.has("paymentType")) {

            throw new JsonParseException(
                    "El pago no contiene paymentType."
            );
        }

        String paymentType = json
                .get("paymentType")
                .getAsString();

        if ("Cash".equals(paymentType)) {

            return gson.fromJson(
                    json,
                    CashPayment.class
            );
        }

        if ("Transfer".equals(paymentType)) {

            return gson.fromJson(
                    json,
                    TransferPayment.class
            );
        }

        throw new JsonParseException(
                "Tipo de pago desconocido: "
                + paymentType
        );
    }

    public Payment fromJsonElement(
            JsonElement element) {

        if (element == null
                || !element.isJsonObject()) {

            throw new JsonParseException(
                    "El elemento del pago es inválido."
            );
        }

        return fromJsonObject(
                element.getAsJsonObject()
        );
    }

    public Payment fromJson(String json) {

        JsonObject paymentJson = JsonParser
                .parseString(json)
                .getAsJsonObject();

        return fromJsonObject(paymentJson);
    }

    public ArrayList<Payment> paymentsFromJson(
            String json) {

        JsonArray paymentsJson = JsonParser
                .parseString(json)
                .getAsJsonArray();

        ArrayList<Payment> payments
                = new ArrayList<>();

        for (JsonElement element : paymentsJson) {

            payments.add(
                    fromJsonElement(element)
            );
        }

        return payments;
    }
}