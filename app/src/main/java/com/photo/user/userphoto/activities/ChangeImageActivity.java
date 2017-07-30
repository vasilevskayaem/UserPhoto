package com.photo.user.userphoto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.photo.user.userphoto.R;
import com.photo.user.userphoto.Services.LoadImageIntentService;
import com.photo.user.userphoto.settings.Settings;

import static com.photo.user.userphoto.settings.Settings.PREFS_NAME;


public class ChangeImageActivity extends AppCompatActivity {
    public static final String DATA = "data";
    private TextView imageUrlTextView;
    private Button loadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageUrlTextView = (TextView) findViewById(R.id.image_url);
        loadImageButton = (Button) findViewById(R.id.load_image_btn);
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = imageUrlTextView.getText().toString();
                if (urlString.length() == 0) {
                    imageUrlTextView.setError("Please enter image Url befare loading");
                } else {
                    Settings.setLoadingInProcess(getSharedPreferences(PREFS_NAME,MODE_PRIVATE),true);
                    Intent intent = new Intent(ChangeImageActivity.this, LoadImageIntentService.class);
                    intent.putExtra(LoadImageIntentService.IMAGE_URL,urlString);
                    startService(intent);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
