package com.example.android.linkup.profile;

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

public class DescriptionFragment extends Fragment implements Observer {

    private final Profile profile;
    private TextView descriptionView;
    private TextView workView;
    private TextView educationView;

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
        workView = (TextView) view.findViewById(R.id.work_text);
        educationView = (TextView) view.findViewById(R.id.education_text);
        workView.setText(profile.work);
        educationView.setText(profile.education);

        if (profile.description != null) {
            if (profile.description.equals("")) {
                descriptionView.setText("Por favor aniade una descripcion.");
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
