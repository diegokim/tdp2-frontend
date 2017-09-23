package com.example.android.linkup.login;

import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.MainActivity;
import com.example.android.linkup.R;

import com.example.android.linkup.login.register_parameters_selection.SelectPhotosCommand;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.location_update.UpdateLocationRequestGenerator;
import com.example.android.linkup.network.login.LoginResponseListener;
import com.example.android.linkup.network.register.RegisterRequestGenerator;
import com.facebook.FacebookSdk;

import com.facebook.login.widget.LoginButton;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class LoginActivity extends BaseActivity {

    private static final String TAG = "FacebookLogin";
    private AuthManager authManager;
    private LoginButton loginButton;
    private TextView title;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationListener locationListener;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        authManager = AuthManager.getInstance(this);
        findViews();
        authManager.initializeFacebookLoginButton(loginButton);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocationManager.requestLocationUpdates("gps",1,10000, locationListener);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLocation = location;
                        }
                    }
                });
    }

    private void findViews() {
        loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        title = (TextView) findViewById(R.id.title_text);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.passActivityResultToFacebookSDK(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (authManager.userIsLoggedIn()) {
            NetworkConfiguration.getInstance().accessToken = authManager.getAccessToken();
            showProgressDialog();
            login();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onFacebookAuthCompleteEvent (AuthManager.OnFacebookAuthCompleteEvent event) {
        if (mLocation != null) {
            login();
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLocation = location;
                                login();
                            } else {
                                Log.e("Get Location ERROR", "Cant get location");
                            }
                        }
                    });
        }
    }

    private void login() {
        WebServiceManager.getInstance(this).login();
    }

    @Subscribe
    public void onRegisterNeeded(LoginResponseListener.PhotosEvent photosEvent) {
        hideProgressDialog();
        Photos photos = photosEvent.photos;
        SelectPhotosCommand command = new SelectPhotosCommand(LoginActivity.this, photos, mLocation);
        command.excecute();
    };

    @Subscribe
    public void onErrorMessageEvent(WebServiceManager.ErrorMessageEvent error) {
        hideProgressDialog();
        authManager.signOut();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, error.message, duration);
        toast.show();
    }

    @Subscribe
    public void onLoginSuccessEvent (RegisterRequestGenerator.RegisterResponseListener.OnLoginSuccessEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        //WebServiceManager.getInstance(this).updateLocation(mLocation);
    }

    @Subscribe
    public void onUpdateLocationSuccessEvent (UpdateLocationRequestGenerator.OnUpdateLocationSuccessEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
