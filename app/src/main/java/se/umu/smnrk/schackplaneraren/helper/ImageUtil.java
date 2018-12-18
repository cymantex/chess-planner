package se.umu.smnrk.schackplaneraren.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines various methods to help managing images.
 * @author Simon Eriksson
 * @version 1.1
 */
public class ImageUtil {
    /**
     * @param imagePath to create a Bitmap from.
     * @param width to use for scaling.
     * @param height to use for scaling.
     * @return the scaled Bitmap or null if the process failed.
     */
    public static Bitmap getScaledBitmap(String imagePath, int width,
                                         int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, options);

        options.inSampleSize = Math.min(options.outWidth/width,
                options.outHeight/height);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;

        return BitmapFactory.decodeFile(imagePath);
    }

    /**
     * @param imagePaths to create a Bitmaps from.
     * @param width to use for scaling.
     * @param height to use for scaling.
     * @return a list of all the scaled Bitmaps.
     */
    public static ArrayList<Bitmap> getScaledBitmaps(List<String> imagePaths,
                                                int width, int height){
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        for(String imagePath : imagePaths){
            bitmaps.add(ImageUtil.getScaledBitmap(imagePath, width, height));
        }

        return bitmaps;
    }

    /**
     * Gets the bitmap for the given view without drawing it on the screen.
     * @param view to create a bitmap from.
     * @return the Bitmap of the view.
     */
    public static Bitmap getViewBitmap(View view){
        view.setDrawingCacheEnabled(true);

        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return b;
    }
}
