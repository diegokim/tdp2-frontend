package com.example.android.linkup.candidates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.CandidateSelectedProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.Command;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;


public class CandidatesViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView photo;
    public TextView header;
    public ImageButton reject;
    public ImageButton link;
    public ImageButton superlink;
    public TextView distance;


    public CandidatesViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        header = (TextView) itemView.findViewById(R.id.candidate_header);
        photo = (ImageView) itemView.findViewById(R.id.profile_picture);
        reject = (ImageButton) itemView.findViewById(R.id.reject_button);
        link = (ImageButton) itemView.findViewById(R.id.link_button);
        superlink = (ImageButton) itemView.findViewById(R.id.superlink_button);
        distance = (TextView) itemView.findViewById(R.id.distance);
        photo.setTag(this);
    }

}
