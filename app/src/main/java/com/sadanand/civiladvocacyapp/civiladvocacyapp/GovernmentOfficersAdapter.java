package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sadanand.civiladvocacyapp.R;

import java.util.ArrayList;

public class GovernmentOfficersAdapter extends RecyclerView.Adapter<GovernmentOfficersAdapter.ViewHolder> {

    private static final String TAG = "GovernmentOfficersAdapter";
    Activity OfficerDetails;
    ArrayList<GovMinister> govMinisterArrayList;
    public GovernmentOfficersAdapter(Activity activity, ArrayList<GovMinister> arrayList) {
        Log.d(TAG, "GovernmentOfficersAdapter: Start");
        this.OfficerDetails = activity;
        this.govMinisterArrayList = arrayList;
        Log.d(TAG, "GovernmentOfficersAdapter: End");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Start");
        return new ViewHolder(OfficerDetails.getLayoutInflater().inflate(R.layout.minister_list,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Start");
        GovMinister Officer = govMinisterArrayList.get(position);
        holder.OfficerName.setText(Officer.getOfficer_Name()+" ("+Officer.getOrganization_party()+")");
        holder.Post.setText(Officer.getMinister_Post());
       /* holder.OfficerImage.setImageDrawable(R.drawable.missing);
        Picasso.get().load(Officer.getImage()).placeholder(R.drawable.placeholder).into(OfficerImage);*/
        /*if (Officer.getImage() == null) {
            Log.d(TAG, "onCreate: Fetch missing photo ");
            Picasso.get().load(R.drawable.missing).into(OfficerImage);
        } else {
            Log.d(TAG, "onCreate: Fetch minister photo");
            Picasso.get().load(Officer.getImage()).placeholder(R.drawable.placeholder).into(OfficerImage);
        }*/

        holder.itemView.setOnClickListener(view -> OfficerDetails.startActivity(new Intent(OfficerDetails, FeaturesActivity.class)
        .putExtra(Utility.Selection,Officer)));
        Log.d(TAG, "onBindViewHolder: End");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView OfficerName, Post;
        ImageView OfficerImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            OfficerName = itemView.findViewById(R.id.OfficerName);
            Post = itemView.findViewById(R.id.tv_Post);
            OfficerImage = itemView.findViewById(R.id.imageView2);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Start");
        return govMinisterArrayList.size();
    }


}
