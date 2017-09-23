package com.example.android.linkup.candidates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.Base64Converter;

import java.util.ArrayList;

public class CandidatesAdapter extends RecyclerView.Adapter<CandidatesViewHolder> {

    ArrayList<Profile> candidates;
    LayoutInflater inflater;
    Base64Converter converter;
    Context context;


    public CandidatesAdapter (ArrayList<Profile> candidates, Context context) {
        this.candidates = candidates;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        converter = new Base64Converter();
        this.context = context;
    }

    @Override
    public CandidatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.candidate_item, parent, false);
        return new CandidatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CandidatesViewHolder holder, final int position) {
        final Profile profile = candidates.get(position);
        holder.setIsRecyclable(false);
        holder.photo.setImageBitmap(converter.Base64ToBitmap(profile.profilePhoto));
        holder.header.setText(profile.name + ", " + profile.age );
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceManager.getInstance(context).reject(profile.id);
                holder.view.animate()
                        .translationX(-holder.view.getWidth())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                candidates.remove(profile);
                                CandidatesAdapter.this.notifyDataSetChanged();
                            }
                        });
            }
        });
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                WebServiceManager.getInstance(context).link(profile.id);
                holder.view.animate()
                        .translationX(holder.view.getWidth())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                candidates.remove(profile);
                                CandidatesAdapter.this.notifyDataSetChanged();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }
}
