package com.example.android.linkup.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.MainActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.candidates.CandidatesFragment;
import com.example.android.linkup.links.LinksFragment;
import com.example.android.linkup.login.LoginActivity;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.models.ChatMessage;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.candidates.ActionOnCandidateResponseListener;
import com.example.android.linkup.profile.ProfileActivity;
import com.example.android.linkup.profile.edit_profile.EditProfileActivity;
import com.example.android.linkup.settings.SettingsActivity;
import com.example.android.linkup.utils.Base64Converter;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

//    private FirebaseListAdapter<ChatMessage> adapter;
    private DrawerLayout mDrawerLayout;
    protected ViewPager fragmentContainer;
    protected Menu menu;
    private Base64Converter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_profile_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ActiveChatProfile.getInstance().profile.name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setTitle(ActiveChatProfile.getInstance().profile.name);
        fragmentContainer = (ViewPager) findViewById(R.id.fragment_container);
        setupViewPager(fragmentContainer);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(fragmentContainer);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        ChatActivity.Adapter adapter = new ChatActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new ChatFragment(), "Chat");
        adapter.addFragment(new ActiveChatProfileFragment(), "Perfil");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem blockItem = menu.add("Bloquear");
        final String id = ActiveChatProfile.getInstance().profile.id;
        blockItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                WebServiceManager.getInstance(getApplicationContext()).blockUser(id);
                return false;
            }
        });

        MenuItem reportItem = menu.add("Denunciar");
        reportItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createReportDialog(id);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void createReportDialog(final String userId) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.description_input_dialog, null);

        final TextView descriptionTextView = (TextView) mView.findViewById(R.id.description_text_field);
        mBuilder.setView(mView);
        mBuilder.setTitle("Razon");
        mBuilder.setPositiveButton("Denunciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = descriptionTextView.getText().toString();
                WebServiceManager.getInstance(getApplicationContext()).reportUser(userId, reason);
            }
        } );

        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onActionSuccessEvent(ActionOnCandidateResponseListener.OnActionSuccessEvent event) {
        finish();
    }
}
