package se.umu.smnrk.schackplaneraren.model;

/**
 * Represents a image_diagram row in a database.
 * @author Simon Eriksson
 * @version 1.0
 */
public class ImageDiagram extends Diagram {
    private String imagePath;

    public ImageDiagram(Integer id, Integer diagramsID,
                        String imagePath){
        super(id, diagramsID);
        this.imagePath = imagePath;
    }

    public ImageDiagram(Integer diagramsID, String imagePath){
        super(diagramsID);
        this.imagePath = imagePath;
    }

    @Override
    public String getDiagram(){
        return imagePath;
    }
}
