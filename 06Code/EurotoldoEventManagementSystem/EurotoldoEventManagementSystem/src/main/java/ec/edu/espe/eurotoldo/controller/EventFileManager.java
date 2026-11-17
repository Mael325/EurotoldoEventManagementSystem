package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ec.edu.espe.eurotoldo.model.Event;
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
public class EventFileManager {

    private final Path filePath
            = Path.of("events.json");

    private final Gson gson
            = GsonFactory.createGson();

    public boolean saveEvents(
            ArrayList<Event> events) {

        if (events == null) {
            return false;
        }

        try {
            String json = gson.toJson(events);

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

    public ArrayList<Event> loadEvents() {

        try {
            String json = Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
            );

            ArrayList<Event> events = gson.fromJson(
                    json,
                    new TypeToken<ArrayList<Event>>() {
                    }.getType()
            );

            if (events == null) {
                return null;
            }

            updateOldEvents(events);
            return events;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();

        } catch (IOException | JsonParseException e) {
            return null;
        }
    }

    private void updateOldEvents(
            ArrayList<Event> events) {

        for (Event event : events) {

            if (event.getEventType() == null
                    || event.getEventType().isBlank()) {

                event.setEventType("Other");
            }
        }
    }
}