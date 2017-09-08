package com.example.android.linkup;



import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android.linkup.sync_list.SyncListFragment;

import java.util.ArrayList;

public class ProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);
        // add rowLayout to the root layout somewhere here

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();


        ArrayList<String> list = new ArrayList();
        for (int i =0 ; i < 10 ; i ++ ){
            list.add("hola " + i);
        }
        SyncListFragment<String> myFrag = new SyncListFragment<>(list);

        fragTransaction.add(R.id.fragment_container, myFrag , "fragment");
        fragTransaction.commit();
    }
}
