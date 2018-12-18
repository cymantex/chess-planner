package se.umu.smnrk.schackplaneraren.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import se.umu.smnrk.schackplaneraren.model.DatabaseObject;

/**
 * Manages basic CRUD operations on a SQLiteDatabase table with the same name.
 * @param <T> a DatabaseObject corresponding to this Table.
 * @author Simon Eriksson
 * @version 1.2
 */
public abstract class Table<T extends DatabaseObject> implements BaseColumns {
    private SQLiteDatabase database;
    private StringBuilder errors;

    Table(SQLiteDatabase database){
        this.database = database;
        errors = new StringBuilder();
    }

    /**
     * @return the name of this Table.
     */
    public abstract String getTableName();

    /**
     * @return the name of all columns in this Table.
     */
    public abstract String[] getColumns();

    /**
     * Translates a cursor pointing to a row in this Table's database
     * representation to an object corresponding to this Table's data.
     * @param cursor pointing to a row in this Table.
     * @return DatabaseObject corresponding to this Table.
     */
    abstract T getEntry(Cursor cursor);

    /**
     * @param entry DatabaseObject corresponding to this Table.
     * @return the values to save into the database table corresponding to
     *         this Table.
     */
    abstract ContentValues getValues(T entry);

    /**
     * Validates the given entry by adding descriptions of the potential
     * problems through the addError method.
     * @param entry DatabaseObject corresponding to this Table.
     */
    void validate(T entry){}

    /**
     *
     * @param entry to validate and find out potential errors with.
     * @return a string explaining all the errors or an empty string if none
     *         was found.
     */
    public String getErrors(T entry){
        errors = new StringBuilder();
        validate(entry);

        return errors.toString();
    }

    /**
     * Adds a single entry to this Table's database representation.
     * @param entry an object representing a row in a database table
     *              corresponding to this Table.
     * @return this to allow chaining calls.
     * @throws IllegalEntryException if something is wrong with the entry.
     */
    public Table<T> add(T entry) throws IllegalEntryException {
        errors = new StringBuilder();
        validate(entry);

        if(!errors.toString().equals("")){
            throw new IllegalEntryException(errors.toString());
        }

        database.insert(getTableName(), null, getValues(entry));

        return this;
    }

    /**
     * Adds all entries to this Table's database representation.
     * @param entries a list of DatabaseObjects corresponding to this Table.
     * @return this to allow chaining calls.
     * @throws IllegalEntryException if something is wrong with any entry.
     */
    public Table<T> addAll(List<T> entries) throws IllegalEntryException {
        for(T entry : entries){
            errors = new StringBuilder();
            validate(entry);

            if(!errors.toString().equals("")){
                throw new IllegalEntryException(errors.toString());
            }

            database.insert(getTableName(), null, getValues(entry));
        }

        return this;
    }

    /**
     * Updates a row with the given id using the given entry.
     * @param entry an object representing a row in a database table
     *              corresponding to this Table.
     * @param id which row to update.
     * @return this to allow chaining calls.
     * @throws IllegalEntryException if something is wrong with the entry.
     */
    public Table<T> update(T entry, int id) throws IllegalEntryException {
        errors = new StringBuilder();
        validate(entry);

        if(!errors.toString().equals("")){
            throw new IllegalEntryException(errors.toString());
        }

        String[] selection = { Integer.toString(id) };
        database.update(
            getTableName(),
            getValues(entry), _ID + "= ?",
            selection
        );

        return this;
    }

    /**
     * Deletes a row from the database with the matching id.
     * @param id of the row to delete in this Table.
     * @return this to allow chaining calls.
     */
    public Table<T> delete(int id){
        String[] selection = { Integer.toString(id) };
        database.delete(getTableName(), _ID + "= ?", selection);

        return this;
    }

    /**
     * Deletes every row in the database table corresponding to this class.
     * @return this to allow chaining calls.
     */
    public Table<T> deleteAll(){
        database.delete(
            getTableName(),
            null,
            null
        );

        return this;
    }

    /**
     * Converts a row this Table's database representation matching the given
     * id to an object.
     * @param id of the row to get data from.
     * @return DatabaseObject corresponding to this Table.
     */
    public T get(int id){
        String[] selection = { Integer.toString(id) };

        Cursor cursor = database.query(
            getTableName(),
            getColumns(),
            _ID + "= ?", selection,
            null,
            null,
            null
        );

        T entry = null;

        if(cursor.moveToNext()){
            entry = getEntry(cursor);
        }

        closeOpenCursor(cursor);

        return entry;
    }

    /**
     * Converts all rows in this Table's database representation to a list of
     * objects.
     * @return a list of DatabaseObjects corresponding to this Table.
     */
    public List<T> getAll(){
        List<T> items = new ArrayList<>();

        Cursor cursor = database.query(
            getTableName(),
            getColumns(),
            null,
            null,
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

    /**
     * Runs a LIKE %searchString% query on every column in this table.
     * @param searchString to find matching rows for.
     * @param excludeColumns columns to exclude from the search.
     * @return a list objects representing the rows which contains some data
     *         matching the searchString.
     */
    public List<T> search(String searchString, String... excludeColumns){
        List<T> items = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        String[] columns = getColumns();
        String[] selectionArgs = new String[columns.length-1];

        for(int i = 1; i < columns.length; i++){
            if(!containsColumn(columns[i], excludeColumns)){
                builder.append(columns[i])
                        .append(" LIKE ?");

                if(i != columns.length-1){
                    builder.append(" OR ");
                }

                selectionArgs[i-1] = "%" + searchString + "%";
            }
        }

        Cursor cursor = database.query(
            getTableName(),
            getColumns(),
            builder.toString(),
            selectionArgs,
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

    /**
     * @return The number of entries in this Table's database representation.
     */
    public long getItemCount(){
        return DatabaseUtils.queryNumEntries(database, getTableName());
    }

    /**
     * Meant to be used in validate(T entry) to register eventual errors to
     * be thrown in an IllegalEntryException.
     * @param error to register.
     */
    void addError(String error){
        if(errors.length() > 1){
            errors.append("\n");
        }

        errors.append(error);
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    void closeOpenCursor(Cursor cursor){
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
    }

    private boolean containsColumn(String columnToFind, String[] columns){
        for(String column : columns){
            if(columnToFind.equals(column)){
                return true;
            }
        }

        return false;
    }
}
