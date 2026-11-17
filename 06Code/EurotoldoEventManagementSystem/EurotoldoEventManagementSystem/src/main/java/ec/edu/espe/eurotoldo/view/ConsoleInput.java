package ec.edu.espe.eurotoldo.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class ConsoleInput {

    private final Scanner scanner;

    private final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm"
            );

    public ConsoleInput() {
        scanner = new Scanner(System.in);
    }

    public int readInt(String message) {

        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Enter a valid integer number.");
            }
        }
    }

    public double readDouble(String message) {

        while (true) {
            System.out.print(message);

            String input = scanner.nextLine()
                    .trim()
                    .replace(",", ".");

            try {
                return Double.parseDouble(input);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Enter a valid decimal number.");
            }
        }
    }

    public String readText(String message) {

        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public String readRequiredText(String message) {

        while (true) {
            System.out.print(message);
            String text = scanner.nextLine().trim();

            if (!text.isEmpty()) {
                return text;
            }

            System.out.println(
                    "This information cannot be empty.");
        }
    }

    public LocalDate readDate(String message) {

        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date. Use YYYY-MM-DD.");
            }
        }
    }

    public LocalDateTime readDateTime(String message) {

        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return LocalDateTime.parse(
                        input,
                        dateTimeFormatter
                );

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date and time. "
                        + "Use YYYY-MM-DD HH:MM.");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}