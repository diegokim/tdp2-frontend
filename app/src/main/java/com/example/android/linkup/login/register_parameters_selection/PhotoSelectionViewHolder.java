package com.example.android.linkup.login.register_parameters_selection;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.android.linkup.R;


public class PhotoSelectionViewHolder extends RecyclerView.ViewHolder {
    public ImageView photoImageView;
    public Boolean isSelected;

    public PhotoSelectionViewHolder(View itemView) {
        super(itemView);
        photoImageView = (ImageView) itemView.findViewById(R.id.photo);
        photoImageView.setTag(this);
    }

    public void bind(final String item, Boolean isSelected, Bitmap photo, final PhotoSelectionAdapter.Listener listener) {
        this.photoImageView.setImageBitmap(photo);
        this.isSelected = isSelected;
        if (this.isSelected) {
            photoImageView.setImageAlpha(160);
        }
        this.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(item, photoImageView);

            }
        });
    }

    public void update (boolean isSelected) {
        this.isSelected = isSelected;
        if (this.isSelected) {
            photoImageView.setImageAlpha(160);
        } else {
            photoImageView.setImageAlpha(255);
        }
    }


}
