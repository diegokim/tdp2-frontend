package com.example.android.linkup.chat;

import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.candidates.CandidateProfileActivity;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.candidates.ActionOnCandidateResponseListener;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

//    private FirebaseListAdapter<ChatMessage> adapter;
    private DrawerLayout mDrawerLayout;
    protected ViewPager fragmentContainer;
    protected Menu menu;
    private Base64Converter converter;

    private RadioButton spam = null;
    private RadioButton otro = null;
    private RadioButton lenguajeInapropiado = null;
    private RadioButton comportamientoAbusivo = null;
    private RadioGroup grupo_tipo_denuncia = null;

    private String tipoDenuncia = null;

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

        View mView = getLayoutInflater().inflate(R.layout.description_input_dialog, null);
        otro = (RadioButton) mView.findViewById(R.id.rad_otro);
        spam = (RadioButton) mView.findViewById(R.id.rad_spam);
        lenguajeInapropiado = (RadioButton) mView.findViewById(R.id.rad_lenguaje);
        comportamientoAbusivo = (RadioButton) mView.findViewById(R.id.rad_comportamiento);
        grupo_tipo_denuncia = (RadioGroup) mView.findViewById(R.id.grupo_tipo_denuncia);

        tipoDenuncia = "";
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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        Log.d("RadioButton","OnRadioButtonClicked");
        switch (view.getId()) {
            case R.id.rad_comportamiento:
                if (checked) {
                    otro.setChecked(false);
                    spam.setChecked(false);
                    lenguajeInapropiado.setChecked(false);
                    comportamientoAbusivo.setChecked(true);
                    tipoDenuncia = "comportamiento abusivo";
                }
                Log.d("RadioButton","rad_comportamiento");
                break;
            case R.id.rad_lenguaje:
                if (checked) {
                    otro.setChecked(false);
                    spam.setChecked(false);
                    comportamientoAbusivo.setChecked(false);
                    lenguajeInapropiado.setChecked(true);
                    tipoDenuncia = "lenguaje inapropiado";
                }
                Log.d("RadioButton","rad_lenguaje");
                break;
            case R.id.rad_spam:
                if (checked) {
                    otro.setChecked(false);
                    comportamientoAbusivo.setChecked(false);
                    lenguajeInapropiado.setChecked(false);
                    spam.setChecked(true);
                    tipoDenuncia = "spam";
                }
                break;
            case R.id.rad_otro:
                if (checked) {
                    comportamientoAbusivo.setChecked(false);
                    spam.setChecked(false);
                    lenguajeInapropiado.setChecked(false);
                    otro.setChecked(true);
                    tipoDenuncia = "otro";
                }
                break;

        }
    }


    private void createReportDialog(final String userId) {
        final View mView = getLayoutInflater().inflate(R.layout.description_input_dialog, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this).create();

        final TextView descriptionTextView = (TextView) mView.findViewById(R.id.description_text_field);

        alertDialog.setTitle("Denunciar Usuario");
        alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_report));
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spam.setChecked(false);
                comportamientoAbusivo.setChecked(false);
                lenguajeInapropiado.setChecked(false);
                otro.setChecked(false);
                tipoDenuncia = "";
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Denunciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = descriptionTextView.getText().toString();
                if (tipoDenuncia.equals("") || tipoDenuncia.isEmpty()) {
                    Toast.makeText(ChatActivity.this,"Error: Debe elegir un motivo",Toast.LENGTH_SHORT).show();
                } else {
                    WebServiceManager.getInstance(getApplicationContext()).reportUser(userId, reason,tipoDenuncia);
                }

            }
        } );

        alertDialog.setView(mView);
        alertDialog.show();
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
