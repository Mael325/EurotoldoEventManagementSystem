package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.Quote;
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
public class QuoteFileManager {

    private final Path filePath
            = Path.of("quotes.json");

    private final Gson gson
            = GsonFactory.createGson();

    private final AssignmentJsonConverter converter
            = new AssignmentJsonConverter(gson);

    public boolean saveQuotes(
            ArrayList<Quote> quotes) {

        if (quotes == null) {
            return false;
        }

        JsonArray quotesJson = new JsonArray();

        for (Quote quote : quotes) {

            JsonObject quoteJson = gson.toJsonTree(
                    quote,
                    Quote.class
            ).getAsJsonObject();

            quoteJson.add(
                    "assignments",
                    converter.assignmentsToJson(
                            quote.getAssignments()
                    )
            );

            quotesJson.add(quoteJson);
        }

        try {
            Files.writeString(
                    filePath,
                    gson.toJson(quotesJson),
                    StandardCharsets.UTF_8
            );

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Quote> loadQuotes() {

        try {
            String json = Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
            );

            JsonArray quotesJson = JsonParser
                    .parseString(json)
                    .getAsJsonArray();

            ArrayList<Quote> quotes
                    = new ArrayList<>();

            for (JsonElement element : quotesJson) {

                JsonObject quoteJson
                        = element.getAsJsonObject();

                Quote quote = gson.fromJson(
                        quoteJson,
                        Quote.class
                );

                quote.setAssignments(
                        converter.assignmentsFromJson(
                                quoteJson.getAsJsonArray(
                                        "assignments"
                                )
                        )
                );

                quotes.add(quote);
            }

            return quotes;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();

        } catch (IOException
                | JsonParseException
                | IllegalArgumentException
                | IllegalStateException e) {

            return null;
        }
    }
}