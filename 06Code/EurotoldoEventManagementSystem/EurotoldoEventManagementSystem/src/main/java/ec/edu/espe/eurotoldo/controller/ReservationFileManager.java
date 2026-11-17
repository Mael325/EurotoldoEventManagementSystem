package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.Reservation;
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
public class ReservationFileManager {

    private final Path filePath
            = Path.of("reservations.json");

    private final Gson gson
            = GsonFactory.createGson();

    private final AssignmentJsonConverter converter
            = new AssignmentJsonConverter(gson);

    public boolean saveReservations(
            ArrayList<Reservation> reservations) {

        if (reservations == null) {
            return false;
        }

        JsonArray reservationsJson = new JsonArray();

        for (Reservation reservation : reservations) {

            JsonObject reservationJson
                    = gson.toJsonTree(
                            reservation,
                            Reservation.class
                    ).getAsJsonObject();

            reservationJson.add(
                    "assignments",
                    converter.assignmentsToJson(
                            reservation.getAssignments()
                    )
            );

            reservationsJson.add(reservationJson);
        }

        try {
            Files.writeString(
                    filePath,
                    gson.toJson(reservationsJson),
                    StandardCharsets.UTF_8
            );

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Reservation> loadReservations() {

        try {
            String json = Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
            );

            JsonArray reservationsJson = JsonParser
                    .parseString(json)
                    .getAsJsonArray();

            ArrayList<Reservation> reservations
                    = new ArrayList<>();

            for (JsonElement element : reservationsJson) {

                JsonObject reservationJson
                        = element.getAsJsonObject();

                Reservation reservation = gson.fromJson(
                        reservationJson,
                        Reservation.class
                );

                reservation.setAssignments(
                        converter.assignmentsFromJson(
                                reservationJson.getAsJsonArray(
                                        "assignments"
                                )
                        )
                );

                updateOldReservation(
                        reservation,
                        reservationJson
                );

                reservations.add(reservation);
            }

            return reservations;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();

        } catch (IOException
                | JsonParseException
                | IllegalArgumentException
                | IllegalStateException e) {

            return null;
        }
    }

    private void updateOldReservation(
            Reservation reservation,
            JsonObject reservationJson) {

        if (reservation.getEvent() != null&& (reservation.getEvent().getEventType() == null|| reservation.getEvent()
                        .getEventType().isBlank())) {

            reservation.getEvent()
                    .setEventType("Other");
        }

        if (!reservationJson.has("totalAmount")) {

            reservation.setTotalAmount(
                    reservation.calculateTotal()
            );
        }
    }
}