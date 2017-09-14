package com.example.android.linkup.profile;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.linkup.R;
import com.example.android.linkup.login.LoginActivity;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkRequestQueue;
import com.example.android.linkup.network.ToastErrorCommand;
import com.example.android.linkup.network.get_profile.GetProfileRequestGenerator;
import com.example.android.linkup.profile.ProfilePagerAdapter;
import com.example.android.linkup.utils.Base64Converter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Observable;
import java.util.Observer;

public class ProfileActivity extends FragmentActivity implements Observer {

    private static final String PROFILE_APP_BAR_TEXT = "Mi Perfil" ;
    public static final String LOGGED_IN = "com.example.android.weatherapp.LOGGED_IN";
    public static final String ACCESS_TOKEN = "com.example.android.weatherapp.ACCESS_TOKEN";

    private TextView name;
    private TextView age;
    private TextView gender;
//    private TextView work;
//    private TextView education;
    private ImageView photo;
    private Profile profile;
    private Base64Converter photoConverter;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoConverter = new Base64Converter();
        this.profile = new Profile();
        this.profile.addObserver(this);

        setContentView(R.layout.activity_profile);
        findAndInitializeViews();
        saveLoginState();
        setUpTabLayout();
        setUpAppToolBar();
        showProgressDialog();
        sendGetProfileRequest();
    }

    public void saveLoginState () {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ACCESS_TOKEN, NetworkConfiguration.getInstance().accessToken);
        editor.putBoolean(LOGGED_IN, true);
        editor.apply();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        NetworkConfiguration.getInstance().accessToken = "-1";
        saveLogoutState();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveLogoutState () {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(LOGGED_IN, false);
        editor.apply();
    }

    private void findAndInitializeViews() {
        name = (TextView) findViewById(R.id.profile_name);
        age = (TextView) findViewById(R.id.profile_age);
        gender = (TextView) findViewById(R.id.profile_gender);
//        education =(TextView) findViewById(R.id.profile_education);
//        work = (TextView) findViewById(R.id.profile_ocupation);
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
                ProfileActivity.this.signOut();
            }
        });

    }

    private void sendGetProfileRequest(){
        ToastErrorCommand onErrorCommand = new ToastErrorCommand(this, NetworkConfiguration.SERVER_REQUEST_ERROR);
        Request getProfile = GetProfileRequestGenerator.generate(profile, onErrorCommand);
        NetworkRequestQueue.getInstance(this).addToRequestQueue(getProfile);
    }

    @Override
    public void update(Observable o, Object arg) {
        hideProgressDialog();
        if (profile.name != null) {
            Bitmap bitmap = photoConverter.Base64ToBitmap(profile.profilePhoto);
            bitmap = photoConverter.getRoundedCornerBitmap(bitmap,Color.WHITE,16,5,this);
            photo.setImageBitmap(bitmap);
            name.setText(profile.name);
            age.setText(Integer.toString(profile.age) + " Años");
            gender.setText(profile.gender);
        }
    }


}
