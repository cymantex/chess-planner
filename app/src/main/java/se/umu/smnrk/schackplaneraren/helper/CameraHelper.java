package se.umu.smnrk.schackplaneraren.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import se.umu.smnrk.schackplaneraren.R;

import static se.umu.smnrk.schackplaneraren.helper.Constants.REQUEST_CAMERA;

/**
 * Provides various methods to help interacting with the camera.
 * @author Simon Eriksson
 * @version 1.3
 */
public class CameraHelper {
    private Activity activity;
    private List<String> imagePaths;

    public CameraHelper(Activity activity){
        this.activity = activity;
        imagePaths = new ArrayList<>();
    }

    public boolean deviceHasCamera(){
        return activity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * Tries to start the device camera in a startActivityForResult call
     * where the output is saved in MediaStore.EXTRA_OUTPUT.
     */
    public void startCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(activity.getPackageManager()) != null){
            try {
                File photoFile = createImageFile();
                Uri photoURI = FileProvider.getUriForFile(
                    activity,
                    activity.getPackageName(),
                    photoFile
                );

                imagePaths.add(photoFile.getAbsolutePath());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(intent, REQUEST_CAMERA);
            } catch(IOException ex){
                Toast.makeText(
                    activity,
                    R.string.error_start_camera,
                    Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    public void setImagePaths(List<String> imagePaths){
        this.imagePaths = new ArrayList<>(imagePaths);
    }

    public List<String> getImagePaths(){
        return imagePaths;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK)
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        return File.createTempFile(
            imageFileName,
            ".jpg",
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
    }
}
