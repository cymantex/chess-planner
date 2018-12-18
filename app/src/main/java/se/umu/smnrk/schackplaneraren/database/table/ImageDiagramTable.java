package se.umu.smnrk.schackplaneraren.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.umu.smnrk.schackplaneraren.model.ImageDiagram;

/**
 * Manages and provides information for CRUD operations on a database table
 * representing an ImageDiagram object.
 * @author Simon Eriksson
 * @version 1.1
 * @see ImageDiagram
 */
public class ImageDiagramTable extends DiagramTable<ImageDiagram> {
    private static final String TABLE_NAME = "image_diagram";
    private static final String COLUMN_DIAGRAMS_ID = "diagrams_id";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_PATH + " INTEGER NOT NULL, " +
                COLUMN_DIAGRAMS_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_DIAGRAMS_ID + ") " +
                        "REFERENCES " + DiagramsTable.TABLE_NAME +
                            "(" + DiagramsTable._ID + "), " +
                "UNIQUE(" + _ID + ") ON CONFLICT REPLACE " +
            ")";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NAME;

    public ImageDiagramTable(SQLiteDatabase database){
        super(database);
    }

    @Override
    public String getDiagramColumnName(){
        return COLUMN_IMAGE_PATH;
    }

    @Override
    public String getTableName(){
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns(){
        return new String[]{
            _ID,
            COLUMN_DIAGRAMS_ID,
            COLUMN_IMAGE_PATH
        };
    }

    @Override
    ImageDiagram getEntry(Cursor cursor){
        return new ImageDiagram(
            cursor.getInt(cursor.getColumnIndex(_ID)),
            cursor.getInt(cursor.getColumnIndex(COLUMN_DIAGRAMS_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH))
        );
    }

    @Override
    ContentValues getValues(ImageDiagram entry){
        ContentValues values = new ContentValues(getColumns().length-1);
        values.put(COLUMN_DIAGRAMS_ID, entry.getDiagramsID());
        values.put(COLUMN_IMAGE_PATH, entry.getDiagram());

        return values;
    }
}
