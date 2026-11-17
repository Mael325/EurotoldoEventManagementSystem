package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public final class GsonFactory {

    private GsonFactory() {
    }

    public static Gson createGson() {

        return new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        createLocalDateAdapter().nullSafe()
                )
                .registerTypeAdapter(
                        LocalDateTime.class,
                        createLocalDateTimeAdapter().nullSafe()
                )
                .setPrettyPrinting()
                .create();
    }

    private static TypeAdapter<LocalDate>
            createLocalDateAdapter() {

        return new TypeAdapter<LocalDate>() {

            @Override
            public void write(
                    JsonWriter writer,
                    LocalDate date) throws IOException {

                writer.value(date.toString());
            }

            @Override
            public LocalDate read(
                    JsonReader reader) throws IOException {

                try {
                    return LocalDate.parse(
                            reader.nextString()
                    );

                } catch (DateTimeParseException e) {
                    throw new JsonParseException(
                            "Invalid LocalDate format.",
                            e
                    );
                }
            }
        };
    }

    private static TypeAdapter<LocalDateTime>
            createLocalDateTimeAdapter() {

        return new TypeAdapter<LocalDateTime>() {

            @Override
            public void write(
                    JsonWriter writer,
                    LocalDateTime dateTime)
                    throws IOException {

                writer.value(dateTime.toString());
            }

            @Override
            public LocalDateTime read(
                    JsonReader reader) throws IOException {

                try {
                    return LocalDateTime.parse(
                            reader.nextString()
                    );

                } catch (DateTimeParseException e) {
                    throw new JsonParseException(
                            "Invalid LocalDateTime format.",
                            e
                    );
                }
            }
        };
    }
}