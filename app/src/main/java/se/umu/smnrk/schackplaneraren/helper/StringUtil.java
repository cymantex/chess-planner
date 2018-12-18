package se.umu.smnrk.schackplaneraren.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static se.umu.smnrk.schackplaneraren.helper.Constants.DATE_FORMAT;

/**
 * Provides various String utility tools.
 * @author Simon Eriksson
 * @version 1.2
 */
public class StringUtil {
    /**
     *
     * @param content to potentially substring to the given length.
     * @param length to limit the content by.
     * @return Substring of the content at the given length or content if the
     *         given length was greater than the size of the content string.
     */
    public static String excerpt(String content, int length){
        if(content.length() < length){
            return content;
        }

        return content.substring(0, length) + "...";
    }

    /**
     * A lazy alternative to traversing the Date API jungle where the
     * given data is parsed into a dd-MM-yyyy string.
     */
    public static String toDateString(int year, int month, int day){
        StringBuilder builder = new StringBuilder();

        if(day < 10){
            builder.append(0);
        }

        builder.append(day).append("-");

        if(month+1 < 10){
            builder.append(0);
        }

        builder.append(month+1).append("-").append(year);

        return builder.toString();
    }

    /**
     * Tries to parse the given date following the Constants.DATE_FORMAT.
     * @param dateString to parse.
     * @return the Date if the parsing succeeded, null otherwise.
     */
    public static Date stringToDate(String dateString){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.UK);

        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException ignored){}

        return date;
    }
}
