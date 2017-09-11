package com.example.android.linkup.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;

import org.w3c.dom.Text;

import java.util.Observable;
import java.util.Observer;


public class InterestsFragment extends Fragment implements Observer{

    private final Profile profile;
    private TextView interestsView;

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
        if (profile.interests != null) {
            if (profile.interests.length == 0) {
                interestsView.setText("Por favor ingresa intereses a tu cuenta de Facebook para que " +
                        "podamos brindarte una mejor experiencia, ya que nos basamos en ellos para " +
                        "recomendarte personas.");
            } else {
                //TODO listview;
            }
        } else {
            interestsView.setText("Por favor ingresa intereses a tu cuenta de Facebook para que " +
                    "podamos brindarte una mejor experiencia, ya que nos basamos en ellos para " +
                    "recomendarte personas.");
        }
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        //Todo update listview
    }
}
