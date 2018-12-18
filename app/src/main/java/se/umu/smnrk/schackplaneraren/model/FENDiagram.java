package se.umu.smnrk.schackplaneraren.model;

/**
 * Represents a fen_diagram row in a database.
 * @author Simon Eriksson
 * @version 1.0
 */
public class FENDiagram extends Diagram {
    private String fen;

    public FENDiagram(Integer id, Integer diagramsID, String fen){
        super(id, diagramsID);
        this.fen = fen;
    }

    public FENDiagram(Integer diagramsID, String fen){
        super(diagramsID);
        this.fen = fen;
    }

    @Override
    public String getDiagram(){
        return fen;
    }
}
