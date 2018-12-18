package se.umu.smnrk.schackplaneraren.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.umu.smnrk.schackplaneraren.database.table.CategoryTable;
import se.umu.smnrk.schackplaneraren.database.table.DiagramsTable;
import se.umu.smnrk.schackplaneraren.database.table.FENDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.ImageDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.TrainingTable;

/**
 * A helper class to manage database creation and version management.
 * @author Simon Eriksson
 * @version 1.2
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_FILE_NAME = "chess_planner.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    public DatabaseHelper(Context context){
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CategoryTable.SQL_CREATE);
        db.execSQL(DiagramsTable.SQL_CREATE);
        db.execSQL(FENDiagramTable.SQL_CREATE);
        db.execSQL(ImageDiagramTable.SQL_CREATE);
        db.execSQL(TrainingTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(FENDiagramTable.SQL_DELETE);
        db.execSQL(ImageDiagramTable.SQL_DELETE);
        db.execSQL(TrainingTable.SQL_DELETE);
        db.execSQL(DiagramsTable.SQL_DELETE);
        db.execSQL(CategoryTable.SQL_DELETE);

        onCreate(db);
    }
}
