package com.photo.user.userphoto.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.flask.colorpicker.ColorPickerView;
import com.photo.user.userphoto.R;
import com.photo.user.userphoto.settings.Settings;

import static com.photo.user.userphoto.settings.Settings.PREFS_NAME;

public class ChooseColorActivity extends AppCompatActivity {
    private Button saveColorButton;
    private Button clearColorButton;
    private static final int DEFAULT_COLOR = Color.parseColor("#55ffffff");
    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);
        saveColorButton = (Button) findViewById(R.id.save_color_btn);
        clearColorButton = (Button) findViewById(R.id.clear_color_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        colorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
        final SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final Integer color = Settings.getColor(sharedPreferences);
        if(color!=null) {
            colorPickerView.setInitialColor(color,false);
        }
        else{
            colorPickerView.setInitialColor(DEFAULT_COLOR,false);
        }
        saveColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.setColor(sharedPreferences,colorPickerView.getSelectedColor());
                finish();
            }
        });
        clearColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.clearColor(sharedPreferences);
                finish();
            }
        });

    }

}
