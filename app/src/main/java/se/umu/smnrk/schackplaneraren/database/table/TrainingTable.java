package se.umu.smnrk.schackplaneraren.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.umu.smnrk.schackplaneraren.database.Validator;
import se.umu.smnrk.schackplaneraren.helper.StringUtil;
import se.umu.smnrk.schackplaneraren.model.Training;

/**
 * Manages and provides information for CRUD operations on a database table
 * representing a Training object.
 * @author Simon Eriksson
 * @version 1.1
 * @see Training
 */
public class TrainingTable extends Table<Training> {
    private static final String TABLE_NAME = "training";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DIAGRAMS_ID = "diagrams_id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_INSTRUCTIONS = "instructions";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_DIAGRAMS_ID + " INTEGER, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_INSTRUCTIONS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_DIAGRAMS_ID + ") " +
                    "REFERENCES " + DiagramsTable.TABLE_NAME +
                        "(" + DiagramsTable._ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") " +
                    "REFERENCES " + CategoryTable.TABLE_NAME +
                        "(" + CategoryTable._ID + ") ON DELETE SET NULL, " +
                "UNIQUE(" + _ID + ") ON CONFLICT REPLACE " +
            ")";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NAME;

    public TrainingTable(SQLiteDatabase database){
        super(database);
    }

    @Override
    public String getTableName(){
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns(){
        return new String[]{
            _ID,
            COLUMN_NAME,
            COLUMN_DATE,
            COLUMN_DIAGRAMS_ID,
            COLUMN_CATEGORY_ID,
            COLUMN_INSTRUCTIONS
        };
    }

    @Override
    Training getEntry(Cursor cursor){
        return new Training(
            cursor.getInt(cursor.getColumnIndex(_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
            cursor.getInt(cursor.getColumnIndex(COLUMN_DIAGRAMS_ID)),
            cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS))
        );
    }

    @Override
    ContentValues getValues(Training entry){
        ContentValues values = new ContentValues(getColumns().length-1);
        values.put(COLUMN_NAME, entry.getName());
        values.put(COLUMN_DATE, entry.getDate());
        values.put(COLUMN_DIAGRAMS_ID, entry.getDiagramsID());
        values.put(COLUMN_CATEGORY_ID, entry.getCategoryID());
        values.put(COLUMN_INSTRUCTIONS, entry.getInstructions());

        return values;
    }

    @Override
    void validate(Training entry){
        if(Validator.isBlank(entry.getName())){
            addError("Träningen måste ha en titel.");
        }

        if(!Validator.isValidDate(entry.getDate()) &&
                !Validator.isBlank(entry.getDate())){
            addError("Datum måste vara index formatet dd-mm-yyyy.");
        }
    }

    public List<Training> sortByDate(boolean descending){
        List<Training> items = new ArrayList<>();

        Cursor cursor = getDatabase().query(
            getTableName(),
            getColumns(),
            COLUMN_DATE + " != ?",
            new String[]{ "" },
            null,
            null,
            null
        );

        while(cursor.moveToNext()){
            items.add(getEntry(cursor));
        }

        closeOpenCursor(cursor);

        Comparator<Training> dateCompare = (descending)
                ? (o1, o2) -> StringUtil.stringToDate(o2.getDate())
                .compareTo(StringUtil.stringToDate(o1.getDate()))
                : (o1, o2) -> StringUtil.stringToDate(o1.getDate())
                .compareTo(StringUtil.stringToDate(o2.getDate()));

        Collections.sort(items, dateCompare);

        return items;
    }

    public List<Training> filterByCategoryID(int categoryID){
        List<Training> items = new ArrayList<>();

        Cursor cursor = getDatabase().query(
            getTableName(),
            getColumns(),
            COLUMN_CATEGORY_ID + " = ?",
            new String[]{ Integer.toString(categoryID) },
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
}
