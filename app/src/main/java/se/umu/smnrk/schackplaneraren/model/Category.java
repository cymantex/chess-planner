package se.umu.smnrk.schackplaneraren.model;

/**
 * Represents a category row in a database.
 * @author Simon Eriksson
 * @version 1.0
 */
public class Category extends DatabaseObject {
    private String name;

    public Category(Integer id, String name){
        super(id);

        this.name = name;
    }

    public Category(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
