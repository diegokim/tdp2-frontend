package com.example.android.linkup.chat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.R;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.models.CandidateSelectedProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.profile.ProfilePagerAdapter;
import com.example.android.linkup.utils.Base64Converter;

/**
 * Created by diegokim on 10/9/17.
 */

public class ActiveChatProfileFragment extends Fragment {

    private static final String PROFILE_APP_BAR_TEXT = "Mi Perfil" ;

    private TextView name;
    private TextView age;
    private TextView gender;
    private ImageView photo;
    private Profile profile;
    private Base64Converter photoConverter;
    private Menu menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile_other,container, false);
        this.photoConverter = new Base64Converter();

        profile = ActiveChatProfile.getInstance().profile;

        findAndInitializeViews(v);
        setUpTabLayout(v);

        if (profile.name != null) {
            Bitmap bitmap = photoConverter.Base64ToBitmap(profile.profilePhoto);
            bitmap = photoConverter.getRoundedCornerBitmap(bitmap, Color.WHITE,16,5,v.getContext());
            photo.setImageBitmap(bitmap);
            name.setText(profile.name);
            age.setText(Integer.toString(profile.age) + " Años");
            gender.setText(profile.gender);
        }

        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }

    private void findAndInitializeViews(View v) {
        name = (TextView) v.findViewById(R.id.profile_name);
        age = (TextView) v.findViewById(R.id.profile_age);
        gender = (TextView) v.findViewById(R.id.profile_gender);
        photo = (ImageView) v.findViewById(R.id.profile_picture);
    }

    public void setUpTabLayout (View v) {
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Fotos"));
        tabLayout.addTab(tabLayout.newTab().setText("Intereses"));
        tabLayout.addTab(tabLayout.newTab().setText("Descripcion"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final ProfilePagerAdapter adapter = new ProfilePagerAdapter
                (getFragmentManager(), tabLayout.getTabCount(),profile);
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
