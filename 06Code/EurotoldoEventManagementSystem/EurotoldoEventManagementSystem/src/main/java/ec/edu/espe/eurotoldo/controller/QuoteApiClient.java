package ec.edu.espe.eurotoldo.controller;

import com.google.gson.JsonParseException;
import ec.edu.espe.eurotoldo.model.Quote;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class QuoteApiClient extends ApiClient {

    private static final String QUOTES_URL
            = "http://localhost:3000/api/quotes";

    private final QuoteJsonConverter converter;

    public QuoteApiClient() {
        converter = new QuoteJsonConverter();
    }

    public ArrayList<Quote> getQuotes() {

        ApiResponse response = sendRequest(
                "GET",
                QUOTES_URL,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {

            return null;
        }

        try {
            return converter.quotesFromJson(
                    response.getBody()
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public Quote getQuoteById(int id) {

        ApiResponse response = sendRequest(
                "GET",
                QUOTES_URL + "/" + id,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {

            return null;
        }

        try {
            return converter.quoteFromJson(
                    response.getBody()
            );

        } catch (JsonParseException
                | IllegalArgumentException
                | IllegalStateException exception) {

            return null;
        }
    }

    public boolean addQuote(Quote quote) {

        String json = converter.quoteToJson(quote);

        ApiResponse response = sendRequest(
                "POST",
                QUOTES_URL,
                json
        );

        return response != null
                && response.getStatusCode() == 201;
    }

    public boolean updateQuote(Quote quote) {

        String json = converter.quoteToJson(quote);

        ApiResponse response = sendRequest(
                "PUT",
                QUOTES_URL + "/" + quote.getId(),
                json
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean approveQuote(int id) {

        ApiResponse response = sendRequest(
                "PUT",
                QUOTES_URL + "/" + id + "/approve",
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean deleteQuote(int id) {

        ApiResponse response = sendRequest(
                "DELETE",
                QUOTES_URL + "/" + id,
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }
}