package se.umu.smnrk.schackplaneraren.helper;

/**
 * Lists constants that can be reused.
 * @author Simon Eriksson
 * @version 1.4
 */
public class Constants {
    //region Intent extras
    public static final String EXTRA_FILTER = "extra_training_filter";
    public static final String EXTRA_TRAINING_ID = "extra_training_id";
    public static final String EXTRA_FEN_STRING = "extra_fen_string";
    //endregion

    //region Intent actions
    public static final String ACTION_FILTER = "action_filter";
    public static final String ACTION_EDIT = "action_edit";
    //endregion

    //region Requests
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_FEN_DIAGRAM = 2;
    public static final int REQUEST_PERMISSION_WRITE = 3;
    //endregion

    public static final String DATE_FORMAT = "dd-MM-yyyy";
}
