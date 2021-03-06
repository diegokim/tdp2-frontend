package com.example.android.linkup.login;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.MainActivity;
import com.example.android.linkup.R;

import com.example.android.linkup.login.register_parameters_selection.SelectPhotosCommand;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.location_update.UpdateLocationRequestGenerator;
import com.example.android.linkup.network.login.LoginResponseListener;
import com.example.android.linkup.network.register.RegisterResponseListener;
import com.example.android.linkup.utils.Command;
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
    private static final int ACCESS_FINE_REQUEST_CODE = 1;
    private AuthManager authManager;
    private LoginButton loginButton;
    private TextView title;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationListener locationListener;
    private Button allowLocationButton;
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

        allowLocationButton = (Button) findViewById(R.id.allow_location_button);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            allowLocationButton.setVisibility(View.VISIBLE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        } else {
            allowLocationButton.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            getLocation(new Command() {
                @Override
                public void excecute() {

                }
            });
        }
        if (authManager.userIsLoggedIn()) {
            NetworkConfiguration.getInstance().accessToken = authManager.getAccessToken();
            showProgressDialog();
            login();
        }
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
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onFacebookAuthCompleteEvent (AuthManager.OnFacebookAuthCompleteEvent event) {
        login();
    }

    private void login() {
        if (mLocation != null) {
            WebServiceManager.getInstance(this).login();
        } else {
            getLocation(new Command() {
                @Override
                public void excecute() {
                    WebServiceManager.getInstance(getApplicationContext()).login();
                }
            });
        }
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
    public void onRegisterSuccessEvent(RegisterResponseListener.OnRegisterSuccessEvent event) {
        showProgressDialog();
        login();
    }

    @Subscribe
    public void onLoginSuccessEvent (RegisterResponseListener.OnLoginSuccessEvent event) {
        Session.getInstance().myProfile.update(event.profile);
        Session.getInstance().mySettings.update(event.settings);
        WebServiceManager.getInstance(this).updateLocation(mLocation);
    }

    @Subscribe
    public void onUpdateLocationSuccessEvent (UpdateLocationRequestGenerator.OnUpdateLocationSuccessEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        hideProgressDialog();
        startActivity(intent);
        finish();
    }

    public void setLocationPermission(View view) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_FINE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    allowLocationButton.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    getLocation(new Command() {
                        @Override
                        public void excecute() {

                        }
                    });
                }
                return;
            }
        }
    }

    public void getLocation(final Command command) {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                command.excecute();
                mLocationManager.removeUpdates(locationListener);
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

        mLocationManager.requestLocationUpdates("gps", 0, 0, locationListener);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mLocation = location;
                            command.excecute();
                        } else {
                            Log.d("ASD","ASD");
                        }
                    }
                });
    }
}
