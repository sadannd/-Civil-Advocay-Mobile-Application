package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sadanand.civiladvocacyapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivityTag";

    private FusedLocationProviderClient getprovicerClient;
    private static int changeTime = 1000;
    private Location getendLocation;
    LocationRequest getLocReq;

    TextView userAddr;
    String address_init = "";
    private static int wait_time = 5;
    public static final int Permissions = 10;

    HashMap<String, String> OfficerDesignation = new HashMap<>();
    ArrayList<GovMinister> OfficersArrayList = new ArrayList();
    Double latitude, longitude;
    RecyclerView GoveOfficList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: init");
        GoveOfficList = findViewById(R.id.GovernmentOfficers_recycler);
        GoveOfficList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        GoveOfficList.setAdapter(new GovernmentOfficersAdapter(this, OfficersArrayList));
        if (PermissiAccess()) {
            settingUpdatingLocations();
            Log.d(TAG, "Changes done after permission granted");

        }
        userAddr = findViewById(R.id.address_tv);
        userAddr.setOnClickListener(view -> SearchAddressIcon());
    }

    private void settingUpdatingLocations() {
        changeLoc();
        getLocationStnadards();
        showLocations();
    }


    public void changeLoc() {
        try{
            if (getprovicerClient != null) {
                getprovicerClient.removeLocationUpdates(callback).addOnCompleteListener(task -> {
                    Log.d(TAG, "Stopped by LocationUpdate!");
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getLocationStnadards() {
            Log.d(TAG, "FetchLocationPref: start");
            getprovicerClient = null;
            getprovicerClient = LocationServices.getFusedLocationProviderClient(this);
            getLocReq = LocationRequest.create();
            setlocreqparams(getLocReq);
    }

    private void setlocreqparams(LocationRequest getLocReq) {
        getLocReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        getLocReq.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        getLocReq.setFastestInterval(changeTime);
        getLocReq.setSmallestDisplacement(wait_time);
        getLocReq.setInterval(changeTime);
    }

    private void showLocations() {
        Log.d(TAG, "showLocations: in");
        try {
            getprovicerClient.requestLocationUpdates(getLocReq,callback, Looper.myLooper());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private final LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            getendLocation = locationResult.getLastLocation();
            double latitude = getendLocation.getLatitude();
            double longitude = getendLocation.getLongitude();
            address_init = getLastLoc(latitude, longitude);
            callApi();
        }
    };

    @SuppressLint("SetTextI18n")
    private void callApi() {
        Log.d(TAG, "in callApi");
        String Apiurl;
        if(Utility.checkInternetConn(this)){
            AlertOnNOConnection();
        }else {
            RequestQueue newReqQ = Volley.newRequestQueue(this);
            if(address_init.equals("")){
                userAddr.setText("No data found for this location");
            }else{
                userAddr.setText(address_init);
                try {
                    address_init = URLEncoder.encode(address_init, "utf-8");
                    String address_final=address_init;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Apiurl = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + Utility.KEY + "&address=" + address_init;
                Log.d(TAG, "FinalUrl is:-"+Apiurl);

                StringRequest MakeStringRequest = new StringRequest(Request.Method.GET, Apiurl, response -> {
                    //Log.d("The API response is", response.toString());
                    handleJsonRes(response);
                }, error -> Log.i(TAG, "error in response:" + error.toString()));

                newReqQ.add(MakeStringRequest);
            }
        }
    }

    private void handleJsonRes(String response) {
        OfficersArrayList.clear();
        try {
            JSONObject jsonobj = new JSONObject(response);
            String officials="officials";
            String offices="offices";

            JSONArray GovPosition = jsonobj.getJSONArray(officials);
            JSONArray organization_Post = jsonobj.getJSONArray(offices);
            JSONObject simplified_Input = jsonobj.getJSONObject("normalizedInput");

            for (int k = 0; k < organization_Post.length(); k++) {
                JSONArray index = organization_Post.getJSONObject(k).getJSONArray("officialIndices");
                for (int d = 0; d < index.length(); d++) {
                    OfficerDesignation.put(index.getString(d), organization_Post.getJSONObject(k).getString("name"));
                    Log.d(TAG, "handleJsonRes:looping for posts");
                }

            }
            Log.d(TAG, "handleJsonRes:Iterating OfficerDesignation");

            for (String name : OfficerDesignation.keySet()) {
                String key_name = name;
                String value = OfficerDesignation.get(name);
                Log.d(TAG, "Designation is:-"+value);
            }

            for (int itr = 0; itr < GovPosition.length(); itr++) {
                JSONObject item = GovPosition.getJSONObject(itr);
                GovMinister govMinister = new GovMinister();
                String officer_name_param="name";
                govMinister.setOfficer_Name(item.getString(officer_name_param));
                String officer_party="party";
                govMinister.setOrganization_party(item.getString(officer_party));
                govMinister.setMinister_Post(OfficerDesignation.get(itr + ""));

                try {
                    String email_param="emails";
                    govMinister.setPersonal_email(item.getJSONArray(email_param).getString(0));
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    String addr_param="address";
                    JSONObject TempAddr = item.getJSONArray(addr_param).getJSONObject(0);
                    govMinister.setAddr_Details_plain(TempAddr.getString("line1") + " " + TempAddr.getString("city") + " " + TempAddr.getString("state") + " " + TempAddr.getString("zip"));
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    String photoUrl="photoUrl";
                    govMinister.setPhoto(item.getString(photoUrl));
                }catch (Exception e){
                    e.printStackTrace();

                }

                govMinister.setUser_Address_line(simplified_Input.getString("line1") + " " + simplified_Input.getString("city") + " " + simplified_Input.getString("state") + " " + simplified_Input.getString("zip"));

                try{
                    String jsonUrl="urls";
                    JSONArray urls = item.getJSONArray(jsonUrl);
                    govMinister.setPersonal_Website(urls.getString(0));

                }catch (Exception exception){
                    exception.printStackTrace();

                }
                try{
                    String social_type="type";
                    String social_id="id";
;                    JSONArray SocialMedia = item.getJSONArray("channels");
                    for (int v = 0; v < SocialMedia.length(); v++) {
                        String fb="Facebook";
                        if (SocialMedia.getJSONObject(v).getString(social_type).equals(fb)) {
                            govMinister.setFacebookLink(SocialMedia.getJSONObject(v).getString(social_id));
                        }
                        if (SocialMedia.getJSONObject(v).getString(social_type).equals("Twitter")) {
                            govMinister.setTwitterLink(SocialMedia.getJSONObject(v).getString(social_id));
                        }
                        String yt="YouTube";

                        if (SocialMedia.getJSONObject(v).getString(social_type).equals(yt)) {
                            govMinister.setYoutubeLink(SocialMedia.getJSONObject(v).getString(social_id));
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    govMinister.setPersonal_phone(item.getJSONArray("phones").getString(0));

                }catch (Exception e){
                    e.printStackTrace();
                }
                OfficersArrayList.add(govMinister);
            }

            GoveOfficList.getAdapter().notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    String[] permi = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean PermissiAccess() {
        int Check_permission;
        List<String> PermissionsList = new ArrayList<>();
        for (String p : permi) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), p) != PackageManager.PERMISSION_GRANTED) {
                PermissionsList.add(p);
            }
        }
        if (!PermissionsList.isEmpty()) {
            Log.d(TAG, "PermissionsList is Empty");
            ActivityCompat.requestPermissions(this, PermissionsList.toArray(new String[PermissionsList.size()]), Permissions);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        switch (requestCode) {
            case Permissions: {
                String emptyString="";
                if (grantResults.length > 0) {
                    String permiDenied = emptyString;
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permiDenied += "\n" + per;
                        }
                    }
                    if (permiDenied.equals("")) {
                        settingUpdatingLocations();
                    }
                }
                return;
            }
            default:{
                Log.d(TAG, "Invalid Request Code");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_help){
            Log.d(TAG, "onOptionsItemSelected help Selected");
            startActivity(new Intent(getApplicationContext(), AboutAct.class));
        }
        else if(item.getItemId() == R.id.menu_search){
            Log.d(TAG, "onOptionsItemSelected search selected");
            SearchAddressIcon();
        }
        return super.onOptionsItemSelected(item);
    }


    private String getLastLoc(double lat, double longitude) {
        String emptyString="";
        String AddressString = emptyString;
        Geocoder geocordinates = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> AddrList = geocordinates.getFromLocation(lat, longitude, 1);
            if (AddrList != null) {
                Address ReturnedAddr = AddrList.get(0);
                StringBuilder ReturnedAddressString = new StringBuilder("");

                for (int i = 0; i <= ReturnedAddr.getMaxAddressLineIndex(); i++) {
                    ReturnedAddressString.append(ReturnedAddr.getAddressLine(i)).append("\n");
                }
                AddressString = String.valueOf(ReturnedAddressString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AddressString;
    }

    public  void AlertOnNOConnection(){
        AlertDialog noInternetalertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("No Internet Connection")
                .setMessage("Data cannot be accessed/loaded without an internet connection")
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(getIntent()))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                })
                .show();
    }

    public  void SearchAddressIcon(){
        if(Utility.checkInternetConn(this)){
            AlertOnNOConnection();
        }else {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
            final EditText edittextAlert = new EditText(getApplicationContext());
            String enterAddress="Enter Address";
            alertdialog.setTitle(enterAddress);
            alertdialog.setView(edittextAlert);
            alertdialog.setPositiveButton("ok", (dialog, whichButton) -> {
                Editable text = edittextAlert.getText();
                address_init = edittextAlert.getText().toString();
                callApi();
                Toast.makeText(MainActivity.this, text.toString(), Toast.LENGTH_SHORT).show();
            });
            alertdialog.setNegativeButton("Cancel", (dialog, whichButton) -> {
            });
            alertdialog.show();
        }
    }
}



