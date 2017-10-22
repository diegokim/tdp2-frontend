package com.example.android.linkup.candidates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Candidate;
import com.example.android.linkup.models.CandidateSelectedProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.example.android.linkup.R.id.profile_picture;

public class CandidatesAdapter extends RecyclerView.Adapter<CandidatesViewHolder> {

    ArrayList<Candidate> candidates;
    ArrayList<Bitmap> photos;
    LayoutInflater inflater;
    Base64Converter converter;
    Context context;
    private long mLastClickTime = 0;


    public CandidatesAdapter (ArrayList<Candidate> candidates, Context context) {
        this.candidates = candidates;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        converter = new Base64Converter();
        photos = new ArrayList<>();
        this.context = context;
    }

    public void updateCandidates (ArrayList<Candidate> candidates) {

        if (!this.candidates.contains(candidates)) {
            this.candidates = candidates;
            photos.clear();
            for (int i = 0 ; i < candidates.size() ; i++) {
                photos.add(Base64Converter.Base64ToBitmap(candidates.get(i).profile.profilePhoto));
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public CandidatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.candidate_item, parent, false);
        return new CandidatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CandidatesViewHolder holder, final int position) {
        final Candidate candidate = candidates.get(position);
        final Profile profile = candidate.profile;

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
                                candidates.remove(candidate);
                                photos.remove(holder.getAdapterPosition());
                                if (candidates.size() == 0) {
                                    EventBus.getDefault().post(new OnNoCandidatesEvent());
                                }
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
                                candidates.remove(candidate);
                                photos.remove(holder.getAdapterPosition());
                                if (candidates.size() == 0) {
                                    EventBus.getDefault().post(new OnNoCandidatesEvent());
                                }
                                CandidatesAdapter.this.notifyDataSetChanged();
                            }
                        });
            }
        });
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                CandidatesViewHolder holder = (CandidatesViewHolder) v.getTag();
                int position = holder.getAdapterPosition();
                Profile candidateProfile = CandidatesAdapter.this.candidates.get(position).profile;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, (View) v, "profile");
                Intent intent = new Intent(context,CandidateProfileActivity.class);
                CandidateSelectedProfile.getInstance().profile = candidateProfile;
                context.startActivity(intent,options.toBundle());
            }
        });
        holder.setIsRecyclable(false);
        holder.photo.setImageBitmap(photos.get(position));
        holder.header.setText(profile.name + ", " + profile.age +" Años" );
        holder.distance.setText(candidate.distance + " Km");
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public static class OnNoCandidatesEvent {
    }

}
