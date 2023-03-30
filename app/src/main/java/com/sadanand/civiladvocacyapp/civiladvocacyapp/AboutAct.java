package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sadanand.civiladvocacyapp.R;

public class AboutAct extends AppCompatActivity {

    private static final String ABOUT_ACTIVITY_TAG = "AboutActivityTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Log.d(ABOUT_ACTIVITY_TAG, "ABOUT_ACTIVITY_TAG: ABOUT_ACTIVITY_TAG init");
        findViewById(R.id.ApiUrl).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.google.com/civic-information"));
            startActivity(browserIntent);
        });
    }
}