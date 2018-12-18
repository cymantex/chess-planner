package se.umu.smnrk.fen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Defines ways of creating and interacting with a position that maps every
 * Coordinate of the chessboard to some SquareContent.
 * @author Simon Eriksson
 * @version 1.0
 * @see Coordinate
 * @see SquareContent
 */
public class ChessPosition implements Parcelable {
    public static final String KEY = "ChessPositionKEY";
    public static final int ROWS = 8;
    public static final int COLUMNS = 8;
    private LinkedHashMap<Coordinate, SquareContent> position;

    /**
     * Creates an empty chess position.
     */
    public ChessPosition(){
        position = new LinkedHashMap<>();

        for(Square square : Square.values()){
            position.put(square.getCoordinate(), SquareContent.EMPTY_SQUARE);
        }
    }

    /**
     * Creates a position from the given fen.
     */
    public ChessPosition(String fen){
        position = new FENBuilder().toPosition(fen);
    }

    /**
     * @param coordinate to add content to.
     * @param content to set at the given Coordinate.
     */
    public void add(Coordinate coordinate, SquareContent content){
        position.put(coordinate, content);
    }

    /**
     * @param coordinate to check if it has a piece.
     * @return true if the Coordinate has a piece, false otherwise.
     */
    public boolean hasPiece(Coordinate coordinate){
        return !position.get(coordinate).equals(SquareContent.EMPTY_SQUARE);
    }

    /**
     * @return position that maps every Coordinate on the chessboard to some
     *         SquareContent.
     */
    public LinkedHashMap<Coordinate, SquareContent> getPosition(){
        return position;
    }

    //region parcelable

    protected ChessPosition(Parcel in) {
        position = new LinkedHashMap<>();
        List<Integer> xCoord = new ArrayList<>();
        List<Integer> yCoord = new ArrayList<>();
        List<String> squares = new ArrayList<>();

        in.readList(xCoord, Integer.class.getClassLoader());
        in.readList(yCoord, Integer.class.getClassLoader());
        in.readList(squares, String.class.getClassLoader());

        for(int i = 0; i < squares.size(); i++){
            Coordinate coord = new Coordinate(xCoord.get(i), yCoord.get(i));
            SquareContent content = SquareContent.valueOf(squares.get(i));

            position.put(coord, content);
        }
    }

    public static final Creator<ChessPosition> CREATOR =
            new Creator<ChessPosition>() {
        @Override
        public ChessPosition createFromParcel(Parcel in) {
            return new ChessPosition(in);
        }

        @Override
        public ChessPosition[] newArray(int size) {
            return new ChessPosition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        List<Integer> xCoord = new ArrayList<>();
        List<Integer> yCoord = new ArrayList<>();
        List<String> squares = new ArrayList<>();

        for(Coordinate coordinate : position.keySet()){
            xCoord.add(coordinate.getX());
            yCoord.add(coordinate.getY());
            squares.add(position.get(coordinate).toString());
        }

        parcel.writeList(xCoord);
        parcel.writeList(yCoord);
        parcel.writeList(squares);
    }

    //endregion
}
