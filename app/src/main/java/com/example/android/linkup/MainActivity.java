package com.example.android.linkup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.candidates.CandidatesFragment;
import com.example.android.linkup.links.LinksFragment;
import com.example.android.linkup.login.LoginActivity;
import com.example.android.linkup.models.Session;

import com.example.android.linkup.network.WebServiceManager;

import com.example.android.linkup.premium.PremiumActivity;
import com.example.android.linkup.settings.SettingsActivity;

import com.example.android.linkup.profile.ProfileActivity;
import com.example.android.linkup.utils.Base64Converter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.List;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends BaseActivity implements Observer{

    private static final int REQUEST_UPDATE_SETTINGS = 1;
    private DrawerLayout mDrawerLayout;
    protected ViewPager fragmentContainer;
    protected Menu menu;
    private Base64Converter converter;
    private NavigationView navView;
    private CandidatesFragment candidatesFragment;

    @Override
    protected void onResume(){
        super.onResume();
        WebServiceManager.getInstance(this).updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentContainer = (ViewPager) findViewById(R.id.fragment_container);
        setupViewPager(fragmentContainer);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(fragmentContainer);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String title = tab.getText().toString();
                //WebServiceManager.getInstance().getAdvertising();
                if ( title.equals("Links")) {
                    WebServiceManager.getInstance(MainActivity.this).getLinks();
                } else if (title.equals("Personas")){
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //WebServiceManager.getInstance().getAdvertising();
                String title = tab.getText().toString();
                if ( title.equals("Links")) {
                    WebServiceManager.getInstance(MainActivity.this).getLinks();
                } else if (title.equals("Personas")){
                    WebServiceManager.getInstance(MainActivity.this).getCandidates();
                }
            }
        });

        Session.getInstance().myProfile.addObserver(this);

        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.item_profile:
                                startActivity(new Intent (MainActivity.this,ProfileActivity.class));
                                break;
                            case R.id.item_prefs:
                                Log.d("DEBUG", "Aprete mis preferencias");
                                startActivityForResult(new Intent (MainActivity.this,SettingsActivity.class),REQUEST_UPDATE_SETTINGS);
                                break;
                            case R.id.item_logout:
                                Toast.makeText(MainActivity.this, "Cerrar sesion",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                finish();
                                break;
                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        navView = navigationView;
        updateNavHeaderView();
//        showCandidatesFragment();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    WebServiceManager.getInstance().getAdvertising();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        WebServiceManager.getInstance(this).updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_UPDATE_SETTINGS) {
            updateNavHeaderView();
            //WebServiceManager.getInstance().getAdvertising();
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                candidatesFragment.showProgressDialog();
                WebServiceManager.getInstance(this).getCandidates();
                // Do something with the contact here (bigger example below)
            }
        }
    }

    private void showCandidatesFragment() {
        CandidatesFragment candidatesFragment = new CandidatesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, candidatesFragment).commit();
    }


    private void updateNavHeaderView() {
        View navHeader = navView.getHeaderView(0);
        TextView name = (TextView) navHeader.findViewById(R.id.nav_header_profile_name);
        name.setText(Session.getInstance().myProfile.name + ", " + Integer.toString(Session.getInstance().myProfile.age));

        converter = new Base64Converter();
        ImageView profilePhoto = (ImageView) navHeader.findViewById(R.id.nav_header_profile_picture);
        String base64Photo = Session.getInstance().myProfile.profilePhoto;
        Bitmap bitmap = converter.Base64ToBitmap(base64Photo);
        bitmap = converter.getRoundedCornerBitmap(bitmap,Color.parseColor("#607D8B"),12,1,this);
        profilePhoto.setImageBitmap(bitmap);

        ImageView premiumLogo = (ImageView) navHeader.findViewById(R.id.premium_logo);

        if (Session.getInstance().mySettings.accountType.equals("premium")) {
            premiumLogo.setVisibility(View.VISIBLE);
        } else {
            premiumLogo.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        candidatesFragment = new CandidatesFragment();
        adapter.addFragment(candidatesFragment, "Personas");
        adapter.addFragment(new LinksFragment(), "Links");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            //TODO: ACCION QUE DISPARA EL BOTON EDITAR
//            return true;
//        } else
            if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateNavHeaderView();
    }

}
