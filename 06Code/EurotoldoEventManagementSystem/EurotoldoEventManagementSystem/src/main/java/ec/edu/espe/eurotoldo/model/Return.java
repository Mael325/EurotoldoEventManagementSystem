package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class Return {
    
    private int id;
    private Dispatch dispatch;
    private int returnedQuantity;
    private int damagedQuantity;
    private LocalDate returnDate;
    private User responsibleUser;

    public Return(int id, Dispatch dispatch, int returnedQuantity, int damagedQuantity, LocalDate returnDate, User responsibleUser) {
        this.id = id;
        this.dispatch = dispatch;
        this.returnedQuantity = returnedQuantity;
        this.damagedQuantity = damagedQuantity;
        this.returnDate = returnDate;
        this.responsibleUser = responsibleUser;
    }

    @Override
    public String toString() {
        return "Return{" + "id=" + getId() + ", dispatch=" + getDispatch() + ", returnedQuantity=" + getReturnedQuantity() + ", damagedQuantity=" + getDamagedQuantity() + ", returnDate=" + getReturnDate() + ", responsibleUser=" + getResponsibleUser() + '}';
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the dispatch
     */
    public Dispatch getDispatch() {
        return dispatch;
    }

    /**
     * @param dispatch the dispatch to set
     */
    public void setDispatch(Dispatch dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * @return the returnedQuantity
     */
    public int getReturnedQuantity() {
        return returnedQuantity;
    }

    /**
     * @param returnedQuantity the returnedQuantity to set
     */
    public void setReturnedQuantity(int returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    /**
     * @return the damagedQuantity
     */
    public int getDamagedQuantity() {
        return damagedQuantity;
    }

    /**
     * @param damagedQuantity the damagedQuantity to set
     */
    public void setDamagedQuantity(int damagedQuantity) {
        this.damagedQuantity = damagedQuantity;
    }

    /**
     * @return the returnDate
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the responsibleUser
     */
    public User getResponsibleUser() {
        return responsibleUser;
    }

    /**
     * @param responsibleUser the responsibleUser to set
     */
    public void setResponsibleUser(User responsibleUser) {
        this.responsibleUser = responsibleUser;
    }
    
    
    
    
    
}
