package com.example.android.linkup.candidates;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;

import org.greenrobot.eventbus.EventBus;


public class CandidatesViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView photo;
    public TextView header;
    public ImageButton reject;
    public ImageButton link;
    public ImageButton superlink;

    public CandidatesViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        header = (TextView) itemView.findViewById(R.id.candidate_header);
        photo = (ImageView) itemView.findViewById(R.id.card_image);
        reject = (ImageButton) itemView.findViewById(R.id.reject_button);
        link = (ImageButton) itemView.findViewById(R.id.link_button);
        superlink = (ImageButton) itemView.findViewById(R.id.superlink_button);
    }

}
