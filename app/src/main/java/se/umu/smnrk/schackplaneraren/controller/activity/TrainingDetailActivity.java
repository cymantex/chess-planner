package se.umu.smnrk.schackplaneraren.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.fragment.TrainingDetailFragment;
import se.umu.smnrk.schackplaneraren.helper.Constants;

/**
 * Shows all information of a database training row.
 * @author Simon Eriksson
 * @version 1.0
 */
public class TrainingDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        setSupportActionBar(findViewById(R.id.toolbar));

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null && savedInstanceState == null){
            int trainingID = extras.getInt(Constants.EXTRA_TRAINING_ID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.training_detail_container,
                         TrainingDetailFragment.newInstance(trainingID))
                    .commit();
        }
    }
}
