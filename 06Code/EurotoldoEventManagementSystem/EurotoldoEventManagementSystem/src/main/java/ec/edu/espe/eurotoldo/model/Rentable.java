package ec.edu.espe.eurotoldo.model;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public interface Rentable {

    double calculateRentalCost(int quantity, int days);

    boolean isAvailable(int quantity);
}