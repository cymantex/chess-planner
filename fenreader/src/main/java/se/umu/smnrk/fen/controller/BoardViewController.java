package se.umu.smnrk.fen.controller;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import se.umu.smnrk.fen.model.ChessPosition;
import se.umu.smnrk.fen.view.ChessBoardView;

/**
 * Creates a simple static ChessBoardView.
 * @author Simon Eriksson
 * @version 1.0
 */
public class BoardViewController extends BoardController {
    private Activity activity;
    private int chessBoardID;

    /**
     * @param activity to generate layouts with.
     * @param chessBoardID the ID to a ChessBoardView layout component.
     * @param fen representing the position to display.
     */
    public BoardViewController(Activity activity, @IdRes int chessBoardID,
                               @Nullable String fen){
        super(fen);
        this.activity = activity;
        this.chessBoardID = chessBoardID;

        createView();
    }

    @Override
    ChessPosition createStartingPosition(String fen){
        return new ChessPosition(fen);
    }

    @Override
    ChessBoardView createChessBoardView(){
        return activity.findViewById(chessBoardID);
    }
}
