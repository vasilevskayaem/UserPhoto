package com.photo.user.userphoto.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import activities.PhotoActivity;
import com.photo.user.userphoto.settings.Settings;
import com.photo.user.userphoto.utils.FileUtils;

import static com.photo.user.userphoto.settings.Settings.PREFS_NAME;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LoadImageIntentService extends IntentService {
    public static String IMAGE_URL = "image_url";
    public LoadImageIntentService() {
        super(LoadImageIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getStringExtra(IMAGE_URL)!=null) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            String imageUrl = intent.getStringExtra(IMAGE_URL);
            Settings.setImageUrl(prefs, imageUrl);
            Intent intentLoadingFinished = new Intent();
            intentLoadingFinished.setAction(PhotoActivity.BROADCAST_IMAGE_DOWNLOADED_ACTION);
            try {
                Bitmap drawable = Glide.
                        with(getApplicationContext()).
                        load(imageUrl)
                        .asBitmap()
                        .into(500,500).
                        get();
                FileUtils.saveProfileImage(drawable,getApplicationContext());
                intentLoadingFinished.putExtra(PhotoActivity.BROADCAST_RECIEVER_LOADING_STATUS,PhotoActivity.LOADING_STATUS_OK);
            } catch (final Exception e) {
                e.printStackTrace();
                intentLoadingFinished.putExtra(PhotoActivity.BROADCAST_RECIEVER_LOADING_STATUS,PhotoActivity.LOADING_STATUS_ERROR);
            }
            Settings.setLoadingInProcess(getSharedPreferences(PREFS_NAME,MODE_PRIVATE),false);
            sendBroadcast(intentLoadingFinished);
        }
    }

}
