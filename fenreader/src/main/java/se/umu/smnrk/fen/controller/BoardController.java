package se.umu.smnrk.fen.controller;

import android.support.annotation.Nullable;

import se.umu.smnrk.fen.model.ChessPosition;
import se.umu.smnrk.fen.model.Coordinate;
import se.umu.smnrk.fen.model.FENBuilder;
import se.umu.smnrk.fen.view.ChessBoardView;

/**
 * Defines a template method pattern for creating a ChessBoardView with a
 * given ChessPosition.
 * @author Simon Eriksson
 * @version 1.1
 * @see ChessBoardView
 * @see ChessPosition
 */
public abstract class BoardController {
    private String fen;
    private ChessBoardView boardView;
    private ChessPosition chessPosition;

    /**
     * @param fen representing the position to display.
     */
    BoardController(@Nullable String fen){
        this.fen = (fen != null) ? fen : FENBuilder.EMPTY_BOARD;
    }

    /**
     * Template method.
     */
    void createView(){
        chessPosition = createStartingPosition(fen);
        boardView = createChessBoardView();
        boardView.setFocusableInTouchMode(true);
        boardView.setFocusable(true);
        boardView.drawPosition(chessPosition.getPosition());
    }

    /**
     * @param fen to optionally create the ChessPosition with.
     * @return the created ChessPosition.
     */
    abstract ChessPosition createStartingPosition(String fen);

    /**
     * Defines how the the ChessBoardView is created and can be
     * interacted with.
     * @return the created ChessBoardView.
     */
    abstract ChessBoardView createChessBoardView();

    /**
     * Gets a coordinate relative to the ChessBoardView's width and height.
     */
    Coordinate getCoordinate(float x, float y){
        return new Coordinate(
            (int)(x*8)/boardView.getWidth(),
            (int)(y*8)/boardView.getHeight()
        );
    }

    /**
     * Updates the ChessBoardView to the given ChessPosition.
     * @param chessPosition to update the ChessBoardView with.
     */
    public void setPosition(ChessPosition chessPosition){
        this.chessPosition = chessPosition;
        boardView.drawPosition(chessPosition.getPosition());
    }

    /**
     * @return a Parcelable current position.
     */
    public ChessPosition getCurrentPosition(){
        return chessPosition;
    }
}
