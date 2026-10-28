package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ec.edu.espe.eurotoldo.model.Event;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EventFileManager {

    private final Path filePath = Path.of("events.json");
    private final Gson gson;

    public EventFileManager() {
        TypeAdapter<LocalDate> dateAdapter = new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter out, LocalDate date)
                    throws IOException {
                out.value(date.toString());
            }

            @Override
            public LocalDate read(JsonReader in) throws IOException {
                try {
                    return LocalDate.parse(in.nextString());
                } catch (DateTimeParseException | IllegalStateException e) {
                    throw new JsonParseException("Fecha invalida", e);
                }
            }
        };

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, dateAdapter.nullSafe())
                .setPrettyPrinting()
                .create();
    }

    public boolean saveEvents(ArrayList<Event> events) {
        if (events == null) {
            return false;
        }

        try {
            String json = gson.toJson(events);
            Files.writeString(filePath, json, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Event> loadEvents() {
        try {
            String json = Files.readString(
                    filePath, StandardCharsets.UTF_8);

            ArrayList<Event> events = gson.fromJson(json,
                    new TypeToken<ArrayList<Event>>() {}.getType());

            if (events == null) {
                return null;
            }

            return events;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        } catch (IOException | JsonParseException e) {
            return null;
        }
    }
}
