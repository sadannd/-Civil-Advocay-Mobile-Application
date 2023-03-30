package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sadanand.civiladvocacyapp.R;
import com.squareup.picasso.Picasso;

public class FeaturesActivity extends AppCompatActivity {
    private TextView AddressLine_minister,organization_Post;
    private TextView minister_AddressDetails, minister_Phoneno;
    private TextView minister_Website,minister_Email;
    ImageView minister_PartySymbol;
    ConstraintLayout constraintLayout;

    private ImageView minister_Facebook,minister_Youtube;

    private TextView minister_Party, minister_Name;
    GovMinister minister;
    private ImageView minister_Twitter, Minister_Photo;
    private static final String TAG = "FeaturesActivity";
    

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minister_details);
        minister = getIntent().getExtras().getParcelable(Utility.Selection);
        Log.d(TAG, "onCreate: Minister selected");

        setContentViews();

        if (minister.getPhoto() == null) {
            Picasso.get().load(R.drawable.missing).into(Minister_Photo);
            Log.d(TAG, "onCreate: getting missing image ");
        } else {
            Log.d(TAG, "onCreate: getting Minister image");
            Picasso.get().load(minister.getPhoto()).placeholder(R.drawable.placeholder).into(Minister_Photo);
        }

        if (minister.getOrganization_party().contains("Democratic")) {
            democraticSettings();
        } else if (minister.getOrganization_party().contains("Republican")) {
            republicanSettings();
        } else {
            invalidSettings();
        }

        setTexts();

        setSocialVisibility();
        minister_Youtube.setOnClickListener(view -> openYoutubeAppLink(minister.getYoutubeLink()));

        minister_Facebook.setOnClickListener(view -> openFacebookAppLink(minister.getFacebookLink()));

        minister_Twitter.setOnClickListener(view -> openTwitterLink(minister.getTwitterLink()));

        Minister_Photo.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: Reverting back");
            if (minister.getPhoto() != null) {
                startActivity(new Intent(getApplicationContext(), ImageHandler.class).putExtra(Utility.Selection, minister));
            }
            else
            {
                Log.d(TAG, "onCreate: null Image");
            }
        });
    }

    private void setSocialVisibility() {
        if (minister.getYoutubeLink() == null) {
            minister_Youtube.setVisibility(View.GONE);
        } else {
            minister_Youtube.setVisibility(View.VISIBLE);
        }

        if (minister.getFacebookLink() == null) {
            minister_Facebook.setVisibility(View.GONE);
        } else {
            minister_Facebook.setVisibility(View.VISIBLE);
        }

        if (minister.getTwitterLink() == null) {
            minister_Twitter.setVisibility(View.GONE);
        } else {
            minister_Twitter.setVisibility(View.VISIBLE);
        }

    }

    private void setTexts() {
        AddressLine_minister.setText(minister.getUser_Address_line());
        minister_Party.setText("( " + minister.getOrganization_party() + " )");
        minister_Name.setText(minister.getOfficer_Name());
        organization_Post.setText(minister.getMinister_Post());
        minister_AddressDetails.setText(minister.getAddr_Details_plain());
        minister_Website.setText(minister.getPersonal_Website());
        minister_Email.setText(minister.getPersonal_email());
        minister_Phoneno.setText(minister.getPersonal_phone());
    }

    private void invalidSettings() {
        Log.d(TAG, "invalidSettings: Nonpartisan minister");
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
    }

    private void republicanSettings() {
        Log.d(TAG, "republicanSettings: Republican minister");
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.red));
        minister_PartySymbol.setImageDrawable(getResources().getDrawable(R.drawable.rep_logo));
    }

    private void democraticSettings() {
        Log.d(TAG, "democraticSettings: Democratic minister");
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        minister_PartySymbol.setImageDrawable(getResources().getDrawable(R.drawable.dem_logo));
    }

    private void setContentViews() {
        AddressLine_minister =  findViewById(R.id.address_tv);
        minister_PartySymbol = findViewById(R.id.Party_Photo);
        minister_Name = findViewById(R.id.tv_Name);
        minister_Email = findViewById(R.id.EmailDetails);
        organization_Post = findViewById(R.id.tv_Post);
        minister_Party = findViewById(R.id.tv_Party);

        minister_Phoneno = findViewById(R.id.PhoneDetails);
        Minister_Photo =  findViewById(R.id.Minister_Photo);
        minister_AddressDetails =  findViewById(R.id.AddressDetails);
        minister_Website = findViewById(R.id.WebsiteDetails);

        minister_Youtube =  findViewById(R.id.YoutubeBtn);
        minister_Facebook =  findViewById(R.id.FacebookBtn);
        minister_Twitter = findViewById(R.id.TwitterBtn);
        constraintLayout = findViewById(R.id.root);

    }


    public void openTwitterLink(String openTwitterLink) {
        Log.d(TAG, "openTwitterLink: Reverting to twitter");
        Intent intentopenTwitterLink = null;
        try {
            String twitterPackage="com.twitter.android";
            getPackageManager().getPackageInfo(twitterPackage, 0);
            intentopenTwitterLink = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user/screen_name=" + openTwitterLink));
            intentopenTwitterLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intentopenTwitterLink = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + openTwitterLink));
            startActivity(intentopenTwitterLink);
        }
        Log.d(TAG, "openTwitterLink: Redirecting ended");
    }

    public void openYoutubeAppLink(String openYoutubeAppLink) {
        Log.d(TAG, "openYoutubeAppLink: starting youtube");
        Intent intentopenYoutubeAppLink = null;
        try {
            intentopenYoutubeAppLink = new Intent(Intent.ACTION_VIEW);
            String youtubepackage="com.google.android.youtube";
            intentopenYoutubeAppLink.setPackage(youtubepackage);
            intentopenYoutubeAppLink.setData(Uri.parse("https://www.youtube.com/" + openYoutubeAppLink));
            startActivity(intentopenYoutubeAppLink);

        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + openYoutubeAppLink)));
        }
    }

    public void openFacebookAppLink(String openFacebookAppLink) {
        Log.d(TAG, "openFacebookAppLink: Opening FB app");
        String url;
        try {
            int FBversionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (FBversionCode >= 3002850) {
                url = "fb://facewebmodal/f?href=" + "https://www.facebook.com/" + openFacebookAppLink;
            } else {
                url = "fb://page/" + openFacebookAppLink;
            }
        } catch (Exception e) {
            url = "https://www.facebook.com/" + openFacebookAppLink;

        }
        Intent fbIntent = new Intent(Intent.ACTION_VIEW);
        fbIntent.setData(Uri.parse(url));
        startActivity(fbIntent);
    }


}