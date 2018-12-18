package se.umu.smnrk.fen.controller;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import se.umu.smnrk.fen.R;
import se.umu.smnrk.fen.model.ChessPosition;
import se.umu.smnrk.fen.model.Coordinate;
import se.umu.smnrk.fen.model.FENBuilder;
import se.umu.smnrk.fen.model.SquareContent;
import se.umu.smnrk.fen.view.ChessBoardView;
import se.umu.smnrk.fen.view.DrawablePieces;


/**
 * Creates a ChessBoardView along with a BoardMenuAdapter to allow the user
 * to edit the position and generate a FEN string.
 * @author Simon Eriksson
 * @version 1.1
 * @see BoardMenuAdapter
 */
public class BoardSetupController extends BoardController {
    private Activity activity;
    private int chessBoardID;
    private BoardMenuAdapter boardMenu;

    /**
     * Sets up a ChessBoardView with the default layout.
     * @param activity to generate layouts with.
     * @param fen representing the position to display.
     */
    public BoardSetupController(Activity activity, @Nullable String fen){
        this(
            activity,
            R.id.chess_board,
            R.id.recyclerview_pieces_container,
            R.layout.recyclerview_pieces,
            R.id.recyclerview_piece,
            fen
        );
    }

    /**
     * Sets up a ChessBoardView with a custom layout.
     * @param activity to generate layouts with.
     * @param chessBoardID the ID to a ChessBoardView layout component.
     * @param recyclerViewID the ID to a RecyclerView layout.
     * @param recyclerViewContent the Layout ID of RecyclerView content.
     * @param imageViewID the image to display the menu items in.
     * @param fen representing the position to display.
     */
    public BoardSetupController(Activity activity, @IdRes int chessBoardID,
                                @IdRes int recyclerViewID,
                                @LayoutRes int recyclerViewContent,
                                @IdRes int imageViewID, @Nullable String fen){
        super(fen);
        this.activity = activity;
        this.chessBoardID = chessBoardID;

        RecyclerView recyclerView = activity.findViewById(recyclerViewID);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,
                DrawablePieces.getLength()/2));
        boardMenu = new BoardMenuAdapter(
            activity,
            recyclerViewContent,
            imageViewID
        );
        recyclerView.setAdapter(boardMenu);

        createView();
    }

    @Override
    ChessPosition createStartingPosition(String fen){
        return new ChessPosition(fen);
    }

    @Override
    ChessBoardView createChessBoardView(){
        ChessBoardView boardView = activity.findViewById(chessBoardID);

        boardView.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                view.performClick();
                Coordinate coord = getCoordinate(event.getX(), event.getY());

                if(getCurrentPosition().hasPiece(coord)){
                    getCurrentPosition().add(coord, SquareContent.EMPTY_SQUARE);
                } else {
                    getCurrentPosition().add(coord, SquareContent.fromID(
                            boardMenu.getSelectedPosition()));
                }

                boardView.drawPosition(getCurrentPosition().getPosition());
            }

            return true;
        });

        return boardView;
    }
}
