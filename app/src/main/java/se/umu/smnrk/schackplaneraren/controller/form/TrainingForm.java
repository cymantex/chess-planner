package se.umu.smnrk.schackplaneraren.controller.form;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.umu.smnrk.fen.model.ChessPosition;
import se.umu.smnrk.fen.view.ChessBoardView;
import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.activity.ChessBoardActivity;
import se.umu.smnrk.schackplaneraren.controller.adapter.BitmapAdapter;
import se.umu.smnrk.schackplaneraren.helper.CameraHelper;
import se.umu.smnrk.schackplaneraren.helper.DataSetObserver;
import se.umu.smnrk.schackplaneraren.helper.ImageUtil;
import se.umu.smnrk.schackplaneraren.database.table.CategoryTable;
import se.umu.smnrk.schackplaneraren.database.table.IllegalEntryException;
import se.umu.smnrk.schackplaneraren.helper.StringUtil;
import se.umu.smnrk.schackplaneraren.model.Training;

import static android.app.Activity.RESULT_OK;
import static se.umu.smnrk.schackplaneraren.helper.Constants.REQUEST_FEN_DIAGRAM;

/**
 * Creates the baseline for a form that interacts with a training database
 * table row.
 * @author Simon Eriksson
 * @version 1.6
 */
public abstract class TrainingForm implements DataSetObserver<List<Bitmap>> {
    public static final String PARCEL_KEY = "TrainingFormParcelKEY";
    private CategoryTable categoryTable;
    private EditText titleInput;
    private EditText instructionsInput;
    private Spinner categorySpinner;
    private BitmapAdapter bitmapAdapter;
    private CameraHelper cameraHelper;
    private Activity activity;
    private Button cameraButton;
    private Button fenDiagramButton;
    private ChessBoardView chessBoardView;
    boolean openedParcel;
    TextView dateInput;
    List<String> fenStrings;
    List<Types> types;

    enum Types { IMAGE, FEN }

    /**
     * Creates the entire form which is ready to take input from the user.
     * @param activity this object is accessed from.
     * @param database for this form to interact with.
     */
    TrainingForm(Activity activity, SQLiteDatabase database){
        this.activity = activity;
        titleInput = activity.findViewById(R.id.form_title);
        instructionsInput = activity.findViewById(R.id.form_instructions);
        chessBoardView = activity.findViewById(R.id.chess_board);
        dateInput = activity.findViewById(R.id.form_date);
        dateInput.setOnClickListener(view -> showDatePicker(activity));
        categoryTable = new CategoryTable(database);
        types = new ArrayList<>();
        openedParcel = false;

        setupCategorySpinner();
        setupDiagramGrid();
        setupCamera();
        addFenDiagramSupport();
    }

    //region view setup

    /**
     * Creates a view managing a grid of Bitmaps.
     */
    private void setupDiagramGrid(){
        RecyclerView diagramGrid = activity.findViewById(R.id.form_grid);
        RecyclerView.LayoutManager grid = new GridLayoutManager(activity, 3);
        bitmapAdapter = new BitmapAdapter(activity,
                R.layout.form_removable_diagram);
        diagramGrid.setLayoutManager(grid);
        diagramGrid.setAdapter(bitmapAdapter);
        bitmapAdapter.addObserver(this);
    }

    private void setupCamera(){
        cameraHelper = new CameraHelper(activity);
        cameraButton = activity.findViewById(R.id.camera_button);

        if(cameraHelper.deviceHasCamera()){
            cameraButton.setOnClickListener(view -> cameraHelper.startCamera());
        } else {
            cameraButton.setVisibility(View.GONE);
        }
    }

    /**
     * Defines a way to retrieve a FEN string.
     */
    private void addFenDiagramSupport(){
        fenDiagramButton = activity.findViewById(R.id.fen_button);
        fenStrings = new ArrayList<>();

        fenDiagramButton.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChessBoardActivity.class);
            activity.startActivityForResult(intent, REQUEST_FEN_DIAGRAM);
        });
    }

    /**
     * Creates a Spinner containing all categories in the database.
     */
    private void setupCategorySpinner(){
        List<String> categories = categoryTable.getAllNames();

        if(categories.size() == 0){
            categoryTable.addAll(
                    activity.getResources()
                            .getStringArray(R.array.filter_categories)
            );
            categories = categoryTable.getAllNames();
        }

        categorySpinner = activity.findViewById(R.id.form_category_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
            activity,
            R.layout.spinner_item,
            categories
        );
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(spinnerAdapter);
    }

    //endregion

    private void showDatePicker(Activity activity){
        FragmentManager manager = activity.getFragmentManager();
        DatePicker datePicker = new DatePicker();
        datePicker.show(manager, DatePicker.TAG);
    }

    /**
     * @param resultCode from the activityResult.
     */
    public void onCameraImageReceived(int resultCode){
        if(openedParcel){
            return;
        }

        int position = cameraHelper.getImagePaths().size()-1;

        if(resultCode == RESULT_OK){
            bitmapAdapter.addBitmap(ImageUtil.getScaledBitmap(
                cameraHelper.getImagePaths().get(position),
                getDiagramGridSize(),
                getDiagramGridSize()
            ));
        } else {
            cameraHelper.getImagePaths().remove(position);
        }

        onDiagramCountChanged();
    }

    public void onFenStringReceived(String fen){
        fenStrings.add(fen);
        onDiagramCountChanged();

        chessBoardView.setPosition(new ChessPosition(fen).getPosition());
        bitmapAdapter.addBitmap(ImageUtil.getViewBitmap(chessBoardView));
    }

    @Override
    public void onDataSetEvent(int position, List<Bitmap> dataSet){
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.warning_title))
                .setMessage(activity.getString(R.string.warning_delete_image))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(
                    R.string.yes,
                    (dialog, whichButton) -> {
                        onDiagramRemoved(position);
                        onDiagramCountChanged();
                    }
                ).show();
    }

    void onDiagramRemoved(int position){
        bitmapAdapter.removeBitmap(position);

        switch(types.get(position)){
        case FEN:
            fenStrings.remove(position-cameraHelper.getImagePaths().size());
            break;
        case IMAGE:
            cameraHelper.getImagePaths().remove(position);
            break;
        }

        onDiagramCountChanged();
    }

    /**
     * Tries to input all information in this form into the database.
     * @throws IllegalEntryException if any of the fields contains invalid
     *                               information.
     */
    public abstract void saveTraining() throws IllegalEntryException;

    void updateTypes(){
        types = new ArrayList<>();

        for(String ignored : cameraHelper.getImagePaths()){
            types.add(Types.IMAGE);
        }

        for(String ignored : fenStrings){
            types.add(Types.FEN);
        }
    }

    CameraHelper getCameraHelper(){
        return cameraHelper;
    }

    /**
     * Sets the field in this form equal to the data in the given Training
     * object.
     * @param training to update this forms fields with.
     */
    void updateFields(Training training){
        titleInput.setText(training.getName());
        dateInput.setText(training.getDate());
        categorySpinner.setSelection(training.getCategoryID()-1);
        instructionsInput.setText(training.getInstructions());
    }

    /**
     * @param diagramsId to add to the Training object.
     * @return Training object with all fields in this form.
     */
    Training createTraining(Integer diagramsId){
        return new Training(
            titleInput.getText().toString(),
            dateInput.getText().toString(),
            diagramsId,
            categoryTable.getId((String)categorySpinner.getSelectedItem()),
            instructionsInput.getText().toString()
        );
    }

    private int getDiagramGridSize(){
        return (int)activity.getResources().getDimension(
                R.dimen.diagram_thumbnail);
    }

    /**
     * Reassigns all data in the BitmapAdapter.
     */
    void resetAdapter(){
        bitmapAdapter.setBitMaps(ImageUtil.getScaledBitmaps(
            cameraHelper.getImagePaths(),
            getDiagramGridSize(),
            getDiagramGridSize()
        ));

        for(String fen : fenStrings){
            chessBoardView.setPosition(new ChessPosition(fen).getPosition());
            bitmapAdapter.addBitmap(ImageUtil.getViewBitmap(chessBoardView));
        }
    }

    void onDiagramCountChanged(){
        if(fenStrings.size() + cameraHelper.getImagePaths().size() >=
                activity.getResources().getInteger(R.integer.max_diagrams)){
            cameraButton.setVisibility(View.GONE);
            fenDiagramButton.setVisibility(View.GONE);
        } else {
            cameraButton.setVisibility(View.VISIBLE);
            fenDiagramButton.setVisibility(View.VISIBLE);
        }

        updateTypes();
    }

    public static class DatePicker extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public static final String TAG = "DATE_PICKER_TAG";
        private TextView dateInput;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            dateInput = getActivity().findViewById(R.id.form_date);

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(android.widget.DatePicker view, int year,
                              int month, int day){
            dateInput.setText(StringUtil.toDateString(year, month, day));
        }
    }

    //region parcelable

    public void openParcel(Parcelable parcel){
        openedParcel = true;
        TrainingFormParcel trainingFormParcel = (TrainingFormParcel)parcel;
        this.fenStrings = trainingFormParcel.fenStrings;
        cameraHelper.setImagePaths(trainingFormParcel.imagePaths);
        dateInput.setText(trainingFormParcel.date);
        resetAdapter();
        onDiagramCountChanged();
    }

    public Parcelable getParcel(){
        return new TrainingFormParcel(
            fenStrings,
            new ArrayList<>(cameraHelper.getImagePaths()),
            dateInput.getText().toString()
        );
    }

    public static class TrainingFormParcel implements Parcelable {
        private List<String> fenStrings;
        private List<String> imagePaths;
        private String date;

        TrainingFormParcel(List<String> fenStrings, List<String> imagePaths,
                           String date){
            this.fenStrings = new ArrayList<>(fenStrings);
            this.imagePaths = new ArrayList<>(imagePaths);
            this.date = date;
        }

        TrainingFormParcel(Parcel in){
            this.imagePaths = in.createStringArrayList();
            this.fenStrings = in.createStringArrayList();
            this.date = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){
            dest.writeStringList(this.imagePaths);
            dest.writeStringList(this.fenStrings);
            dest.writeString(this.date);
        }

        @Override
        public int describeContents(){
            return 0;
        }

        public static final Creator<TrainingFormParcel> CREATOR =
                new Creator<TrainingFormParcel>(){
            @Override
            public TrainingFormParcel createFromParcel(Parcel in){
                return new TrainingFormParcel(in);
            }

            @Override
            public TrainingFormParcel[] newArray(int size){
                return new TrainingFormParcel[size];
            }
        };
    }

    //endregion
}
