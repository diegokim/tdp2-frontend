package com.example.android.linkup.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;

import java.util.Observable;
import java.util.Observer;


public class InterestsFragment extends Fragment implements Observer{

    private final Profile profile;

    public InterestsFragment (Profile profile) {
        super();
        this.profile = profile;
        profile.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interests, container, false);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
