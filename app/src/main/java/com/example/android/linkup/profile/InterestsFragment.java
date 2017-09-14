package com.example.android.linkup.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class InterestsFragment extends Fragment implements Observer{

    private final Profile profile;
    private TextView noInterestsView;
    private TextView noInterestsTitleView;
    private ListView interestsListView;
    private TextView interestsTitleView;

    public InterestsFragment (Profile profile) {
        super();
        this.profile = profile;
        profile.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interests, container, false);
        noInterestsView = (TextView) view.findViewById(R.id.no_interests_text);
        noInterestsTitleView = (TextView) view.findViewById(R.id.no_interests_title);
        interestsListView = (ListView) view.findViewById(R.id.interests_list_view);
        interestsTitleView = (TextView) view.findViewById(R.id.interests_title);
        updateView();
        return view;
    }

    private void updateView () {
        if (profile.interests != null && profile.interests.length != 0 ) {
            setVisibilityOfTheInterestsList(true);
            ListAdapter adapter = new ArrayAdapter<String>(interestsListView.getContext(),R.layout.interest_layout, profile.interests);
            interestsListView.setAdapter(adapter);
        } else {
            setVisibilityOfTheInterestsList(false);
        }
    }

    public void setVisibilityOfTheInterestsList(boolean thereAreInterests) {
        if ( thereAreInterests ) {
            noInterestsView.setVisibility(View.INVISIBLE);
            noInterestsTitleView.setVisibility(View.INVISIBLE);
            interestsTitleView.setVisibility(View.VISIBLE);
            interestsListView.setVisibility(View.VISIBLE);
        } else {
            noInterestsView.setVisibility(View.VISIBLE);
            noInterestsTitleView.setVisibility(View.VISIBLE);
            interestsTitleView.setVisibility(View.INVISIBLE);
            interestsListView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateView();
    }
}
