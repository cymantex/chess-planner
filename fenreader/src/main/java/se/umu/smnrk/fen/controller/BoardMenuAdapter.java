package se.umu.smnrk.fen.controller;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import se.umu.smnrk.fen.view.DrawablePieces;

/**
 * Describes how to render a menu for editing a ChessPosition.
 * @author Simon Eriksson
 * @version 1.0
 */
public class BoardMenuAdapter extends
        RecyclerView.Adapter<BoardMenuAdapter.ViewHolder> {
    private Context context;
    private int imageViewID;
    private int layoutID;
    private int selectedPosition;

    /**
     * @param context to manage layouts with.
     * @param layoutID to inflate.
     * @param imageViewID to use in the ViewHolder.
     */
    BoardMenuAdapter(Context context, @LayoutRes int layoutID,
                     @IdRes int imageViewID){
        this.context = context;
        this.layoutID = layoutID;
        this.imageViewID = imageViewID;
        selectedPosition = -1;
    }

    /**
     * @return the selected position in this adapter or -1 if none has been
     *         selected yet.
     */
    int getSelectedPosition(){
        return selectedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        return new ViewHolder(view, imageViewID);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        int piece = DrawablePieces.getPiece(position);
        holder.imageView.setImageDrawable(context.getDrawable(piece));
        holder.imageView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
        holder.imageView.setBackgroundColor(
            (selectedPosition == position)
            ? Color.parseColor("#3F51B5")
            : Color.parseColor("#00000000")
        );
    }

    @Override
    public int getItemCount(){
        return DrawablePieces.getLength();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        ViewHolder(View itemView, @IdRes int imageViewID){
            super(itemView);

            imageView = itemView.findViewById(imageViewID);
        }
    }
}
