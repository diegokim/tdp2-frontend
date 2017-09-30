package com.example.android.linkup.links;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.utils.Base64Converter;

import java.util.ArrayList;
import java.util.Random;

public class LinksAdapter extends RecyclerView.Adapter<LinksViewHolder>{

    ArrayList<Profile> links;
    LayoutInflater inflater;
    Base64Converter converter;
    Context context;

    public LinksAdapter (Context context, ArrayList<Profile> links) {
        this.links = links;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        converter = new Base64Converter();
    }

    @Override
    public LinksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_link, parent, false);
        return new LinksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LinksViewHolder holder, int position) {
        Profile profile = links.get(position);
        Bitmap photo = converter.Base64ToBitmap(profile.profilePhoto);
        //photo = converter.getRoundedCornerBitmap(photo, Color.WHITE,360,1,context);
        holder.profilePhoto.setImageBitmap(photo);
        holder.header.setText(profile.name + ", " + Integer.toString(profile.age));
        holder.lastMessage.setText("Hola como estas?");
        holder.profilePhoto.setBorderColor(23123);
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}
