package se.umu.smnrk.fen.model;

/**
 * Defines the different contents of a chess square.
 * @author Simon Eriksson
 * @version 1.0
 */
public enum SquareContent {
    WHITE_KING(0),
    WHITE_QUEEN(1),
    WHITE_ROOK(2),
    WHITE_BISHOP(3),
    WHITE_KNIGHT(4),
    WHITE_PAWN(5),
    BLACK_KING(6),
    BLACK_QUEEN(7),
    BLACK_ROOK(8),
    BLACK_BISHOP(9),
    BLACK_KNIGHT(10),
    BLACK_PAWN(11),
    EMPTY_SQUARE(12);

    public int id;

    SquareContent(int id){
        this.id = id;
    }

    public static SquareContent fromID(int id){
        if(id >= 0 && id < 12){
            return SquareContent.values()[id];
        }

        return EMPTY_SQUARE;
    }
}
