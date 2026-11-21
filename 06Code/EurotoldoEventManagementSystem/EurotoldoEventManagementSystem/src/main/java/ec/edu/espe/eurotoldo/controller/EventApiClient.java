package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ec.edu.espe.eurotoldo.model.Event;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class EventApiClient extends ApiClient {

    private static final String EVENTS_URL
            = "http://localhost:3000/api/events";

    private final Gson gson;

    public EventApiClient() {
        gson = GsonFactory.createGson();
    }

    public ArrayList<Event> getEvents() {

        ApiResponse response = sendRequest(
                "GET", EVENTS_URL, null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            ArrayList<Event> events = gson.fromJson(
                    response.getBody(),
                    new TypeToken<ArrayList<Event>>() {
                    }.getType()
            );

            if (events == null) {
                return new ArrayList<>();
            }

            return events;

        } catch (JsonParseException exception) {
            return null;
        }
    }

    public Event getEventById(int id) {

        ApiResponse response = sendRequest(
                "GET",
                EVENTS_URL + "/" + id,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            return gson.fromJson(
                    response.getBody(),
                    Event.class
            );

        } catch (JsonParseException exception) {
            return null;
        }
    }

    public boolean addEvent(Event event) {

        if (event == null) {
            return false;
        }

        ApiResponse response = sendRequest(
                "POST",
                EVENTS_URL,
                gson.toJson(event)
        );

        return response != null
                && response.getStatusCode() == 201;
    }

    public boolean updateEvent(Event event) {

        if (event == null) {
            return false;
        }

        ApiResponse response = sendRequest(
                "PUT",
                EVENTS_URL + "/" + event.getId(),
                gson.toJson(event)
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean markSetupCompleted(int id) {

        ApiResponse response = sendRequest(
                "PUT",
                EVENTS_URL + "/" + id
                + "/setup-completed",
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean markPickupCompleted(int id) {

        ApiResponse response = sendRequest(
                "PUT",
                EVENTS_URL + "/" + id
                + "/pickup-completed",
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean deleteEvent(int id) {

        ApiResponse response = sendRequest(
                "DELETE",
                EVENTS_URL + "/" + id,
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }
}