package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Customer;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Event;
import ec.edu.espe.eurotoldo.model.Quote;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class QuoteController {

    private final ArrayList<Quote> quotes;
    private final QuoteFileManager fileManager;

    private final CustomerController customerController;
    private final EventController eventController;
    private final EquipmentController equipmentController;

    public QuoteController(
            CustomerController customerController,
            EventController eventController,
            EquipmentController equipmentController) {

        quotes = new ArrayList<>();
        fileManager = new QuoteFileManager();

        this.customerController = customerController;
        this.eventController = eventController;
        this.equipmentController = equipmentController;
    }

    public boolean loadQuotes() {

        ArrayList<Quote> loadedQuotes
                = fileManager.loadQuotes();

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

        if (quoteId < 0
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

        Quote quote = new Quote(
                quoteId,
                customer,
                event
        );

        quotes.add(quote);
        return true;
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

        if (quote == null || quote.isApproved()) {
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

            return quote.addAssignment(assignment);

        } catch (IllegalArgumentException e) {
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

        return quote.removeAssignment(assignmentId);
    }

    public boolean approveQuote(int quoteId) {

        Quote quote = findQuoteById(quoteId);

        if (quote == null || quote.isApproved()) {
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

        return quote.approve();
    }

    public boolean removeQuote(int quoteId) {

        Quote quote = findQuoteById(quoteId);

        if (quote == null || quote.isApproved()) {
            return false;
        }

        return quotes.remove(quote);
    }

    public ArrayList<Quote> getQuotes() {
        return new ArrayList<>(quotes);
    }

    public boolean saveQuotes() {
        return fileManager.saveQuotes(quotes);
    }

    private boolean isValid(Quote quote) {

        if (quote == null
                || quote.getId() < 0
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