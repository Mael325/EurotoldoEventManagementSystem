package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ec.edu.espe.eurotoldo.model.CashPayment;
import ec.edu.espe.eurotoldo.model.Payment;
import ec.edu.espe.eurotoldo.model.TransferPayment;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentJsonConverter {

    private final Gson gson;

    public PaymentJsonConverter() {
        this.gson = GsonFactory.createGson();
    }

    public JsonObject toJsonObject(Payment payment) {
        if (payment == null) {
            return null;
        }
        JsonObject jsonObject = gson.toJsonTree(payment).getAsJsonObject();
        jsonObject.addProperty("paymentType", payment.getPaymentMethod());
        return jsonObject;
    }

    public Payment fromJsonObject(JsonObject json) {
        if (json == null || !json.has("paymentType")) {
            throw new JsonParseException("El registro JSON no contiene el campo 'paymentType'.");
        }

        String paymentType = json.get("paymentType").getAsString();

        if ("Cash".equals(paymentType)) {
            return gson.fromJson(json, CashPayment.class);
        }

        if ("Transfer".equals(paymentType)) {
            return gson.fromJson(json, TransferPayment.class);
        }

        throw new JsonParseException("Tipo de pago desconocido: " + paymentType);
    }

    public Payment fromJsonElement(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            throw new JsonParseException("Elemento JSON inválido para Payment.");
        }
        return fromJsonObject(element.getAsJsonObject());
    }
}