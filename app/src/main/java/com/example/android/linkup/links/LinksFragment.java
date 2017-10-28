package com.example.android.linkup.links;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.linkup.BaseFragment;
import com.example.android.linkup.R;
import com.example.android.linkup.models.Link;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.candidates.ActionOnCandidateResponseListener;
import com.example.android.linkup.network.candidates.GetLinksResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class LinksFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private View noLinksView;
    ImageView advertising;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_links, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new LinksAdapter(getContext(), new ArrayList<Link>()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        noLinksView = (View) view.findViewById(R.id.no_links_layout);
        advertising = (ImageView) view.findViewById(R.id.advertising);
        if (!Session.getInstance().mySettings.accountType.equals("free") ) {
            advertising.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetLinksSuccessEvent (GetLinksResponseListener.OnGetLinksSuccessEvent links) {
        if (links.links.size() == 0) {
            noLinksView.setVisibility(View.VISIBLE);
        } else {
            noLinksView.setVisibility(View.INVISIBLE);
        }
        recyclerView.setAdapter(new LinksAdapter(getActivity(),links.links));
    }

    @Subscribe
    public void onActionSuccessEvent(ActionOnCandidateResponseListener.OnActionSuccessEvent event) {
        if (event.action.equals("delete") || event.action.equals("block") || event.action.equals("report") ){
            WebServiceManager.getInstance(getActivity()).getLinks();
        }

    }

}
