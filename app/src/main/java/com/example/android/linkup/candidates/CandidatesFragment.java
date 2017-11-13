package com.example.android.linkup.candidates;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.example.android.linkup.BaseFragment;
import com.example.android.linkup.R;
import com.example.android.linkup.models.Candidate;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.advertising.AdvertisingResponseListener;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class CandidatesFragment extends BaseFragment {


    private RecyclerView candidatesView;
    private View view;
    private TextView noCandidatesText;
    private ImageView noCandidatesImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CandidatesAdapter mAdapter;
    private long mLastClickTime;
    private ImageView advertising;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO: Get Candidates


        showProgressDialog();
        view = inflater.inflate(R.layout.fragment_list_of_candidates, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.candidates_refresh_layout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(false);
                        showProgressDialog();
                        WebServiceManager.getInstance(view.getContext()).getCandidates();
                    }
                }
        );


        advertising = (ImageView) view.findViewById(R.id.advertising);
        if (!Session.getInstance().mySettings.accountType.equals("free") ) {
            advertising.setVisibility(View.GONE);
        }

        noCandidatesImage = (ImageView) view.findViewById(R.id.no_candidates_image);
        noCandidatesText = (TextView) view.findViewById(R.id.no_candidates_text);
        WebServiceManager.getInstance(view.getContext()).getCandidates();
        candidatesView = (RecyclerView) view.findViewById(R.id.recommendations_list);
        mAdapter = new CandidatesAdapter(new ArrayList<Candidate>(),getActivity());
        candidatesView.setAdapter(mAdapter);
        candidatesView.setHasFixedSize(true);
        candidatesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLastClickTime = 0;

        WebServiceManager.getInstance().getAdvertising();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog();
        WebServiceManager.getInstance().getAdvertising();
        WebServiceManager.getInstance(getContext()).getCandidates();
        if (!Session.getInstance().mySettings.accountType.equals("free") ) {
            advertising.setVisibility(View.GONE);
        } else {
            advertising.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        WebServiceManager.getInstance().getAdvertising();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onGetCandidatesSuccess (ArrayList<Candidate> candidates) {
        WebServiceManager.getInstance().getAdvertising();
        hideProgressDialog();
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        if (candidates.size() == 0) {
            showNoCandidates(true);
        } else {
            showNoCandidates(false);
        }
        mAdapter.updateCandidates(candidates);
    }

    public void showNoCandidates(boolean show) {
        if (show) {
            noCandidatesText.setVisibility(View.VISIBLE);
            noCandidatesImage.setVisibility(View.VISIBLE);
        } else {
            noCandidatesText.setVisibility(View.GONE);
            noCandidatesImage.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onErrorMessage(WebServiceManager.ErrorMessageEvent event) {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        hideProgressDialog();
    }

    @Subscribe
    public void onNoCandidatesEvent (CandidatesAdapter.OnNoCandidatesEvent event) {
        hideProgressDialog();
        showNoCandidates(true);
    }

    @Subscribe
    public void OnGetAdvertisingSuccessEvent(AdvertisingResponseListener.OnGetAdvertisingSuccessEvent event) {
        if( event.ads.size() >0 ) {
            Bitmap originalBitmap = event.ads.get(0).image;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap,290,75,false);
            advertising.setImageBitmap(resizedBitmap);
        }
    }
}
