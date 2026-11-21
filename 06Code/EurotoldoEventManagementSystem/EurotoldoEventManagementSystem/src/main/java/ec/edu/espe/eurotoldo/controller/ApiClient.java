package ec.edu.espe.eurotoldo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public abstract class ApiClient {

    protected ApiResponse sendRequest(
            String method, String address, String json) {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(address);

            connection = (HttpURLConnection)
                    url.openConnection();

            connection.setRequestMethod(method);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty(
                    "Accept", "application/json"
            );

            if (json != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                try (OutputStream output
                        = connection.getOutputStream()) {

                    output.write(json.getBytes(
                            StandardCharsets.UTF_8
                    ));
                }
            }

            int statusCode = connection.getResponseCode();

            InputStream input;

            if (statusCode >= 200 && statusCode < 300) {
                input = connection.getInputStream();
            } else {
                input = connection.getErrorStream();
            }

            return new ApiResponse(
                    statusCode,
                    readBody(input)
            );

        } catch (IOException exception) {
            return null;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readBody(InputStream input)
            throws IOException {

        if (input == null) {
            return "";
        }

        StringBuilder body = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        input, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        return body.toString();
    }

    protected static class ApiResponse {

        private final int statusCode;
        private final String body;

        public ApiResponse(
                int statusCode, String body) {

            this.statusCode = statusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            return body;
        }
    }
}