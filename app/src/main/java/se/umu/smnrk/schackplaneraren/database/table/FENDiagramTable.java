package se.umu.smnrk.schackplaneraren.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.umu.smnrk.schackplaneraren.model.FENDiagram;

/**
 * Manages and provides information for CRUD operations on a database table
 * representing a FENDiagram object.
 * @author Simon Eriksson
 * @version 1.0
 * @see FENDiagram
 */
public class FENDiagramTable extends DiagramTable<FENDiagram> {
    private static final String TABLE_NAME = "fen_diagram";
    private static final String COLUMN_FEN = "fen";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FEN + " TEXT NOT NULL, " +
                COLUMN_DIAGRAMS_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_DIAGRAMS_ID + ") " +
                        "REFERENCES " + DiagramsTable.TABLE_NAME +
                            "(" + DiagramsTable._ID + "), " +
                "UNIQUE( " + _ID + " ) ON CONFLICT REPLACE " +
            ")";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NAME;

    public FENDiagramTable(SQLiteDatabase database){
        super(database);
    }

    @Override
    public String getDiagramColumnName(){
        return COLUMN_FEN;
    }

    @Override
    public String getTableName(){
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns(){
        return new String[]{ _ID, COLUMN_DIAGRAMS_ID, COLUMN_FEN };
    }

    @Override
    FENDiagram getEntry(Cursor cursor){
        return new FENDiagram(
            cursor.getInt(cursor.getColumnIndex(_ID)),
            cursor.getInt(cursor.getColumnIndex(COLUMN_DIAGRAMS_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_FEN))
        );
    }

    @Override
    ContentValues getValues(FENDiagram entry){
        ContentValues values = new ContentValues(getColumns().length-1);
        values.put(COLUMN_DIAGRAMS_ID, entry.getDiagramsID());
        values.put(COLUMN_FEN, entry.getDiagram());

        return values;
    }
}
