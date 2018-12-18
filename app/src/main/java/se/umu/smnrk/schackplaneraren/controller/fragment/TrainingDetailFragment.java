package se.umu.smnrk.schackplaneraren.controller.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import se.umu.smnrk.fen.model.ChessPosition;
import se.umu.smnrk.fen.view.ChessBoardView;
import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.controller.adapter.BitmapAdapter;
import se.umu.smnrk.schackplaneraren.helper.CameraHelper;
import se.umu.smnrk.schackplaneraren.helper.ImageUtil;
import se.umu.smnrk.schackplaneraren.database.DatabaseHelper;
import se.umu.smnrk.schackplaneraren.database.table.CategoryTable;
import se.umu.smnrk.schackplaneraren.database.table.FENDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.ImageDiagramTable;
import se.umu.smnrk.schackplaneraren.database.table.TrainingTable;
import se.umu.smnrk.schackplaneraren.model.Diagram;
import se.umu.smnrk.schackplaneraren.model.Training;

/**
 * Shows all information from some Training row in the database.
 * @author Simon Eriksson
 * @version 1.2
 */
public class TrainingDetailFragment extends Fragment {
    private static final String TRAINING_ID_KEY = "training_id_key";

    public static TrainingDetailFragment newInstance(int trainingID){
        TrainingDetailFragment fragment = new TrainingDetailFragment();
        Bundle args = new Bundle();

        args.putInt(TRAINING_ID_KEY, trainingID);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(
            R.layout.fragment_training_detail,
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
        TrainingTable trainingTable = new TrainingTable(database);
        ImageDiagramTable imageDiagramTable = new ImageDiagramTable(database);
        FENDiagramTable fenDiagramTable = new FENDiagramTable(database);
        CategoryTable categoryTable = new CategoryTable(database);
        CameraHelper cameraHelper = new CameraHelper(getActivity());

        //Find views
        TextView titleView = view.findViewById(R.id.training_title);
        TextView dateView = view.findViewById(R.id.training_date);
        TextView categoryView = view.findViewById(R.id.training_category);
        TextView instructionView = view.findViewById(R.id.training_instructions);

        //Do not proceed unless a training id was specified
        if(getArguments() == null){ return; }

        Training training = trainingTable.get(
                getArguments().getInt(TRAINING_ID_KEY));

        if(training == null){ return; }

        //Retrieve all information from the training object.
        titleView.setText(training.getName());
        dateView.setText(training.getDate());
        instructionView.setText(training.getInstructions());
        categoryView.setText(
                categoryTable.get(training.getCategoryID()).getName());
        List<Diagram> imageDiagrams = imageDiagramTable.getAll(
                training.getDiagramsID());
        cameraHelper.setImagePaths(Diagram.toList(imageDiagrams));

        RecyclerView diagramList = view.findViewById(R.id.diagram_list);
        BitmapAdapter bitmapAdapter = new BitmapAdapter(getActivity(),
                R.layout.recyclerview_diagram);
        diagramList.setLayoutManager(new LinearLayoutManager(
            getActivity(),
            LinearLayout.VERTICAL,
            false
        ));
        int size = (int)getResources().getDimension(R.dimen.diagram);
        diagramList.setAdapter(bitmapAdapter);

        //Add photographed images to adapter
        bitmapAdapter.setBitMaps(ImageUtil.getScaledBitmaps(
            cameraHelper.getImagePaths(),
            size,
            size
        ));

        ChessBoardView chessBoardView = view.findViewById(R.id.chess_board);
        List<String> fenStrings = fenDiagramTable.getAllDiagrams(
                training.getDiagramsID());

        //Add bitmaps of FEN string positions to adapter.
        for(String fen : fenStrings){
            chessBoardView.setPosition(new ChessPosition(fen).getPosition());
            bitmapAdapter.addBitmap(ImageUtil.getViewBitmap(chessBoardView));
        }
    }
}
