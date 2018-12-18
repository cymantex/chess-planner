package se.umu.smnrk.schackplaneraren.controller.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.activity.MainActivity;
import se.umu.smnrk.schackplaneraren.controller.form.AddTrainingForm;
import se.umu.smnrk.schackplaneraren.controller.form.EditTrainingForm;
import se.umu.smnrk.schackplaneraren.controller.form.TrainingForm;
import se.umu.smnrk.schackplaneraren.database.DatabaseHelper;
import se.umu.smnrk.schackplaneraren.database.table.IllegalEntryException;
import se.umu.smnrk.schackplaneraren.helper.Constants;

import static se.umu.smnrk.schackplaneraren.helper.Constants.EXTRA_FEN_STRING;
import static se.umu.smnrk.schackplaneraren.helper.Constants.REQUEST_CAMERA;
import static se.umu.smnrk.schackplaneraren.helper.Constants.REQUEST_FEN_DIAGRAM;

/**
 * Shows a form for either adding or editing a new chess lecture.
 * @author Simon Eriksson
 * @version 1.4
 */
public class TrainingFormActivity extends AppCompatActivity {
    private TrainingForm trainingForm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setSupportActionBar(findViewById(R.id.toolbar));

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        Intent intent = getIntent();
        String action = intent.getAction() != null ? intent.getAction() : "";

        switch(action){
        case Constants.ACTION_EDIT:
            Bundle extras = intent.getExtras();
            if(extras != null){
                int id = extras.getInt(Constants.EXTRA_TRAINING_ID);
                TextView title = findViewById(R.id.action_bar_title);
                title.setText(getString(R.string.edit_training));
                trainingForm = new EditTrainingForm(this, database, id,
                        savedInstanceState != null);
                setupFormSubmit(trainingForm, R.string.done);
            }
            break;
        default:
            trainingForm = new AddTrainingForm(this, database);
            setupFormSubmit(trainingForm, R.string.done);
            break;
        }
    }

    /**
     * Defines what happens when the form is submitted.
     * @param trainingForm to get information from.
     * @param submitText the text to display on the form submit button.
     */
    private void setupFormSubmit(TrainingForm trainingForm,
                                 @StringRes int submitText){
        TextView submitButton = findViewById(R.id.submit);
        submitButton.setText(submitText);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

        submitButton.setOnClickListener(view -> {
            try {
                trainingForm.saveTraining();
            } catch(IllegalEntryException e){
                Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
                ).show();
                return;
            }

            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
        if(requestCode == REQUEST_CAMERA){
            trainingForm.onCameraImageReceived(resultCode);
        } else if(requestCode == REQUEST_FEN_DIAGRAM && resultCode == RESULT_OK
                && data.getExtras() != null){
            String fen = data.getExtras().getString(EXTRA_FEN_STRING);
            trainingForm.onFenStringReceived(fen);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            trainingForm.openParcel(savedInstanceState.getParcelable(
                    TrainingForm.PARCEL_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(
            TrainingForm.PARCEL_KEY,
            trainingForm.getParcel()
        );

        super.onSaveInstanceState(savedInstanceState);
    }
}
