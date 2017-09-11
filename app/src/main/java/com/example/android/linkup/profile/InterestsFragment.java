package com.example.android.linkup.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class InterestsFragment extends Fragment implements Observer{

    private final Profile profile;
    private TextView interestsView;
    private ListView interestsListView;

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
        interestsView = (TextView) view.findViewById(R.id.interests_fragment_text);
        interestsListView = (ListView) view.findViewById(R.id.interests_list_view);
        updateView();
        return view;
    }

    private void updateView () {
        if (profile.interests != null) {
            if (profile.interests.length == 0) {
                interestsView.setVisibility(View.VISIBLE);
                interestsListView.setVisibility(View.INVISIBLE);
                interestsView.setText("Aun no tienes intereses, los cuales son una parte " +
                        "fundamental para poder recomendarte personas, por favor selecciona algunos " +
                        "en facebook, para que podamos brindarte una mejor experiencia!");
            } else {
                interestsView.setVisibility(View.INVISIBLE);
                interestsListView.setVisibility(View.VISIBLE);
                ListAdapter adapter = new ArrayAdapter<String>(interestsListView.getContext(),R.layout.interest_layout, profile.interests);
                interestsListView.setAdapter(adapter);
            }
        } else {
            interestsView.setVisibility(View.VISIBLE);
            interestsListView.setVisibility(View.INVISIBLE);
            interestsView.setText("Aun no tienes intereses, los cuales son una parte " +
                    "fundamental para poder recomendarte personas, por favor selecciona algunos " +
                    "en facebook, para que podamos brindarte una mejor experiencia!");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateView();
    }
}
