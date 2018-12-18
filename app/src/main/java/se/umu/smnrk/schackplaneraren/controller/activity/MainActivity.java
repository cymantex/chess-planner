package se.umu.smnrk.schackplaneraren.controller.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.fragment.TrainingListFragment;
import se.umu.smnrk.schackplaneraren.helper.HiddenMenu;
import se.umu.smnrk.schackplaneraren.helper.Constants;
import se.umu.smnrk.schackplaneraren.helper.StringUtil;

/**
 * Displays a list of all training rows in the database.
 * @author Simon Eriksson
 * @version 1.2
 */
public class MainActivity extends AppCompatActivity {
    private HiddenMenu.Controller menuController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        checkExternalStoragePermissions();

        if(savedInstanceState == null){
            addTrainingListFragment();
        } else {
            menuController = (TrainingListFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.training_list_container);
        }
    }

    private void checkExternalStoragePermissions(){
        String state = Environment.getExternalStorageState();

        if(!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(
                this,
                getString(R.string.error_external_storage),
                Toast.LENGTH_LONG
            ).show();
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                Constants.REQUEST_PERMISSION_WRITE
            );
        }
    }

    private void addTrainingListFragment(){
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        Intent intent = getIntent();
        String action = (intent.getAction() != null) ? intent.getAction() : "";
        TrainingListFragment fragment;

        switch(action){
        case Intent.ACTION_SEARCH:
            String query = intent.getStringExtra(SearchManager.QUERY);

            setToolbarTitle(query);

            fragment = TrainingListFragment.newSearch(query);
            break;
        case Constants.ACTION_FILTER:
            String filterKey = Constants.EXTRA_FILTER;
            String filter = intent.getStringExtra(filterKey);

            if(!filter.equals(getString(R.string.filter_all))){
                setToolbarTitle(filter);
            }

            fragment = TrainingListFragment.newFilter(filter);
            break;
        default:
            fragment = TrainingListFragment.newInstance();
            break;
        }

        menuController = fragment;
        transaction.add(R.id.training_list_container, fragment).commit();
    }

    private void setToolbarTitle(String title){
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(StringUtil.excerpt(title,
                getResources().getInteger(R.integer.toolbar_title_length)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);

        switch(menuController.getStateManipulator().getState()){
        case DEFAULT:
            menu.findItem(R.id.menu_item_delete).setVisible(false);
            menu.findItem(R.id.menu_item_edit).setVisible(false);
            menu.findItem(R.id.menu_item_add).setVisible(true);
            menu.findItem(R.id.menu_item_search).setVisible(true);
            menu.findItem(R.id.menu_item_filter).setVisible(true);
            break;
        case EDIT:
            menu.findItem(R.id.menu_item_edit).setVisible(true);
            menu.findItem(R.id.menu_item_delete).setVisible(true);
            menu.findItem(R.id.menu_item_add).setVisible(false);
            menu.findItem(R.id.menu_item_search).setVisible(false);
            menu.findItem(R.id.menu_item_filter).setVisible(false);
            break;
        case DELETE:
            menu.findItem(R.id.menu_item_edit).setVisible(false);
            menu.findItem(R.id.menu_item_delete).setVisible(true);
            menu.findItem(R.id.menu_item_add).setVisible(false);
            menu.findItem(R.id.menu_item_search).setVisible(false);
            menu.findItem(R.id.menu_item_filter).setVisible(false);
            break;
        default:
            throw new IllegalStateException(String.format(
                getString(R.string.error_unexpected_string),
                menuController.getStateManipulator().getState()
            ));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
        case R.id.menu_item_add:
            startActivity(new Intent(this, TrainingFormActivity.class));
            break;
        case R.id.menu_item_search:
            onSearchRequested();
            break;
        case R.id.menu_item_filter:
            startActivity(new Intent(this, TrainingFilterActivity.class));
            break;
        case R.id.menu_item_edit:
            menuController.onMenuEdit();
            break;
        case R.id.menu_item_delete:
            menuController.onMenuDelete();
            break;
        default:
            throw new IllegalStateException(String.format(
                getString(R.string.error_unexpected_int),
                item.getItemId()
            ));
        }

        return super.onOptionsItemSelected(item);
    }
}
