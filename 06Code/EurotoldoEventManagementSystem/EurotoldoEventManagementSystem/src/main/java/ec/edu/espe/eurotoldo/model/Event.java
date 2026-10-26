package ec.edu.espe.eurotoldo.model;

import java.time.LocalDate;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class Event {
    
    private int id;
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;

    public Event(int id, String name, String location, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Evennt{" + "id=" + getId() + ", name=" + getName() + ", location=" + getLocation() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + '}';
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    
}
