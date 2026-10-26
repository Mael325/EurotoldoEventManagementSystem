
package ec.edu.espe.eurotoldo.model;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class Category {
    private int id;
    private String name;
    private String description;

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
