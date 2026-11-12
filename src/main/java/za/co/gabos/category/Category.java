package za.co.gabos.category;

/**
 * This represents a entry in the category database e.g groceries
 */
public class Category {
    private int id;
    private final String name;

    /**
     * The reason we use this is in our constructor that handles all attributes contains validation using this and giving the id a deault value calls that constructor its similar to using super in a abstract class
     * @param name
     */
    public Category(String name){
        this(0, name);
    }

    public Category(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or blank");
        }
        this.id = id;
        this.name = name.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
