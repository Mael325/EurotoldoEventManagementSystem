package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.Quote;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class QuoteJsonConverter {

    private final Gson gson;
    private final AssignmentJsonConverter assignmentConverter;

    public QuoteJsonConverter() {

        gson = GsonFactory.createGson();

        assignmentConverter
                = new AssignmentJsonConverter(gson);
    }

    public String quoteToJson(Quote quote) {

        JsonObject quoteJson = gson.toJsonTree(
                quote,
                Quote.class
        ).getAsJsonObject();

        quoteJson.add(
                "assignments",
                assignmentConverter.assignmentsToJson(
                        quote.getAssignments()
                )
        );

        return gson.toJson(quoteJson);
    }

    public Quote quoteFromJson(String json) {

        JsonObject quoteJson = JsonParser
                .parseString(json)
                .getAsJsonObject();

        return quoteFromJsonObject(quoteJson);
    }

    public ArrayList<Quote> quotesFromJson(
            String json) {

        JsonArray quotesJson = JsonParser
                .parseString(json)
                .getAsJsonArray();

        ArrayList<Quote> quotes
                = new ArrayList<>();

        for (JsonElement element : quotesJson) {

            Quote quote = quoteFromJsonObject(
                    element.getAsJsonObject()
            );

            quotes.add(quote);
        }

        return quotes;
    }

    private Quote quoteFromJsonObject(
            JsonObject quoteJson) {

        Quote quote = gson.fromJson(
                quoteJson,
                Quote.class
        );

        JsonArray assignmentsJson
                = new JsonArray();

        if (quoteJson.has("assignments")
                && quoteJson.get("assignments")
                        .isJsonArray()) {

            assignmentsJson = quoteJson
                    .getAsJsonArray("assignments");
        }

        quote.setAssignments(
                assignmentConverter.assignmentsFromJson(
                        assignmentsJson
                )
        );

        if (quote.getEvent() != null
                && (quote.getEvent().getEventType() == null
                || quote.getEvent().getEventType()
                        .isBlank())) {

            quote.getEvent().setEventType("Other");
        }

        return quote;
    }
}