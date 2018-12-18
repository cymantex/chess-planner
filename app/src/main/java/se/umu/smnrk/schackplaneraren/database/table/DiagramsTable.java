package se.umu.smnrk.schackplaneraren.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import se.umu.smnrk.schackplaneraren.model.DatabaseObject;

/**
 * Manages and provides information for CRUD operations on a Diagrams Table.
 * @author Simon Eriksson
 * @version 1.0
 */
public class DiagramsTable extends Table<DatabaseObject> {
    static final String TABLE_NAME = "diagrams";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UNIQUE(" + _ID + ") ON CONFLICT REPLACE " +
            ")";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NAME;

    public DiagramsTable(SQLiteDatabase database){
        super(database);
    }

    public DiagramsTable add(){
        getDatabase().execSQL("INSERT INTO " + TABLE_NAME + " DEFAULT VALUES");

        return this;
    }

    @Override
    public String getTableName(){
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns(){
        return new String[]{ _ID };
    }

    @Override
    DatabaseObject getEntry(Cursor cursor){
        return new DatabaseObject(cursor.getInt(cursor.getColumnIndex(_ID)));
    }

    @Override
    ContentValues getValues(DatabaseObject entry){
        return new ContentValues(1);
    }
}
