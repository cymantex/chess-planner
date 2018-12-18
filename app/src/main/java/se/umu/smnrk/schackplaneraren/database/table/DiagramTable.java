package se.umu.smnrk.schackplaneraren.database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.umu.smnrk.schackplaneraren.model.Diagram;

public abstract class DiagramTable<T extends Diagram> extends Table<T> {
    static final String COLUMN_DIAGRAMS_ID = "diagrams_id";

    DiagramTable(SQLiteDatabase database){
        super(database);
    }

    public abstract String getDiagramColumnName();

    public Diagram get(String diagramDesc){
        Diagram diagram = null;

        Cursor cursor = getDatabase().query(
            getTableName(),
            getColumns(),
            getDiagramColumnName() + "= ?",
            new String[]{ diagramDesc },
            null,
            null,
            null
        );

        if(cursor.moveToNext()){
            diagram = getEntry(cursor);
        }

        closeOpenCursor(cursor);

        return diagram;
    }

    public List<Diagram> getAll(int diagramsId){
        List<Diagram> items = new ArrayList<>();

        Cursor cursor = getDatabase().query(
            getTableName(),
            getColumns(),
            COLUMN_DIAGRAMS_ID + "= ?",
            new String[]{ Integer.toString(diagramsId) },
            null,
            null,
            null
        );

        while(cursor.moveToNext()){
            items.add(getEntry(cursor));
        }

        closeOpenCursor(cursor);

        return items;
    }

    public List<String> getAllDiagrams(int diagramsId){
        List<Diagram> diagrams = getAll(diagramsId);
        List<String> imagePaths = new ArrayList<>();

        for(Diagram diagram : diagrams){
            imagePaths.add(diagram.getDiagram());
        }

        return imagePaths;
    }
}
