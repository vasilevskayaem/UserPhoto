package com.photo.user.userphoto.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.photo.user.userphoto.R;

import java.io.FileOutputStream;

/**
 * Created by Kate on 7/29/17.
 */

public class FileUtils {
    public static final int REQUEST_IMAGE_CAPTURE = 1;


    public static void saveProfileImage(Bitmap bitmap, Context context){
        String filename = "profile.png";
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filename, context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.saving_photo_error
                    ,Toast.LENGTH_LONG).show();
        }
    }

}
