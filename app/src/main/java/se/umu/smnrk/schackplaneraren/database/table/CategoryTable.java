package se.umu.smnrk.schackplaneraren.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.umu.smnrk.schackplaneraren.database.Validator;
import se.umu.smnrk.schackplaneraren.model.Category;

/**
 * Manages and provides information for CRUD operations on a database table
 * representing a Category object.
 * @author Simon Eriksson
 * @version 1.1
 * @see Category
 */
public class CategoryTable extends Table<Category> {
    static final String TABLE_NAME = "category";
    private static final String COLUMN_NAME = "name";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                "UNIQUE(" + _ID + ") ON CONFLICT REPLACE " +
            ")";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NAME;

    public CategoryTable(SQLiteDatabase database){
        super(database);
    }

    @Override
    public String getTableName(){
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns(){
        return new String[]{ _ID, COLUMN_NAME };
    }

    @Override
    Category getEntry(Cursor cursor){
        return new Category(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        );
    }

    @Override
    ContentValues getValues(Category entry){
        ContentValues values = new ContentValues(getColumns().length-1);
        values.put(COLUMN_NAME, entry.getName());

        return values;
    }

    @Override
    void validate(Category entry){
        if(Validator.isBlank(entry.getName())){
            addError("Kategorin måste ha ett namn.");
        }
    }

    /**
     * @param categoryName to find the ID for.
     * @return the ID for the category or null if no match was found.
     */
    public Integer getId(String categoryName){
        String[] selection = { categoryName };

        Cursor cursor = getDatabase().query(getTableName(), getColumns(),
                COLUMN_NAME + "= ?", selection, null, null, null);

        Category entry = null;

        if(cursor.moveToNext()){
            entry = getEntry(cursor);
        }

        closeOpenCursor(cursor);

        return (entry != null) ? entry.getID() : null;
    }

    /**
     * Adds all the default categories to the database.
     */
    public void addAll(String[] categoryNames){
        for(String name : categoryNames){
            try {
                add(new Category(name));
            } catch(IllegalEntryException ignored){}
        }
    }

    /**
     * @return a list of all category names.
     */
    public List<String> getAllNames(){
        List<String> names = new ArrayList<>();
        List<Category> categories = getAll();

        for(Category category : categories){
            names.add(category.getName());
        }

        return names;
    }
}
