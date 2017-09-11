package com.example.android.linkup.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;

import java.util.Observable;
import java.util.Observer;

public class DescriptionFragment extends Fragment implements Observer {

    private final Profile profile;
    private TextView descriptionView;

    public DescriptionFragment (Profile profile) {
        super();
        this.profile = profile;
        profile.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        descriptionView = (TextView) view.findViewById(R.id.description_fragment_text);
        if (profile.description != null) {
            if (profile.description.equals("")) {
                descriptionView.setText("Por favor ingresa intereses a tu cuenta de Facebook para que " +
                        "podamos brindarte una mejor experiencia, ya que nos basamos en ellos para " +
                        "recomendarte personas.");
            } else {
                descriptionView.setText(profile.description);
            }
        }
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        descriptionView.setText(profile.description);
    }
}
