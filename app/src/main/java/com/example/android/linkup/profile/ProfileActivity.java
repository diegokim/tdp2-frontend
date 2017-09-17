package com.example.android.linkup.profile;

import android.graphics.Bitmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.login.AuthManager;

import com.example.android.linkup.models.Profile;


import com.example.android.linkup.network.WebServiceManager;

import com.example.android.linkup.network.get_profile.GetProfileResponseListener;
import com.example.android.linkup.utils.Base64Converter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ProfileActivity extends BaseActivity {

    private static final String PROFILE_APP_BAR_TEXT = "Mi Perfil" ;

    private TextView name;
    private TextView age;
    private TextView gender;
    private ImageView photo;
    private Profile profile;
    private Base64Converter photoConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoConverter = new Base64Converter();
        this.profile = new Profile();

        setContentView(R.layout.activity_profile);
        findAndInitializeViews();
        setUpTabLayout();
        setUpAppToolBar();
        showProgressDialog();
        EventBus.getDefault().register(this);
        WebServiceManager.getInstance(this).getProfile();
    }


    private void findAndInitializeViews() {
        name = (TextView) findViewById(R.id.profile_name);
        age = (TextView) findViewById(R.id.profile_age);
        gender = (TextView) findViewById(R.id.profile_gender);
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
        Button button = (Button) findViewById(R.id.log_out);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AuthManager.getInstance(ProfileActivity.this).signOut();
            }
        });

    }


    @Subscribe
    public void onGetProfileSuccessEvent(GetProfileResponseListener.GetProfileSuccessEvent event) {
        hideProgressDialog();
        this.profile.update(event.profile);
        this.profile.commitChanges();
        if (profile.name != null) {
            Bitmap bitmap = photoConverter.Base64ToBitmap(profile.profilePhoto);
            bitmap = photoConverter.getRoundedCornerBitmap(bitmap,Color.WHITE,16,5,this);
            photo.setImageBitmap(bitmap);
            name.setText(profile.name);
            age.setText(Integer.toString(profile.age) + " Años");
            gender.setText(profile.gender);
        }
    }

    @Subscribe
    public void onErrorMessageEvent(WebServiceManager.ErrorMessageEvent error) {
        hideProgressDialog();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, error.message, duration);
        toast.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
