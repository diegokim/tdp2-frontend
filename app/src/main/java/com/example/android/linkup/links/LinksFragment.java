package com.example.android.linkup.links;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.candidates.GetLinksRequestGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class LinksFragment extends Fragment {

    private RecyclerView recyclerView;
    private View noLinksView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_links, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        noLinksView = (View) view.findViewById(R.id.no_links_layout);
        WebServiceManager.getInstance(getActivity()).getLinks();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        WebServiceManager.getInstance(getActivity()).getLinks();
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
    public void onGetLinksSuccessEvent (GetLinksRequestGenerator.OnGetLinksSuccessEvent links) {
        if (links.profiles.size() == 0) {
            noLinksView.setVisibility(View.VISIBLE);
        } else {
            noLinksView.setVisibility(View.INVISIBLE);
        }
        recyclerView.setAdapter(new LinksAdapter(getActivity(),links.profiles));
    }

}
