package se.umu.smnrk.schackplaneraren.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.adapter.FilterAdapter;
import se.umu.smnrk.schackplaneraren.helper.Constants;

/**
 * Shows a list of filters the user may apply to a list of Training objects.
 * @author Simon Eriksson
 * @version 1.0
 */
public class TrainingFilterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setSupportActionBar(findViewById(R.id.toolbar));

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        createFilterList();
    }

    /**
     * Populates an ExpandableListAdapter with different filter options
     * and defines what happens when these options are selected.
     */
    private void createFilterList(){
        ExpandableListView filterList = findViewById(R.id.filter_list);

        List<String> groupData = new ArrayList<>();
        groupData.add(getString(R.string.filter_all));
        groupData.add(getString(R.string.sort_title));
        groupData.add(getString(R.string.filter_by));

        String[] dates = getResources().getStringArray(R.array.filter_dates);
        String[] categories =
                getResources().getStringArray(R.array.filter_categories);

        HashMap<String, List<String>> groupToChildData = new HashMap<>();
        groupToChildData.put(groupData.get(0), new ArrayList<>());
        groupToChildData.put(groupData.get(1), Arrays.asList(dates));
        groupToChildData.put(groupData.get(2), Arrays.asList(categories));

        ExpandableListAdapter filterAdapter =
                new FilterAdapter(this, groupData, groupToChildData);
        filterList.setAdapter(filterAdapter);

        filterList.setOnGroupClickListener((parent, view, groupPosition, id)->{
            if(groupPosition == 0){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.EXTRA_FILTER, groupData.get(0));
                intent.setAction(Constants.ACTION_FILTER);
                startActivity(intent);
                return true;
            }

            return false;
        });

        filterList.setOnChildClickListener((parent, view, groupPosition,
                                            childPosition, id) -> {
            Intent intent = new Intent(this, MainActivity.class);
            String filter = groupToChildData.get(groupData.get
                    (groupPosition)).get(childPosition);
            intent.putExtra(Constants.EXTRA_FILTER, filter);
            intent.setAction(Constants.ACTION_FILTER);
            startActivity(intent);

            return true;
        });
    }
}
