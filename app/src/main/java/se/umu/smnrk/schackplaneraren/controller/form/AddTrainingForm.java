package se.umu.smnrk.schackplaneraren.controller.form;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import se.umu.smnrk.schackplaneraren.database.table.DiagramsTable;
import se.umu.smnrk.schackplaneraren.database.table.FENDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.IllegalEntryException;
import se.umu.smnrk.schackplaneraren.database.table.ImageDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.TrainingTable;
import se.umu.smnrk.schackplaneraren.model.DatabaseObject;
import se.umu.smnrk.schackplaneraren.model.FENDiagram;
import se.umu.smnrk.schackplaneraren.model.ImageDiagram;

/**
 * A form for adding a new training row into the database.
 * @author Simon Eriksson
 * @version 1.1
 */
public class AddTrainingForm extends TrainingForm {
    private TrainingTable trainingTable;
    private ImageDiagramTable imageDiagramTable;
    private FENDiagramTable fenDiagramTable;
    private DiagramsTable diagramsTable;

    public AddTrainingForm(Activity activity, SQLiteDatabase database){
        super(activity, database);

        trainingTable = new TrainingTable(database);
        diagramsTable = new DiagramsTable(database);
        imageDiagramTable = new ImageDiagramTable(database);
        fenDiagramTable = new FENDiagramTable(database);
    }

    @Override
    public void saveTraining() throws IllegalEntryException {
        String errors = trainingTable.getErrors(super.createTraining(0));

        if(!errors.equals("")){
            throw new IllegalEntryException(errors);
        }

        diagramsTable.add();
        List<DatabaseObject> databaseObjects = diagramsTable.getAll();
        Integer diagramsId = databaseObjects.get(databaseObjects.size()-1)
                .getID();

        for(String imagePath : getCameraHelper().getImagePaths()){
            imageDiagramTable.add(new ImageDiagram(diagramsId, imagePath));
        }

        for(String fenString : fenStrings){
            fenDiagramTable.add(new FENDiagram(diagramsId, fenString));
        }

        trainingTable.add(super.createTraining(diagramsId));
    }
}
