package se.umu.smnrk.schackplaneraren.controller.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.activity.MainActivity;
import se.umu.smnrk.schackplaneraren.controller.activity.TrainingDetailActivity;
import se.umu.smnrk.schackplaneraren.helper.HiddenMenu;
import se.umu.smnrk.schackplaneraren.helper.HiddenMenu.State;
import se.umu.smnrk.schackplaneraren.database.table.CategoryTable;
import se.umu.smnrk.schackplaneraren.database.table.TrainingTable;
import se.umu.smnrk.schackplaneraren.helper.Constants;
import se.umu.smnrk.schackplaneraren.model.Training;

/**
 * Describes how to display a list of Training objects.
 * @author Simon Eriksson
 * @version 1.4
 */
public class TrainingAdapter extends
        RecyclerView.Adapter<TrainingAdapter.ViewHolder>
        implements HiddenMenu.StateManipulator {
    public static final String LIST_KEY = "training_adapter_list";
    private Activity activity;
    private List<Training> trainingList;
    private SparseArray<String> idToCategory;
    private ArrayList<Integer> checkedTrainingIDs;
    private State menuState;

    public TrainingAdapter(Activity activity){
        this.activity = activity;
        idToCategory = new SparseArray<>();
        checkedTrainingIDs = new ArrayList<>();
        trainingList = new ArrayList<>();
        menuState = State.DEFAULT;
    }

    /**
     * Assigns a new data set for this adapter and then rebuilds the notifies
     * of the change.
     * @param trainingList to set.
     * @param categoryTable to create a training id to category map with.
     */
    public void applyData(List<Training> trainingList,
                          CategoryTable categoryTable){
        this.trainingList = trainingList;
        createCategoryMap(categoryTable);
        notifyDataSetChanged();
    }

    private void createCategoryMap(CategoryTable categoryTable){
        for(Training training : trainingList){
            int id = training.getID();
            int categoryID = training.getCategoryID();
            idToCategory.put(id, categoryTable.get(categoryID).getName());
        }
    }

    /**
     * Removes all the selected Training items from the database and then
     * starts the MainActivity after clearing the back stack.
     * @param trainingTable to remove selected Training items from.
     */
    public void removeCheckedItems(TrainingTable trainingTable){
        for(Integer id : checkedTrainingIDs){
            trainingTable.delete(id);
        }

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType){
        int layout = R.layout.recyclerview_training;
        LayoutInflater inflater = LayoutInflater.from(activity);
        return new ViewHolder(inflater.inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Training training = trainingList.get(position);

        holder.trainingId = training.getID();
        holder.title.setText(training.getName());
        holder.category.setText(idToCategory.get(training.getID()));
        holder.date.setText(training.getDate());
        holder.checkBox.setChecked(checkedTrainingIDs.contains(training.getID()));
    }

    @Override
    public int getItemCount(){
        return trainingList.size();
    }

    public void restoreState(ArrayList<Integer> checkedItems){
        this.checkedTrainingIDs = checkedItems;
        manipulateState();
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getCheckedTrainingIDs(){
        return checkedTrainingIDs;
    }

    @Override
    public State getState(){
        return menuState;
    }

    @Override
    public void manipulateState(){
        switch(checkedTrainingIDs.size()){
        case 1:
            menuState = State.EDIT;
            break;
        case 0:
            menuState = State.DEFAULT;
            break;
        default:
            menuState = State.DELETE;
            break;
        }

        activity.invalidateOptionsMenu();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup listItem;
        private CheckBox checkBox;
        private TextView title;
        private TextView category;
        private TextView date;
        private int trainingId;

        ViewHolder(View itemView){
            super(itemView);

            listItem = itemView.findViewById(R.id.training_list_item);
            checkBox = itemView.findViewById(R.id.training_checkbox);
            title = itemView.findViewById(R.id.training_list_title);
            category = itemView.findViewById(R.id.training_list_category);
            date = itemView.findViewById(R.id.training_list_date);

            listItem.setOnClickListener(view -> {
                Intent intent = new Intent(activity, TrainingDetailActivity.class);
                intent.putExtra(Constants.EXTRA_TRAINING_ID, trainingId);
                activity.startActivity(intent);
            });

            checkBox.setOnClickListener(view -> onCheckBoxChanged());

            listItem.setOnLongClickListener(view -> {
                checkBox.setChecked(!checkBox.isChecked());
                onCheckBoxChanged();
                return true;
            });
        }

        private void onCheckBoxChanged(){
            if(checkBox.isChecked()){
                checkedTrainingIDs.add(trainingId);
            } else {
                checkedTrainingIDs.remove(checkedTrainingIDs.indexOf(trainingId));
            }

            manipulateState();
        }
    }
}
