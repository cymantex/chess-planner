package se.umu.smnrk.schackplaneraren.database.table;

/**
 * Thrown to indicate an error with a database entry.
 * @author Simon Eriksson
 * @version 1.0
 */
public class IllegalEntryException extends Exception {
    public IllegalEntryException(String message){
        super(message);
    }
}
