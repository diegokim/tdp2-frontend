package com.example.android.linkup.candidates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class CandidatesFragment extends Fragment {


    private RecyclerView candidatesView;
    private View view;
    private TextView noCandidatesText;
    private ImageView noCandidatesImage;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO: Get Candidates
        view = inflater.inflate(R.layout.fragment_list_of_candidates, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.candidates_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("REFRESH", "onRefresh called from SwipeRefreshLayout");
                        WebServiceManager.getInstance(view.getContext()).getCandidates();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                    }
                }
        );


        noCandidatesImage = (ImageView) view.findViewById(R.id.no_candidates_image);
        noCandidatesText = (TextView) view.findViewById(R.id.no_candidates_text);
        WebServiceManager.getInstance(view.getContext()).getCandidates();
        candidatesView = (RecyclerView) view.findViewById(R.id.recommendations_list);
        candidatesView.setHasFixedSize(true);
        candidatesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onGetCandidatesSuccess (ArrayList<Profile> profiles) {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        if (profiles.size() == 0) {
            showNoCandidates(true);
        } else {
            showNoCandidates(false);
        }
        candidatesView.setAdapter(new CandidatesAdapter(profiles,getActivity()));
    }

    public void showNoCandidates(boolean show) {
        if (show) {
            noCandidatesText.setVisibility(View.VISIBLE);
            noCandidatesImage.setVisibility(View.VISIBLE);
        } else {
            noCandidatesText.setVisibility(View.INVISIBLE);
            noCandidatesImage.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        WebServiceManager.getInstance(view.getContext()).getCandidates();
    }

    @Subscribe
    public void onNoCandidatesEvent (CandidatesAdapter.OnNoCandidatesEvent event) {
        showNoCandidates(true);
    }
}
