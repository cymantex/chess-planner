package se.umu.smnrk.schackplaneraren.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import se.umu.smnrk.fen.controller.BoardController;
import se.umu.smnrk.fen.controller.BoardSetupController;
import se.umu.smnrk.fen.model.ChessPosition;
import se.umu.smnrk.fen.model.FENBuilder;
import se.umu.smnrk.schackplaneraren.R;

import static se.umu.smnrk.schackplaneraren.helper.Constants.EXTRA_FEN_STRING;

/**
 * Let's the user setup a chess position which is then passed as a FEN
 * in an Intent extra.
 * @author Simon Eriksson
 * @version 1.0
 */
public class ChessBoardActivity extends AppCompatActivity {
    private BoardController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);

        controller = new BoardSetupController(this, FENBuilder.START_POSITION);

        TextView submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_FEN_STRING, new FENBuilder()
                    .create(controller.getCurrentPosition().getPosition()));
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            controller.setPosition(savedInstanceState.getParcelable(
                    ChessPosition.KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putParcelable(
            ChessPosition.KEY,
            controller.getCurrentPosition()
        );
        super.onSaveInstanceState(outState);
    }
}
