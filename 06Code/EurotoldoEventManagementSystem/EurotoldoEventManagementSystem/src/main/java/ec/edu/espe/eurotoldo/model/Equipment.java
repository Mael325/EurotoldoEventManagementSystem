
package ec.edu.espe.eurotoldo.model;

/**
 *
 * @author Anthony Arevalo, MosQ ,@ESPE
 */
public class Equipment {
    private int id;
    private String name;
    private Category category;
    private int totalQuantity;

    public Equipment(int id, String name, Category category, int totalQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.totalQuantity = totalQuantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Equipment{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", category=").append(category);
        sb.append(", totalQuantity=").append(totalQuantity);
        sb.append('}');
        return sb.toString();
    }
    
    
}
