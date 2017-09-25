package com.example.android.linkup.links;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;

public class LinksViewHolder extends RecyclerView.ViewHolder{

    public View view;
    public ImageView profilePhoto;
    public TextView header;
    public TextView lastMessage;

    public LinksViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        profilePhoto = (ImageView) view.findViewById(R.id.link_image);
        header = (TextView) view.findViewById(R.id.link_header);
        lastMessage = (TextView) view.findViewById(R.id.link_last_message);
    }
}
