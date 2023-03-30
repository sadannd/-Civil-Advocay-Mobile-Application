package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import static com.sadanand.civiladvocacyapp.civiladvocacyapp.MainActivity.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.sadanand.civiladvocacyapp.R;
import com.squareup.picasso.Picasso;

public class FrontPageImage extends AppCompatActivity {

    GovMinister Minister;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minister_list);
        Log.d(TAG, "FrontPageImage: Minister is selected");
        Minister = getIntent().getExtras().getParcelable(Utility.Selection);
        handleImageSelection(Minister);
    }

    private void handleImageSelection(GovMinister Minister) {
        ImageView ministerPhoto = findViewById(R.id.imageView2);

        if (Minister.getPhoto() != null) {
            Log.d(TAG, "FrontPageImage: Fetch minister photo");
            Picasso.get().load(Minister.getPhoto()).placeholder(R.drawable.placeholder).into(ministerPhoto);
        } else {
            Log.d(TAG, "FrontPageImage: Fetch missing photo ");
            Picasso.get().load(R.drawable.missing).into(ministerPhoto);
        }
    }
}
