package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.sadanand.civiladvocacyapp.R;
import com.squareup.picasso.Picasso;

public class ImageHandler extends AppCompatActivity {
    private TextView AddressLines,minister_Post;
    ConstraintLayout constraintLayout;
    private TextView minister_org,minister_Name;
    private ImageView minister_PartySymbol,Minister_Photo;
    private GovMinister govOfficer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        govOfficer = getIntent().getExtras().getParcelable(Utility.Selection);

        Log.d("onCreate", "Setting Contents");

        setContentViews();

        if(govOfficer.getPhoto() == null){
            Picasso.get().load(R.drawable.missing).into(Minister_Photo);
        }else{
            Picasso.get().load(govOfficer.getPhoto()).placeholder(R.drawable.placeholder).into(Minister_Photo);
        }

        settexts(govOfficer);
        if(govOfficer.getOrganization_party().contains("Republican")){
            RepublicanSelection();

        }else if(govOfficer.getOrganization_party().contains("Democratic")){
            DemocraticSelection();
        }else{
            InvalidSelection();
        }

    }

    private void InvalidSelection() {
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
    }
    private void DemocraticSelection() {
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        minister_PartySymbol.setImageDrawable(getResources().getDrawable(R.drawable.dem_logo));
    }

    private void RepublicanSelection() {
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.red));
        minister_PartySymbol.setImageDrawable(getResources().getDrawable(R.drawable.rep_logo));
    }

    private void settexts(GovMinister govOfficer) {

        minister_Post.setText(govOfficer.getMinister_Post());
        minister_Name.setText(govOfficer.getOfficer_Name());
        AddressLines.setText(govOfficer.getUser_Address_line());
        minister_org.setText(govOfficer.getOrganization_party());

    }

    private void setContentViews() {
        minister_Name = findViewById(R.id.tv_Name);
        AddressLines = findViewById(R.id.address_tv);
        minister_Post = findViewById(R.id.tv_Post);
        minister_org = findViewById(R.id.tv_Party);
        constraintLayout = findViewById(R.id.root);
        Minister_Photo = findViewById(R.id.Minister_Photo);
        minister_PartySymbol = findViewById(R.id.Party_Photo);
    }

}


