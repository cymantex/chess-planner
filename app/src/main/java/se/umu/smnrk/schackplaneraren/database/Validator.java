package se.umu.smnrk.schackplaneraren.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static se.umu.smnrk.schackplaneraren.helper.Constants.DATE_FORMAT;

/**
 * Defines functions used to validate data in the database.
 * @author Simon Eriksson
 * @version 1.0
 */
public class Validator {
    /**
     * @param entry to validate.
     * @return true if the given string is either null or empty, false
     *         otherwise.
     */
    public static boolean isBlank(String entry){
        return entry == null || entry.equals("");
    }

    /**
     * Checks if the given string follows the format specified at
     * Constants.DATE_FORMAT.
     * @param date to validate.
     * @return if the string follows the format, false otherwise.
     */
    public static boolean isValidDate(String date){
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.UK);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
