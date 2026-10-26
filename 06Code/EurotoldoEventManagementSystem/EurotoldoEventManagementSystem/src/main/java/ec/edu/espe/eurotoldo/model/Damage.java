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
public class Damage {
    
    private int id;
    private Equipment equipment;
    private int quantity;
    private String description;
    private LocalDate reportedDate;
    private String status;

    public Damage(int id, Equipment equipment, int quantity, String description, LocalDate reportedDate, String status) {
        this.id = id;
        this.equipment = equipment;
        this.quantity = quantity;
        this.description = description;
        this.reportedDate = reportedDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Damage{" + "id=" + getId() + ", equipment=" + getEquipment() + ", quantity=" + getQuantity() + ", description=" + getDescription() + ", reportedDate=" + getReportedDate() + ", status=" + getStatus() + '}';
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the reportedDate
     */
    public LocalDate getReportedDate() {
        return reportedDate;
    }

    /**
     * @param reportedDate the reportedDate to set
     */
    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
