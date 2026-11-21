package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.Reservation;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class ReservationJsonConverter {

    private final Gson gson;
    private final AssignmentJsonConverter assignmentConverter;

    public ReservationJsonConverter(Gson gson) {

        this.gson = gson;
        assignmentConverter
                = new AssignmentJsonConverter(gson);
    }

    public JsonObject reservationToJson(
            Reservation reservation) {

        JsonObject reservationJson
                = gson.toJsonTree(
                        reservation,
                        Reservation.class
                ).getAsJsonObject();

        reservationJson.add(
                "assignments",
                assignmentConverter.assignmentsToJson(
                        reservation.getAssignments()
                )
        );

        return reservationJson;
    }

    public Reservation reservationFromJson(
            JsonObject reservationJson) {

        Reservation reservation = gson.fromJson(
                reservationJson,
                Reservation.class
        );

        JsonArray assignmentsJson = new JsonArray();

        if (reservationJson.has("assignments")
                && reservationJson
                        .get("assignments")
                        .isJsonArray()) {

            assignmentsJson = reservationJson
                    .getAsJsonArray("assignments");
        }

        reservation.setAssignments(
                assignmentConverter.assignmentsFromJson(
                        assignmentsJson
                )
        );

        if (reservation.getEvent() != null
                && (reservation.getEvent()
                        .getEventType() == null
                || reservation.getEvent()
                        .getEventType().isBlank())) {

            reservation.getEvent()
                    .setEventType("Other");
        }

        if (!reservationJson.has("totalAmount")) {
            reservation.setTotalAmount(
                    reservation.calculateTotal()
            );
        }

        return reservation;
    }

    public ArrayList<Reservation>
            reservationsFromJson(String json) {

        JsonArray reservationsJson = JsonParser
                .parseString(json)
                .getAsJsonArray();

        ArrayList<Reservation> reservations
                = new ArrayList<>();

        for (JsonElement element : reservationsJson) {

            if (!element.isJsonObject()) {
                throw new IllegalArgumentException(
                        "Invalid reservation JSON."
                );
            }

            reservations.add(
                    reservationFromJson(
                            element.getAsJsonObject()
                    )
            );
        }

        return reservations;
    }
}