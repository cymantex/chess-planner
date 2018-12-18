package se.umu.smnrk.fen.view;

import se.umu.smnrk.fen.R;

/**
 * Defines which pieces to draw.
 * @author Simon Eriksson
 * @version 1.0
 */
public class DrawablePieces {
    private static int[] pieces = {
        R.drawable.king_white,
        R.drawable.queen_white,
        R.drawable.rook_white,
        R.drawable.bishop_white,
        R.drawable.knight_white,
        R.drawable.pawn_white,
        R.drawable.king_black,
        R.drawable.queen_black,
        R.drawable.rook_black,
        R.drawable.bishop_black,
        R.drawable.knight_black,
        R.drawable.pawn_black
    };

    public static int getPiece(int index){
        if(index < 0 || index >= pieces.length){
            return -1;
        }

        return pieces[index];
    }

    public static int getLength(){
        return pieces.length;
    }
}
