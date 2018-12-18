package se.umu.smnrk.schackplaneraren.controller.form;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import se.umu.smnrk.schackplaneraren.database.table.DiagramsTable;
import se.umu.smnrk.schackplaneraren.database.table.FENDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.IllegalEntryException;
import se.umu.smnrk.schackplaneraren.database.table.ImageDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.TrainingTable;
import se.umu.smnrk.schackplaneraren.model.DatabaseObject;
import se.umu.smnrk.schackplaneraren.model.Diagram;
import se.umu.smnrk.schackplaneraren.model.FENDiagram;
import se.umu.smnrk.schackplaneraren.model.ImageDiagram;
import se.umu.smnrk.schackplaneraren.model.Training;

/**
 * A form for editing an existing training row in the database.
 * @author Simon Eriksson
 * @version 1.3
 */
public class EditTrainingForm extends TrainingForm {
    private TrainingTable trainingTable;
    private ImageDiagramTable imageDiagramTable;
    private DiagramsTable diagramsTable;
    private FENDiagramTable fenDiagramTable;
    private Training training;
    private List<String> initialImagePaths;
    private List<String> initialFenStrings;
    private List<String> removedPaths;
    private List<String> removedFenStrings;

    public EditTrainingForm(Activity activity, SQLiteDatabase database,
                            int trainingId, boolean restoredInstance){
        super(activity, database);

        removedPaths = new ArrayList<>();
        removedFenStrings = new ArrayList<>();
        initialImagePaths = new ArrayList<>();
        initialFenStrings = new ArrayList<>();
        trainingTable = new TrainingTable(database);
        diagramsTable = new DiagramsTable(database);
        imageDiagramTable = new ImageDiagramTable(database);
        fenDiagramTable = new FENDiagramTable(database);

        training = trainingTable.get(trainingId);
        super.updateFields(training);

        if(!restoredInstance){
            initialFenStrings = Diagram.toList(fenDiagramTable.getAll(
                    training.getDiagramsID()));
            initialImagePaths = Diagram.toList(
                    imageDiagramTable.getAll(training.getDiagramsID()));
            getCameraHelper().getImagePaths().addAll(initialImagePaths);
            fenStrings.addAll(initialFenStrings);

            super.updateTypes();
            super.resetAdapter();
        }
    }

    @Override
    public void saveTraining() throws IllegalEntryException {
        String errors = trainingTable.getErrors(createTraining(0));

        if(!errors.equals("")){
            throw new IllegalEntryException(errors);
        }

        List<DatabaseObject> databaseObjects = diagramsTable.getAll();
        Integer diagramsId = databaseObjects.get(databaseObjects.size()-1).getID();

        for(String imagePath : removedPaths){
            imageDiagramTable.delete(imageDiagramTable.get(imagePath).getID());
        }

        for(String fen : removedFenStrings){
            fenDiagramTable.delete(fenDiagramTable.get(fen).getID());
        }

        for(String imagePath : getCameraHelper().getImagePaths()){
            if(!initialImagePaths.contains(imagePath)){
                imageDiagramTable.add(new ImageDiagram(diagramsId, imagePath));
            }
        }

        for(String fen : fenStrings){
            if(!initialFenStrings.contains(fen)){
                fenDiagramTable.add(new FENDiagram(diagramsId, fen));
            }
        }

        trainingTable.update(createTraining(diagramsId), training.getID());
    }

    @Override
    void onDiagramRemoved(int position){
        switch(types.get(position)){
        case FEN:
            removedFenStrings.add(fenStrings.get(position-
                    getCameraHelper().getImagePaths().size()));
            break;
        case IMAGE:
            removedPaths.add(getCameraHelper().getImagePaths().get(position));
            break;
        }

        super.onDiagramRemoved(position);
    }

    //region parcelable

    @Override
    public void openParcel(Parcelable parcel){
        openedParcel = true;
        EditFormParcel editFormParcel = (EditFormParcel) parcel;
        this.fenStrings = editFormParcel.fenStrings;
        this.initialImagePaths = editFormParcel.initialImagePaths;
        this.initialFenStrings = editFormParcel.initialFenStrings;
        this.removedFenStrings = editFormParcel.removedFenStrings;
        this.removedPaths = editFormParcel.removedImagePaths;
        dateInput.setText(editFormParcel.date);
        getCameraHelper().setImagePaths(editFormParcel.imagePaths);
        resetAdapter();
        onDiagramCountChanged();
    }

    @Override
    public Parcelable getParcel(){
        return new EditFormParcel(
            fenStrings,
            new ArrayList<>(getCameraHelper().getImagePaths()),
            initialImagePaths,
            initialFenStrings,
            removedPaths,
            removedFenStrings,
            dateInput.getText().toString()
        );
    }

    public static class EditFormParcel implements Parcelable {
        private List<String> fenStrings;
        private List<String> imagePaths;
        private List<String> initialImagePaths;
        private List<String> initialFenStrings;
        private List<String> removedImagePaths;
        private List<String> removedFenStrings;
        private String date;

        EditFormParcel(List<String> fenStrings, List<String> imagePaths,
                       List<String> initialImagePaths,
                       List<String> initialFenStrings,
                       List<String> removedImagePaths,
                       List<String> removedFenStrings, String date){
            this.fenStrings = fenStrings;
            this.imagePaths = imagePaths;
            this.initialImagePaths = initialImagePaths;
            this.initialFenStrings = initialFenStrings;
            this.removedImagePaths = removedImagePaths;
            this.removedFenStrings = removedFenStrings;
            this.date = date;
        }

        EditFormParcel(Parcel in){
            this.fenStrings = in.createStringArrayList();
            this.imagePaths = in.createStringArrayList();
            this.initialImagePaths = in.createStringArrayList();
            this.initialFenStrings = in.createStringArrayList();
            this.removedImagePaths = in.createStringArrayList();
            this.removedFenStrings = in.createStringArrayList();
            date = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){
            dest.writeStringList(fenStrings);
            dest.writeStringList(imagePaths);
            dest.writeStringList(initialImagePaths);
            dest.writeStringList(initialFenStrings);
            dest.writeStringList(removedImagePaths);
            dest.writeStringList(removedFenStrings);
            dest.writeString(date);
        }

        @Override
        public int describeContents(){
            return 0;
        }

        public static final Creator<EditFormParcel> CREATOR =
                new Creator<EditFormParcel>(){
            @Override
            public EditFormParcel createFromParcel(Parcel in){
                return new EditFormParcel(in);
            }

            @Override
            public EditFormParcel[] newArray(int size){
                return new EditFormParcel[size];
            }
        };
    }

    //endregion
}
