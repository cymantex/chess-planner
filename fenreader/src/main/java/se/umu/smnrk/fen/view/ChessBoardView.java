package se.umu.smnrk.fen.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import se.umu.smnrk.fen.R;
import se.umu.smnrk.fen.model.Coordinate;
import se.umu.smnrk.fen.model.Square;
import se.umu.smnrk.fen.model.SquareContent;

import static se.umu.smnrk.fen.model.ChessPosition.COLUMNS;
import static se.umu.smnrk.fen.model.ChessPosition.ROWS;

/**
 * A View which renders a chessboard along with pieces.
 * @author Simon Eriksson
 * @version 1.2
 */
public class ChessBoardView extends AppCompatImageView {
    private Bitmap chessBoard;
    private List<Bitmap> pieces;
    private LinkedHashMap<Coordinate, SquareContent> position;

    public ChessBoardView(Context context, @Nullable AttributeSet attr){
        super(context, attr);

        pieces = new ArrayList<>();
        position = new LinkedHashMap<>();

        Resources r = getResources();
        chessBoard = BitmapFactory.decodeResource(r, R.drawable.chessboard);

        //Get the drawable for every type of square content.
        for(SquareContent content : SquareContent.values()){
            int piece = DrawablePieces.getPiece(content.id);
            if(piece != -1){
                pieces.add(BitmapFactory.decodeResource(r, piece));
            }
        }
    }

    /**
     * @param position to draw.
     */
    public void drawPosition(LinkedHashMap<Coordinate, SquareContent> position){
        this.position = position;
        invalidate();
    }

    public void setPosition(LinkedHashMap<Coordinate, SquareContent> position){
        this.position = position;
    }

    /**
     * Checks the SquareContent of each Square in the position and draws
     * all pieces on top of the chessboard.
     * @param canvas to draw onto.
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(position.isEmpty()){ return; }

        canvas.drawBitmap(chessBoard, 0, 0, null);

        int relativeWidth = this.getWidth() / COLUMNS;
        int relativeHeight = this.getHeight() / ROWS;

        for(Square square : Square.values()){
            Coordinate coordinate = square.getCoordinate();
            SquareContent content = position.get(square.getCoordinate());
            if(content != null){
                if(!content.equals(SquareContent.EMPTY_SQUARE)){
                    canvas.drawBitmap(
                        pieces.get(content.id),
                        relativeWidth*coordinate.getX(),
                        relativeHeight*coordinate.getY(),
                        null
                    );
                }
            }
        }
    }

    @Override
    protected void onMeasure(int width, int height){
        int measuredWidth = getMeasurement(width, chessBoard.getWidth());
        int measuredHeight = getMeasurement(height, chessBoard.getHeight());

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int i = MeasureSpec.getMode(measureSpec);

        switch(i){
        case MeasureSpec.UNSPECIFIED:
            return preferred;
        case MeasureSpec.AT_MOST:
            return Math.min(preferred, specSize);
        case MeasureSpec.EXACTLY:
            return specSize;
        default:
            return preferred;
        }
    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }
}
