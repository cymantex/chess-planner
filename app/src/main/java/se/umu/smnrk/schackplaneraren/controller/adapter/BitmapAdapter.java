package se.umu.smnrk.schackplaneraren.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import se.umu.smnrk.schackplaneraren.R;
import se.umu.smnrk.schackplaneraren.helper.DataSet;
import se.umu.smnrk.schackplaneraren.helper.DataSetObserver;

/**
 * Describes how to display a list of bitmaps.
 * @author Simon Eriksson
 * @version 1.2
 */
public class BitmapAdapter extends
        RecyclerView.Adapter<BitmapAdapter.ViewHolder> implements
        DataSet<List<Bitmap>> {
    private List<DataSetObserver<List<Bitmap>>> observers;
    private ArrayList<Bitmap> diagramImages;
    private LayoutInflater inflater;
    private int bitmapLayout;

    public BitmapAdapter(Context context, @LayoutRes int bitmapLayout){
        inflater = LayoutInflater.from(context);
        observers = new ArrayList<>();
        diagramImages = new ArrayList<>();
        this.bitmapLayout = bitmapLayout;
    }

    /**
     * Adds the given bitmap to the data set and then notifies of it's change.
     * @param bitmap to add.
     */
    public void addBitmap(Bitmap bitmap){
        diagramImages.add(bitmap);
        notifyDataSetChanged();
    }

    /**
     * Replaces the current data set with the given one.
     * @param bitMaps to set.
     */
    public void setBitMaps(ArrayList<Bitmap> bitMaps){
        diagramImages = new ArrayList<>(bitMaps);
    }

    /**
     * Removes the bitmap in the given position if it's inside the data sets
     * size.
     * @param position to remove a bitmap from.
     */
    public void removeBitmap(int position){
        if(position < diagramImages.size()){
            diagramImages.remove(position);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType){
        return new ViewHolder(inflater.inflate(bitmapLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        holder.imageView.setImageBitmap(diagramImages.get(position));
        holder.imageView.setOnClickListener(view -> notifyObservers(position));
    }

    @Override
    public int getItemCount(){
        return diagramImages.size();
    }

    @Override
    public void addObserver(DataSetObserver<List<Bitmap>> observer){
        observers.add(observer);
    }

    @Override
    public void notifyObservers(int position){
        for(DataSetObserver<List<Bitmap>> observer : observers){
            observer.onDataSetEvent(position, diagramImages);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.diagram_image);
        }
    }
}
