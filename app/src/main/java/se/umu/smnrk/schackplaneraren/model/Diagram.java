package se.umu.smnrk.schackplaneraren.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A DatabaseObject which can be grouped as one of many diagrams.
 * @author Simon Eriksson
 * @version 1.1
 */
public abstract class Diagram extends DatabaseObject {
    private Integer diagramsID;

    Diagram(Integer id, Integer diagramsID){
        super(id);
        this.diagramsID = diagramsID;
    }

    Diagram(Integer diagramsID){
        this.diagramsID = diagramsID;
    }

    public Integer getDiagramsID(){
        return diagramsID;
    }

    public abstract String getDiagram();

    public static List<String> toList(List<? extends Diagram> diagrams){
        List<String> list = new ArrayList<>();

        for(Diagram diagram : diagrams){
            list.add(diagram.getDiagram());
        }

        return list;
    }
}
