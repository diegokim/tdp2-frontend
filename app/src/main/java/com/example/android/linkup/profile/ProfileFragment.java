package com.example.android.linkup.profile;


import android.graphics.Bitmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.BaseFragment;
import com.example.android.linkup.R;

import com.example.android.linkup.models.Profile;


import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;

import com.example.android.linkup.network.get_profile.GetProfileResponseListener;
import com.example.android.linkup.utils.Base64Converter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ProfileFragment extends BaseFragment {

    private static final String PROFILE_APP_BAR_TEXT = "Mi Perfil" ;

    private TextView name;
    private TextView age;
    private TextView gender;
    private ImageView photo;
    private Profile profile;
    private Base64Converter photoConverter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        this.photoConverter = new Base64Converter();
        this.profile = Session.getInstance().myProfile;
        findAndInitializeViews(view);
        setUpTabLayout(view);
        this.view = view;
        showProgressDialog();
        EventBus.getDefault().register(this);
        WebServiceManager.getInstance(view.getContext()).getProfile();
        return view;
    }

    private void findAndInitializeViews(View view) {
        name = (TextView) view.findViewById(R.id.profile_name);
        age = (TextView) view.findViewById(R.id.profile_age);
        gender = (TextView) view.findViewById(R.id.profile_gender);
        photo = (ImageView) view.findViewById(R.id.profile_picture);
    }

    public void setUpTabLayout (View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Fotos"));
        tabLayout.addTab(tabLayout.newTab().setText("Intereses"));
        tabLayout.addTab(tabLayout.newTab().setText("Descripcion"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
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


    @Subscribe
    public void onGetProfileSuccessEvent(GetProfileResponseListener.GetProfileSuccessEvent event) {
        hideProgressDialog();
        this.profile.update(event.profile);
        this.profile.commitChanges();
        if (profile.name != null) {
            Bitmap bitmap = photoConverter.Base64ToBitmap(profile.profilePhoto);
            bitmap = photoConverter.getRoundedCornerBitmap(bitmap,Color.WHITE,16,5,view.getContext());
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
        Toast toast = Toast.makeText(view.getContext(), error.message, duration);
        toast.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
