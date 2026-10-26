/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class Dispatch {
    
    private int id;
    private Reservation reservation;
    private Equipment equipment;
    private int quantity;
    private LocalDate dispatchDate;
    private User responsibleUser;

    public Dispatch(int id, Reservation reservation, Equipment equipment, int quantity, LocalDate dispatchDate, User responsibleUser) {
        this.id = id;
        this.reservation = reservation;
        this.equipment = equipment;
        this.quantity = quantity;
        this.dispatchDate = dispatchDate;
        this.responsibleUser = responsibleUser;
    }

    @Override
    public String toString() {
        return "Dispatch{" + "id=" + getId() + ", reservation=" + getReservation() + ", equipment=" + getEquipment() + ", quantity=" + getQuantity() + ", dispatchDate=" + getDispatchDate() + ", responsibleUser=" + getResponsibleUser() + '}';
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
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * @return the equipment
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * @param equipment the equipment to set
     */
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the dispatchDate
     */
    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    /**
     * @param dispatchDate the dispatchDate to set
     */
    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
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
