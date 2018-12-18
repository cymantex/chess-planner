package se.umu.smnrk.schackplaneraren.model;

/**
 * Represents a training row in a database.
 * @author Simon Eriksson
 * @version 1.0
 */
public class Training extends DatabaseObject {
    private String name;
    private String date;
    private Integer categoryID;
    private Integer diagramsID;
    private String instructions;

    public Training(Integer id, String name, String date, Integer diagramsID,
                    Integer categoryID, String instructions){
        super(id);
        this.name = name;
        this.date = date;
        this.categoryID = categoryID;
        this.diagramsID = diagramsID;
        this.instructions = instructions;
    }

    public Training(String name, String date, Integer diagramsID,
                    Integer categoryID, String instructions){
        this.name = name;
        this.date = date;
        this.categoryID = categoryID;
        this.diagramsID = diagramsID;
        this.instructions = instructions;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

    public Integer getCategoryID(){
        return categoryID;
    }

    public Integer getDiagramsID(){
        return diagramsID;
    }

    public String getInstructions(){
        return instructions;
    }
}
