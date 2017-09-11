package com.example.android.linkup.login.photo_selection;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;


public class PhotoSelectionViewHolder extends RecyclerView.ViewHolder {
    public ImageView photoImageView;

    public PhotoSelectionViewHolder(View itemView) {
        super(itemView);
        photoImageView = (ImageView) itemView.findViewById(R.id.photo);
        //this.isSelected = false;
    }

    public void bind(final String item, Bitmap photo, final PhotoSelectionAdapter.Listener listener) {
        this.photoImageView.setImageBitmap(photo);

        this.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(item, photoImageView);

            }
        });

    }


}
