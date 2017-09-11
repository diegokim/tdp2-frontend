package com.example.android.linkup.profile;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkRequestQueue;
import com.example.android.linkup.network.ToastErrorCommand;
import com.example.android.linkup.network.get_profile.GetProfileRequestGenerator;
import com.example.android.linkup.profile.ProfilePagerAdapter;
import com.example.android.linkup.utils.Base64Converter;

import java.util.Observable;
import java.util.Observer;

public class ProfileActivity extends FragmentActivity implements Observer {

    private static final String PROFILE_APP_BAR_TEXT = "Mi Perfil" ;
    private TextView name;
    private TextView age;
    private TextView ocupation;
    private TextView education;
    private ImageView photo;
    private Profile profile;
    private Base64Converter photoConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoConverter = new Base64Converter();
        this.profile = new Profile();
        this.profile.addObserver(this);
        setContentView(R.layout.activity_profile);
        findAndInitializeViews();
        setUpTabLayout();
        setUpAppToolBar();
        sendGetProfileRequest();
    }

    private void findAndInitializeViews() {
        name = (TextView) findViewById(R.id.profile_name);
        age = (TextView) findViewById(R.id.profile_age);
        education =(TextView) findViewById(R.id.profile_education);
        ocupation = (TextView) findViewById(R.id.profile_ocupation);
        photo = (ImageView) findViewById(R.id.profile_picture);
    }

    public void setUpTabLayout () {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Fotos"));
        tabLayout.addTab(tabLayout.newTab().setText("Intereses"));
        tabLayout.addTab(tabLayout.newTab().setText("Descripcion"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ProfilePagerAdapter adapter = new ProfilePagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),profile);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpAppToolBar() {
        TextView appBarTitle = (TextView) findViewById(R.id.app_bar_title);
        appBarTitle.setText(PROFILE_APP_BAR_TEXT);
    }

    private void sendGetProfileRequest(){
        ToastErrorCommand onErrorCommand = new ToastErrorCommand(this, NetworkConfiguration.SERVER_REQUEST_ERROR);
        Request getProfile = GetProfileRequestGenerator.generate(profile, onErrorCommand);
        NetworkRequestQueue.getInstance(this).addToRequestQueue(getProfile);
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.i("PROFILE", profile.toString());
        photo.setImageBitmap(photoConverter.Base64ToBitmap(profile.profilePhoto));
        name.setText(profile.name);
        age.setText(Integer.toString(profile.age) + " Años");
        ocupation.setText(profile.ocupation);
        education.setText(profile.education);
    }
}
