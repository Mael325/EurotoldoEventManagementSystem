package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ec.edu.espe.eurotoldo.model.Reservation;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class ReservationApiClient extends ApiClient {

    private static final String RESERVATIONS_URL
            = "http://localhost:3000/api/reservations";

    private final Gson gson;
    private final ReservationJsonConverter converter;

    public ReservationApiClient() {

        gson = GsonFactory.createGson();
        converter = new ReservationJsonConverter(gson);
    }

    public ArrayList<Reservation> getReservations() {

        ApiResponse response = sendRequest(
                "GET", RESERVATIONS_URL, null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            return converter.reservationsFromJson(
                    response.getBody()
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public Reservation getReservationById(int id) {

        ApiResponse response = sendRequest(
                "GET",
                RESERVATIONS_URL + "/" + id,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            JsonObject reservationJson = gson.fromJson(
                    response.getBody(),
                    JsonObject.class
            );

            return converter.reservationFromJson(
                    reservationJson
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public boolean addReservation(
            Reservation reservation) {

        if (reservation == null) {
            return false;
        }

        JsonObject reservationJson
                = converter.reservationToJson(
                        reservation
                );

        ApiResponse response = sendRequest(
                "POST",
                RESERVATIONS_URL,
                reservationJson.toString()
        );

        return response != null
                && response.getStatusCode() == 201;
    }

    public boolean updateReservation(
            Reservation reservation) {

        if (reservation == null) {
            return false;
        }

        JsonObject reservationJson
                = converter.reservationToJson(
                        reservation
                );

        ApiResponse response = sendRequest(
                "PUT",
                RESERVATIONS_URL + "/"
                + reservation.getId(),
                reservationJson.toString()
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean cancelReservation(int id) {

        ApiResponse response = sendRequest(
                "PUT",
                RESERVATIONS_URL + "/"
                + id + "/cancel",
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean completeReservation(int id) {

        ApiResponse response = sendRequest(
                "PUT",
                RESERVATIONS_URL + "/"
                + id + "/complete",
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean deleteReservation(int id) {

        ApiResponse response = sendRequest(
                "DELETE",
                RESERVATIONS_URL + "/" + id,
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }
}