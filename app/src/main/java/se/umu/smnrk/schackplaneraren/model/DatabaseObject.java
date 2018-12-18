package se.umu.smnrk.schackplaneraren.model;

/**
 * A DatabaseObject is a POJO which corresponds to some row in a database.
 * @author Simon Eriksson
 * @version 1.0
 */
public class DatabaseObject {
    private Integer id;

    public DatabaseObject(){
        id = null;
    }

    public DatabaseObject(Integer id){
        this.id = id;
    }

    public int getID(){
        return id;
    }
}
