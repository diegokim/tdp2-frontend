package com.example.android.linkup;



import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.linkup.profile.InterestsFragment;
import com.example.android.linkup.profile.PhotosFragment;
import com.example.android.linkup.sync_list.SyncListFragment;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ProfileActivity extends FragmentActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView appBarTitle = (TextView) findViewById(R.id.app_bar_title);
        appBarTitle.setText("Mi Perfil");


    }

    public void setInterestsFragment (View view) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.profile_fragment_container);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        Fragment fragment = new PhotosFragment();

        fragTransaction.add(linearLayout.getId(), fragment , "fragment");
        fragTransaction.commit();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
