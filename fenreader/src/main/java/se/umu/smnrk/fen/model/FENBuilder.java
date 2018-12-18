package se.umu.smnrk.fen.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Handles parsing and reading FEN strings.
 * @author Simon Eriksson
 * @version 1.1
 */
public class FENBuilder {
    public static final String EMPTY_BOARD = "8/8/8/8/8/8/8/8 w KQkq - 0 1";
    public static final String START_POSITION =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Enumerates all characters in a FEN string defined in an order matching
     * the SquareContent.
     */
    public enum FEN {
        k(0), q(1), r(2), b(3), n(4),  p(5),
        K(6), Q(7), R(8), B(9), N(10), P(11);

        public int id;

        FEN(int id){
            this.id = id;
        }

        public static String get(int id){
            return FEN.values()[id].toString();
        }

        /**
         * @param c to check if it's a FEN character.
         * @return true if the given char is a FEN character, false otherwise.
         */
        public static boolean isMember(char c){
            try {
                valueOf(Character.toString(c));
            } catch (IllegalArgumentException | NullPointerException e){
                return false;
            }

            return true;
        }
    }

    //region createFEN

    private StringBuilder builder;

    /**
     * @param position to generate a FEN string from.
     * @return the FEN string of the given position.
     */
    public String create(HashMap<Coordinate, SquareContent> position){
        builder = new StringBuilder();

        for(Coordinate coord : position.keySet()){
            SquareContent content = position.get(coord);

            if(content.equals(SquareContent.EMPTY_SQUARE)){
                emptySquares++;
            } else {
                checkEmptySquares();
                builder.append(FEN.get(content.id));
            }

            if(coord.getX() == ChessPosition.ROWS-1 && coord.getY() != 0){
                checkEmptySquares();
                builder.append("/");
            }
        }

        builder.append(" w KQkq - 0 1");

        return builder.toString();
    }

    private void checkEmptySquares(){
        if(emptySquares > 0){
            builder.append(emptySquares);
            emptySquares = 0;
        }
    }

    //endregion

    //region toPosition

    private LinkedHashMap<Coordinate, SquareContent> position;
    private int emptySquares;
    private int row;
    private int col;

    /**
     * @param fen to parse a position from.
     * @return a position matching the given fen.
     */
    public LinkedHashMap<Coordinate, SquareContent> toPosition(String fen){
        position = new LinkedHashMap<>();
        emptySquares = 0;
        row = ChessPosition.ROWS-1;
        col = 0;
        char c;

        for(int i = 0; i < fen.length(); i++){
            c = fen.charAt(i);

            if(FEN.isMember(c)){
                int id = FEN.valueOf(Character.toString(c)).id;
                position.put(new Coordinate(col, row), SquareContent.fromID(id));
                increaseCount();
            } else if(c >= '1' && c <= '8'){
                addEmptySquares(c - '0');
            } else if(c == ' '){
                break;
            }
        }

        return position;
    }

    private void addEmptySquares(int emptySquares){
        for(int i = 0; i < emptySquares; i++){
            position.put(new Coordinate(col, row), SquareContent.EMPTY_SQUARE);
            increaseCount();
        }
    }

    private void increaseCount(){
        col++;

        if(col == ChessPosition.COLUMNS){
            col = 0;
            row--;
        }
    }

    //endregion
}
