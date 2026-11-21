package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Customer;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Event;
import ec.edu.espe.eurotoldo.model.Quote;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class QuoteController {

    private final ArrayList<Quote> quotes;
    private final QuoteApiClient apiClient;

    private final CustomerController customerController;
    private final EventController eventController;
    private final EquipmentController equipmentController;

    public QuoteController(
            CustomerController customerController,
            EventController eventController,
            EquipmentController equipmentController) {

        quotes = new ArrayList<>();
        apiClient = new QuoteApiClient();

        this.customerController = customerController;
        this.eventController = eventController;
        this.equipmentController = equipmentController;
    }

    public boolean loadQuotes() {

        ArrayList<Quote> loadedQuotes
                = apiClient.getQuotes();

        if (loadedQuotes == null) {
            return false;
        }

        quotes.clear();

        for (Quote quote : loadedQuotes) {

            if (!isValid(quote)
                    || findQuoteById(quote.getId()) != null) {

                quotes.clear();
                return false;
            }

            quotes.add(quote);
        }

        return true;
    }

    public boolean createQuote(
            int quoteId,
            int customerId,
            int eventId) {

        if (quoteId <= 0
                || findQuoteById(quoteId) != null) {

            return false;
        }

        Customer customer
                = customerController.findCustomerById(
                        customerId
                );

        Event event
                = eventController.findEventById(
                        eventId
                );

        if (customer == null || event == null) {
            return false;
        }

        try {
            Quote quote = new Quote(
                    quoteId,
                    customer,
                    event
            );

            if (!apiClient.addQuote(quote)) {
                return false;
            }

            quotes.add(quote);
            return true;

        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public Quote findQuoteById(int id) {

        for (Quote quote : quotes) {

            if (quote.getId() == id) {
                return quote;
            }
        }

        return null;
    }

    public boolean addAssignment(
            int quoteId,
            int assignmentId,
            int equipmentId,
            int quantity) {

        Quote quote = findQuoteById(quoteId);

        if (quote == null
                || quote.isApproved()
                || assignmentId <= 0
                || quantity <= 0) {

            return false;
        }

        Equipment equipment
                = equipmentController.findEquipmentById(
                        equipmentId
                );

        if (equipment == null
                || !equipment.isAvailable(quantity)) {

            return false;
        }

        try {
            Assignment assignment = new Assignment(
                    assignmentId,
                    equipment,
                    quantity
            );

            if (!quote.addAssignment(assignment)) {
                return false;
            }

            if (!apiClient.updateQuote(quote)) {

                quote.removeAssignment(assignmentId);
                return false;
            }

            return true;

        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean removeAssignment(
            int quoteId,
            int assignmentId) {

        Quote quote = findQuoteById(quoteId);

        if (quote == null || quote.isApproved()) {
            return false;
        }

        Assignment assignment
                = quote.findAssignmentById(
                        assignmentId
                );

        if (assignment == null) {
            return false;
        }

        if (!quote.removeAssignment(assignmentId)) {
            return false;
        }

        if (!apiClient.updateQuote(quote)) {

            quote.addAssignment(assignment);
            return false;
        }

        return true;
    }

    public boolean approveQuote(int quoteId) {

        Quote quote = findQuoteById(quoteId);

        if (quote == null
                || quote.isApproved()
                || quote.getAssignments().isEmpty()
                || quote.calculateTotal() <= 0) {

            return false;
        }

        for (Assignment assignment
                : quote.getAssignments()) {

            Equipment equipment
                    = equipmentController.findEquipmentById(
                            assignment.getEquipment().getId()
                    );

            if (equipment == null
                    || !equipment.isAvailable(
                            assignment.getQuantity())) {

                return false;
            }
        }

        if (!apiClient.approveQuote(quoteId)) {
            return false;
        }

        return quote.approve();
    }

    public boolean removeQuote(int quoteId) {

        Quote quote = findQuoteById(quoteId);

        if (quote == null || quote.isApproved()) {
            return false;
        }

        if (!apiClient.deleteQuote(quoteId)) {
            return false;
        }

        return quotes.remove(quote);
    }

    public ArrayList<Quote> getQuotes() {
        return new ArrayList<>(quotes);
    }

    /*
     * Se conserva para mantener compatibilidad con
     * otras clases. Los cambios ya se guardan mediante
     * la API cuando se realiza cada operación.
     */
    public boolean saveQuotes() {
        return true;
    }

    private boolean isValid(Quote quote) {

        if (quote == null
                || quote.getId() <= 0
                || quote.getCustomer() == null
                || quote.getEvent() == null
                || quote.getAssignments() == null) {

            return false;
        }

        if (quote.isApproved()) {

            return !quote.getAssignments().isEmpty()
                    && quote.calculateTotal() > 0;
        }

        return true;
    }
}