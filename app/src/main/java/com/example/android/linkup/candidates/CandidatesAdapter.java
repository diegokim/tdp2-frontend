package com.example.android.linkup.candidates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Candidate;
import com.example.android.linkup.models.CandidateSelectedProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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
        holder.photo.setImageBitmap(photos.get(position));
        holder.header.setText(profile.name + ", " + profile.age +" Años" );
        holder.distance.setText(candidate.distance + " Km");
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, candidates.size());
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
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
                                WebServiceManager.getInstance(context).link(profile.id);
                            }
                        });
            }
        });
        holder.superlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (Session.getInstance().mySettings.superLinksCount == 0) {
                    Toast toast = Toast.makeText(context, "Ya no te quedan SuperLinks", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

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
                                WebServiceManager.getInstance(context).superLink(profile.id);
                                Session.getInstance().mySettings.superLinksCount--;
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
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public static class OnNoCandidatesEvent {
    }

}
