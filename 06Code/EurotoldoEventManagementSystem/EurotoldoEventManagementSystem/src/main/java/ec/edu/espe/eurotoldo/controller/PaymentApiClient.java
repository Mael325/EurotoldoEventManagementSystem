package ec.edu.espe.eurotoldo.controller;

import com.google.gson.JsonParseException;
import ec.edu.espe.eurotoldo.model.Payment;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class PaymentApiClient extends ApiClient {

    private static final String PAYMENTS_URL
            = "http://localhost:3000/api/payments";

    private final PaymentJsonConverter converter;

    public PaymentApiClient() {
        converter = new PaymentJsonConverter();
    }

    public ArrayList<Payment> getPayments() {

        ApiResponse response = sendRequest(
                "GET",
                PAYMENTS_URL,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {

            return null;
        }

        try {
            return converter.paymentsFromJson(
                    response.getBody()
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public Payment getPaymentById(int id) {

        ApiResponse response = sendRequest(
                "GET",
                PAYMENTS_URL + "/" + id,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {

            return null;
        }

        try {
            return converter.fromJson(
                    response.getBody()
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public ArrayList<Payment> getPaymentsByReservation(
            int reservationId) {

        ApiResponse response = sendRequest(
                "GET",
                PAYMENTS_URL
                + "/reservation/"
                + reservationId,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {

            return null;
        }

        try {
            return converter.paymentsFromJson(
                    response.getBody()
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public boolean addPayment(Payment payment) {

        String json = converter.toJson(payment);

        if (json == null) {
            return false;
        }

        ApiResponse response = sendRequest(
                "POST",
                PAYMENTS_URL,
                json
        );

        return response != null
                && response.getStatusCode() == 201;
    }

    public boolean deletePayment(int id) {

        ApiResponse response = sendRequest(
                "DELETE",
                PAYMENTS_URL + "/" + id,
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }
}