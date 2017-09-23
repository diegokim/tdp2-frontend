package com.example.android.linkup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.candidates.CandidatesFragment;
import com.example.android.linkup.login.LoginActivity;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.edit_profile.EditProfileRequestGenerator;
import com.example.android.linkup.profile.ProfileActivity;
import com.example.android.linkup.profile.edit_profile.EditProfileActivity;
import com.example.android.linkup.utils.Base64Converter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    protected FrameLayout fragmentContainer;
    protected Menu menu;
    private Base64Converter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);



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
                                startActivity(new Intent (MainActivity.this,SettingsActivity.class));
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

        View navHeader = navigationView.getHeaderView(0);
        TextView name = (TextView) navHeader.findViewById(R.id.nav_header_profile_name);
        name.setText(Session.getInstance().myProfile.name);

//        TextView age = (TextView) navHeader.findViewById(R.id.nav_header_profile_age);
//        age.setText(Session.getInstance().myProfile.age);

        converter = new Base64Converter();
        ImageView profilePhoto = (ImageView) navHeader.findViewById(R.id.nav_header_profile_picture);
        String base64Photo = Session.getInstance().myProfile.profilePhoto;
        Bitmap bitmap = converter.Base64ToBitmap(base64Photo);
        bitmap = converter.getRoundedCornerBitmap(bitmap, Color.GRAY,16,5,this);
        profilePhoto.setImageBitmap(bitmap);

        showCandidatesFragment();
    }

    private void showCandidatesFragment() {
        CandidatesFragment candidatesFragment = new CandidatesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, candidatesFragment).commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
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

}
