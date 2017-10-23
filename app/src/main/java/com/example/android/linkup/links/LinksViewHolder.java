package com.example.android.linkup.links;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.utils.Command;

import de.hdodenhof.circleimageview.CircleImageView;

public class LinksViewHolder extends RecyclerView.ViewHolder{

    public View view;
    public CircleImageView profilePhoto;
    public TextView header;
    public TextView lastMessage;
    public ImageView moreVert;
    public Command onLinkClickCommand;
    Command allowedChatCommand;

    public LinksViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        profilePhoto = (CircleImageView) view.findViewById(R.id.link_image);
        header = (TextView) view.findViewById(R.id.link_header);
        lastMessage = (TextView) view.findViewById(R.id.link_last_message);
        moreVert = (ImageView) view.findViewById(R.id.more_vert);
    }
}
