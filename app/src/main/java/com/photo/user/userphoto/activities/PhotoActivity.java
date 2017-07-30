package com.photo.user.userphoto.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.photo.user.userphoto.R;
import com.photo.user.userphoto.Services.LoadImageIntentService;
import com.photo.user.userphoto.settings.Settings;
import com.photo.user.userphoto.utils.FileUtils;

import java.io.File;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;

import static com.photo.user.userphoto.settings.Settings.PREFS_NAME;

public class PhotoActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String BROADCAST_RECIEVER_LOADING_STATUS = "status";
    static final int REQUEST_INTERNET = 456;
    public static final String LOADING_STATUS_OK = "ok";
    public static final String LOADING_STATUS_ERROR = "error";
    private SharedPreferences prefs;

    public static String BROADCAST_IMAGE_DOWNLOADED_ACTION = "com.photo.user.userphoto.IMAGE_DOWNLOADED";
    private ImageView profileImage;
    private Button editPhotoButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        profileImage = (ImageView) findViewById(R.id.user_photo);
        editPhotoButton = (Button) findViewById(R.id.change_user_photo);
        progressBar = (ProgressBar) findViewById(R.id.user_photo_progress);
        editPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PhotoActivity.this,
                        Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED) {
                    //not checked on real device
                    Intent intent = new Intent(PhotoActivity.this, ChangeImageActivity.class);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(PhotoActivity.this,
                            new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
                }

            }
        });
        if (Settings.isLoadingInProcess(prefs)) {
            Intent intent = new Intent(this, LoadImageIntentService.class);
            intent.putExtra(LoadImageIntentService.IMAGE_URL, Settings.getImageUrl(prefs));
            startService(intent);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFileIntoImage();
        registerReceiver(imageDownloadedReceiver, new IntentFilter(BROADCAST_IMAGE_DOWNLOADED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(imageDownloadedReceiver);
    }

    private BroadcastReceiver imageDownloadedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra(BROADCAST_RECIEVER_LOADING_STATUS);
            if (LOADING_STATUS_ERROR.equals(status)) {
                Toast.makeText(getApplicationContext(), R.string.image_loading_error, Toast.LENGTH_LONG).show();
            }
            loadFileIntoImage();
            progressBar.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
        }
    };

    private void loadFileIntoImage() {
        Integer color = Settings.getColor(prefs);
        if (Settings.isLoadingInProcess(prefs)) {
            progressBar.setVisibility(View.VISIBLE);
            if (color != null)
                progressBar.setBackgroundColor(color);
            profileImage.setVisibility(View.GONE);
        } else {
            File file = this.getFileStreamPath("profile.png");
            if (file.exists()) {
                if (color != null) {
                    Glide.with(this).load(file)
                            .bitmapTransform(new ColorFilterTransformation(this, color))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(profileImage);
                } else {
                    Glide.with(this).load(file)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(profileImage);
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ChooseColorActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
            FileUtils.saveProfileImage(imageBitmap, getApplicationContext());
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_INTERNET: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(PhotoActivity.this, ChangeImageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Grant internet permission to load image by url."
                            , Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
