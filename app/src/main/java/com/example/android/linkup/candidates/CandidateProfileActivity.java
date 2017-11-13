package com.example.android.linkup.candidates;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.models.CandidateSelectedProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.candidates.ActionOnCandidateResponseListener;
import com.example.android.linkup.profile.ProfileActivity;
import com.example.android.linkup.profile.ProfilePagerAdapter;
import com.example.android.linkup.profile.edit_profile.EditProfileActivity;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CandidateProfileActivity extends BaseActivity {
    private static final String PROFILE_APP_BAR_TEXT = "Mi Perfil" ;

    private TextView name;
    private TextView age;
    private TextView gender;
    private ImageView photo;
    private Profile profile;
    private Base64Converter photoConverter;
    private Menu menu;

    public RadioButton spam = null;
    public RadioButton otro = null;
    public RadioButton lenguajeInapropiado = null;
    public RadioButton comportamientoAbusivo = null;

    public String tipoDenuncia = null;

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

        View mView = getLayoutInflater().inflate(R.layout.description_input_dialog, null);
        otro = (RadioButton) mView.findViewById(R.id.rad_otro);
        spam = (RadioButton) mView.findViewById(R.id.rad_spam);
        lenguajeInapropiado = (RadioButton) mView.findViewById(R.id.rad_lenguaje);
        comportamientoAbusivo = (RadioButton) mView.findViewById(R.id.rad_comportamiento);

        tipoDenuncia = "";
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rad_comportamiento:
                if (checked) {
                    otro.setChecked(false);
                    spam.setChecked(false);
                    lenguajeInapropiado.setChecked(false);
                    comportamientoAbusivo.setChecked(true);
                    tipoDenuncia = "comportamiento abusivo";
                }
                break;
            case R.id.rad_lenguaje:
                if (checked) {
                    otro.setChecked(false);
                    spam.setChecked(false);
                    comportamientoAbusivo.setChecked(false);
                    lenguajeInapropiado.setChecked(true);
                    tipoDenuncia = "lenguaje inapropiado";
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuItem item = menu.add("Denunciar");
        item.setIcon(R.drawable.com_facebook_button_icon);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createReportDialog(CandidateSelectedProfile.getInstance().profile.id);
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
        mBuilder.setTitle("Denunciar usuario");
        mBuilder.setIcon(getResources().getDrawable(R.drawable.ic_report));
        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spam.setChecked(false);
                comportamientoAbusivo.setChecked(false);
                lenguajeInapropiado.setChecked(false);
                otro.setChecked(false);
            }
        });
        mBuilder.setPositiveButton("Denunciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = descriptionTextView.getText().toString();
                if (tipoDenuncia.equals("") || tipoDenuncia.isEmpty()) {
                    Toast.makeText(CandidateProfileActivity.this,"Error: Debe elegir un motivo",Toast.LENGTH_SHORT).show();
                } else {
                    WebServiceManager.getInstance(getApplicationContext()).reportUser(userId, reason,tipoDenuncia);
                }
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

    @Subscribe
    public void onActionSuccessEvent(ActionOnCandidateResponseListener.OnActionSuccessEvent event) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
