package com.example.android.linkup.profile;


import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;

import com.example.android.linkup.models.Profile;


import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;

import com.example.android.linkup.network.get_profile.GetProfileResponseListener;
import com.example.android.linkup.profile.edit_profile.EditProfileActivity;
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
    private Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.photoConverter = new Base64Converter();
        this.profile = Session.getInstance().myProfile;
        findAndInitializeViews();
        setUpTabLayout();
        showProgressDialog();
        EventBus.getDefault().register(this);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(PROFILE_APP_BAR_TEXT);

        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_account_box_black_24dp  , getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        WebServiceManager.getInstance(this).getProfile();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuItem item = menu.add("Editar Perfil");
        item.setIcon(R.drawable.com_facebook_button_icon);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
