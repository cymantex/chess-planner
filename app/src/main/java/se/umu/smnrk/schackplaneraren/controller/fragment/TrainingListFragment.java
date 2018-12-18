package se.umu.smnrk.schackplaneraren.controller.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.activity.TrainingFormActivity;
import se.umu.smnrk.schackplaneraren.controller.adapter.TrainingAdapter;
import se.umu.smnrk.schackplaneraren.helper.HiddenMenu;
import se.umu.smnrk.schackplaneraren.database.DatabaseHelper;
import se.umu.smnrk.schackplaneraren.database.EventBusEvent;
import se.umu.smnrk.schackplaneraren.database.EventBusEventHandler;
import se.umu.smnrk.schackplaneraren.database.table.CategoryTable;
import se.umu.smnrk.schackplaneraren.database.table.TrainingTable;
import se.umu.smnrk.schackplaneraren.helper.Constants;
import se.umu.smnrk.schackplaneraren.model.Training;

/**
 * Shows a list of all Training objects in the database.
 * @author Simon Eriksson
 * @version 1.4
 */
public class TrainingListFragment extends Fragment implements
        HiddenMenu.Controller, EventBusEventHandler<Training> {
    private static final String FILTER_KEY = "filter_key";
    private static final String SEARCH_KEY = "search_key";
    private TrainingTable trainingTable;
    private CategoryTable categoryTable;
    private TrainingAdapter trainingAdapter;
    private Bundle savedInstanceState;
    private ProgressBar progressBar;
    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Creates an instance of this class for showing all training rows without
     * restriction.
     * @return an instance of this.
     */
    public static TrainingListFragment newInstance(){
        TrainingListFragment fragment = new TrainingListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Creates an instance if this class for showing training rows which is
     * accepted by the given filter.
     * @param filter to apply.
     * @return an instance of this.
     */
    public static TrainingListFragment newFilter(String filter){
        TrainingListFragment fragment = new TrainingListFragment();
        Bundle args = new Bundle();

        args.putString(FILTER_KEY, filter);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Creates an instance of this class for showing training rows containing
     * data matching the given search.
     * @param search the search query.
     * @return an instance of this.
     */
    public static TrainingListFragment newSearch(String search){
        TrainingListFragment fragment = new TrainingListFragment();
        Bundle args = new Bundle();

        args.putString(SEARCH_KEY, search);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(
            R.layout.fragment_training_list,
            container,
            false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //Get database and setup tables
        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase database = helper.getReadableDatabase();
        trainingTable = new TrainingTable(database);
        categoryTable = new CategoryTable(database);

        //Setup the list to display information in
        RecyclerView recyclerView = view.findViewById(R.id.training_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(
            getActivity(),
            LinearLayout.VERTICAL,
            false
        ));
        trainingAdapter = new TrainingAdapter(getActivity());
        recyclerView.setAdapter(trainingAdapter);

        this.savedInstanceState = savedInstanceState;

        if(getArguments() == null){ return; }

        String searchQuery = getArguments().getString(SEARCH_KEY);
        String filter = getArguments().getString(FILTER_KEY);

        //Retrieve all database rows in a new thread and show a progress bar.
        progressBar = view.findViewById(R.id.progress_bar);
        EventBus.getDefault().register(this);
        executor.execute(()->{
            progressBar.setVisibility(View.VISIBLE);

            if(searchQuery != null){
                EventBus.getDefault().post(
                    new EventBusEvent<>(trainingTable.search(searchQuery))
                );
            } else if(filter != null){
                EventBus.getDefault().post(
                    new EventBusEvent<>(createFilter(filter))
                );
            } else {
                EventBus.getDefault().post(
                    new EventBusEvent<>(trainingTable.getAll())
                );
            }
        });
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusPost(EventBusEvent<Training> trainingListEvent){
        progressBar.setVisibility(View.GONE);

        trainingAdapter.applyData(trainingListEvent.getList(), categoryTable);

        if(savedInstanceState != null){
            trainingAdapter.restoreState(
                savedInstanceState.getIntegerArrayList(TrainingAdapter.LIST_KEY)
            );
        }

        trainingAdapter.manipulateState();
    }

    private List<Training> createFilter(@NonNull String filter){
        if(filter.equals(getString(R.string.filter_newest))){
            return trainingTable.sortByDate(true);
        } else if(filter.equals(getString(R.string.filter_oldest))){
            return trainingTable.sortByDate(false);
        } else if(filter.equals(getString(R.string.filter_all))){
            return trainingTable.getAll();
        } else {
            return trainingTable.filterByCategoryID(
                    categoryTable.getId(filter));
        }
    }

    @Override
    public HiddenMenu.StateManipulator getStateManipulator(){
        return trainingAdapter;
    }

    @Override
    public void onMenuDelete(){
        if(getActivity() == null){ return; }

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.warning_title))
                .setMessage(
                    String.format(
                        getString(R.string.warning_delete_training),
                        trainingAdapter.getCheckedTrainingIDs().size()
                    )
                ).setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(
                    R.string.yes,
                    (dialog, whichButton) ->
                            trainingAdapter.removeCheckedItems(trainingTable)
                ).show();
    }

    @Override
    public void onMenuEdit(){
        if(getActivity() == null){ return; }

        Intent intent = new Intent(getActivity(), TrainingFormActivity.class);
        Training training = trainingTable.get(
                trainingAdapter.getCheckedTrainingIDs().get(0));
        intent.putExtra(Constants.EXTRA_TRAINING_ID, training.getID());
        intent.setAction(Constants.ACTION_EDIT);

        getActivity().startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        savedInstanceState.putIntegerArrayList(
            TrainingAdapter.LIST_KEY,
            trainingAdapter.getCheckedTrainingIDs()
        );

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
