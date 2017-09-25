package com.example.android.linkup.candidates;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.models.CandidateSelectedProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.profile.ProfilePagerAdapter;
import com.example.android.linkup.utils.Base64Converter;

public class CandidateProfileActivity extends BaseActivity {
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

        profile = CandidateSelectedProfile.getInstance().profile;

        findAndInitializeViews();
        setUpTabLayout();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(profile.name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (profile.name != null) {
            Bitmap bitmap = photoConverter.Base64ToBitmap(profile.profilePhoto);
            bitmap = photoConverter.getRoundedCornerBitmap(bitmap,Color.WHITE,16,5,this);
            photo.setImageBitmap(bitmap);
            name.setText(profile.name);
            age.setText(Integer.toString(profile.age) + " Años");
            gender.setText(profile.gender);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
        }

        return super.onOptionsItemSelected(item);
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

}
